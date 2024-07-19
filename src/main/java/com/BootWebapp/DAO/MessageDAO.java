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

    private static final String NEW_MSG = "INSERT INTO messages(msgId,message) VALUES(?,?)";
    private static final String NEW_MSG_REL = "INSERT INTO chatsRelation(msgId,projId,sender_id,receiver_id) " +
            "VALUES(?,?,?,?)";
    private static final String UPADTE_MSG = "UPDATE messages SET message=? WHERE msgId=?";
    private static final String DELETE_MSG = "DELETE FROM messages WHERE msgId=?";
    private static final String DELETE_MSG_REL = "DELETE FROM chatsrelation WHERE msgId=?";
    private static final String MSG_SENDER_ID = "SELECT sender_id FROM chatsrelation WHERE msgId = ? ";
    private static final String GET_MESSAGES = "SELECT m.msgId,m.message," +
            " CASE WHEN u.user_id = ? THEN 'me' " +
            " ELSE CONCAT(u.first_name, ' ', u.last_name) " +
            " END AS name " +
            " FROM messages m " +
            " INNER JOIN chatsrelation c ON m.msgId = c.msgId " +
            " INNER JOIN users u ON u.user_id = c.sender_id " +
            " WHERE c.projId = ? " +
            " ORDER BY CAST(SUBSTRING_INDEX(m.msgId, '.', -1) AS UNSIGNED);";


    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public MessageDAO(JdbcTemplate jdbcTemplate,TransactionTemplate transactionTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public int getUserId(String msgId){

        return jdbcTemplate.query(MSG_SENDER_ID ,(ResultSet rs)->{

            if(rs.next()){
                return rs.getInt("sender_id");
            }else {
                return null;
            }

        } ,msgId);

    }

    public List<Message> getMessages(Integer user_Id,Integer pId){

        List<Message> messageList=jdbcTemplate.query(GET_MESSAGES ,
            (ResultSet rs, int rowNum)->{

                return new Message(
                    Message.convertBackToNormalMsgId(
                        rs.getString("msgId")
                    ),
                    rs.getString("message"),
                    rs.getString("name"));

            } ,user_Id ,pId);

        return messageList;

    }

    public boolean insert(Message message,String pId) throws DataAccessException{

        return transactionTemplate.execute((status)->{

            try {

                jdbcTemplate.update(NEW_MSG ,message.getMsgId() ,message.getMessageText());
                jdbcTemplate.update(NEW_MSG_REL ,message.getMsgId() ,pId ,message.getSender(),
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

        return jdbcTemplate.update(UPADTE_MSG ,message.getMessageText() ,message.getMsgId());

    }

    public boolean delete(String msgId){

        return transactionTemplate.execute((status)->{

            try {

                jdbcTemplate.update(DELETE_MSG_REL ,msgId);
                jdbcTemplate.update(DELETE_MSG ,msgId);
                return true;

            }catch (DataAccessException e){
                System.out.println(e.getMessage());
                status.setRollbackOnly();
                return false;
            }

        });

    }

}
