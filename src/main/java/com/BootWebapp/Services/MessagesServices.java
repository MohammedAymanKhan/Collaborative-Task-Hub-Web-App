package com.BootWebapp.Services;

import com.BootWebapp.DAO.MessageDAO;
import com.BootWebapp.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesServices {

    private final MessageDAO messageDAO;

    @Autowired
    public MessagesServices(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> getMessages(String email,Integer pId){
        return messageDAO.getMessages(email,pId);
    }

    public boolean save(Message message,Integer pId){
        String strPid=null;
        if (pId!=null){
            strPid=pId.toString();
        }

     return messageDAO.insert(message,strPid);
    }

    public boolean update(Message message){

        if(message.getMsgId()!=null){
            String senderEmail = messageDAO.getSenderEmail(message.getMsgId());
            if(message.getSender().equals(senderEmail)){
                return messageDAO.update(message)==1;
            }else {
                return false;
            }
        }else{
            return false;
        }

    }

    public boolean remove(Message message){

        if(message.getMsgId()!=null){
            String senderEmail = messageDAO.getSenderEmail(message.getMsgId());
            if(message.getSender().equals(senderEmail)){
                return messageDAO.delete(message.getMsgId());
            }else {
                return false;
            }
        }else{
            return false;
        }

    }
    
}
