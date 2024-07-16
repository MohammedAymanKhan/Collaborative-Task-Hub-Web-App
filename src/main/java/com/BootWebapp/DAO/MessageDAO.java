package com.BootWebapp.DAO;

import com.BootWebapp.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.BootWebapp.Model.Message.convertBackToNormalMsgId;


@Repository
public class MessageDAO {

    private static final String NEWMSG = "INSERT INTO messages(msgId,message) VALUES(?,?)";
    private static final String NEWMSGREL = "INSERT INTO chatsRelation(msgId,projId,senderEmail,receiverEmail) " +
            "VALUES(?,?,?,?)";
    private static final String UPADTEMSG = "UPDATE messages SET message=? WHERE msgId=?";
    private static final String DELETEMSG = "DELETE FROM messages WHERE msgId=?";
    private static final String DELETEMSGREL = "DELETE FROM chatsrelation WHERE msgId=?";
    private static final String MSGSENDEREMAIL = "SELECT senderEmail FROM chatsrelation WHERE msgId = ? ";
    private static final String GETMESSAGES = "SELECT m.msgId,message, " +
            "CASE WHEN u.email = ? " +
            "THEN 'me' " +
            "ELSE name " +
            "END AS name "+
            "FROM messages m " +
            "INNER JOIN chatsrelation c ON " +
            "m.msgId=c.msgId " +
            "INNER JOIN user u ON " +
            "u.email=c.senderEmail " +
            "WHERE projId=? " +
            "ORDER BY CAST(SUBSTRING_INDEX(m.msgId, '.', -1) AS UNSIGNED)";


    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public MessageDAO(JdbcTemplate jdbcTemplate,TransactionTemplate transactionTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public String getSenderEmail(String msgId){

        return jdbcTemplate.query(MSGSENDEREMAIL ,(ResultSet rs)->{

            if(rs.next()){
                return rs.getString("senderEmail");
            }else {
                return null;
            }

        } ,msgId);

    }

    public List<Message> getMessages(String email,Integer pId){

        List<Message> messageList=jdbcTemplate.query(GETMESSAGES ,
            (ResultSet rs, int rowNum)->{

                return new Message(
                    Message.convertBackToNormalMsgId(
                        rs.getString("msgId")
                    ),
                    rs.getString("message"),
                    rs.getString("name"));

            } ,email ,pId);

        return messageList;

    }

    public boolean insert(Message message,String pId) throws DataAccessException{

        return transactionTemplate.execute((status)->{

            try {

                jdbcTemplate.update(NEWMSG ,message.getMsgId() ,message.getMessageText());
                jdbcTemplate.update(NEWMSGREL ,message.getMsgId() ,pId ,message.getSender(),
                        message.getReceiver());

                return true;

            }catch (DataAccessException e){
                status.setRollbackOnly();
                System.out.println("exception in saving of message"+e.getMessage());
                return false;
            }

        });

    }

    public int update(Message message){

        return jdbcTemplate.update(UPADTEMSG ,message.getMessageText() ,message.getMsgId());

    }

    public boolean delete(String msgId){

        return transactionTemplate.execute((status)->{

            try {

                jdbcTemplate.update(DELETEMSGREL ,msgId);
                jdbcTemplate.update(DELETEMSG ,msgId);
                return true;

            }catch (DataAccessException e){
                System.out.println(e.getMessage());
                status.setRollbackOnly();
                return false;
            }

        });

    }

}
