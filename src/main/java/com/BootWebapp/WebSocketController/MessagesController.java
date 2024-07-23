package com.BootWebapp.WebSocketController;


import com.BootWebapp.Model.Message;
import com.BootWebapp.Services.MessagesServices;
import com.BootWebapp.WebSocketConnection.WebSocketMessageHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


import java.io.IOException;
import java.util.List;

import static com.BootWebapp.Model.Message.getFormattedMsgId;

@Component
public class MessagesController {

    private final MessagesServices messagesServices;
    private final ObjectMapper converter;

    @Autowired
    public MessagesController(MessagesServices messagesServices,ObjectMapper converter){
        this.messagesServices = messagesServices;
        this.converter = converter;
    }

    public Message convertToObject(String body,Integer pID,WebSocketSession session){

        try{

            Message message = converter.readValue(body,Message.class);
            message.setSender((int)session.getAttributes().get("user_id"));
            message.setMsgId(getFormattedMsgId(message.getMsgId(),pID));
            message.setSenderName(String.valueOf(session.getAttributes().get("userName")));
            return message;

        } catch (JsonProcessingException e) {
            System.out.println("error in convert method: "+e.getMessage());
            return null;
        }

    }

    public void getMessages(Integer pId, WebSocketSession session, WebSocketMessageHandler webSocketHandler) throws IOException {

        try{

            int user_id = (int)session.getAttributes().get("user_id");

            List<Message> messageList = messagesServices.getMessages(user_id,pId);

            if (messageList != null){
                String body = converter.writeValueAsString(messageList);
                String message = "{ \"header\": \"msgReceived\", \"body\": " + body + "}";
                webSocketHandler.sendToUser(message,session);
            }

        }catch (DataAccessException e){
            String errorMsg = "\"Error not able to get old messages\"";
            String message = "{ \"header\": \"error\", \"body\": " + errorMsg + "}";
            webSocketHandler.sendToUser(message,session);
        }

    }

    public void saveMessage(String body, Integer pID,WebSocketMessageHandler webSocketHandler, WebSocketSession session) throws IOException {

        try{

            Message message = convertToObject(body,pID,session);

            boolean flag = messagesServices.save(message,pID);

            if(flag){

                String userBody = "{ \"header\": \"messageSendByMe\", \"body\": " + body + " }";
                webSocketHandler.sendToUser(userBody,session);

                String msgBody = "{ \"header\": \"msgReceived\", \"body\": [" + body + "] }";
                webSocketHandler.forwardMessages(msgBody,session);

            }

        } catch (DataAccessException exc) {
            String errorMsg = "\"Error not able to send message\"";
            webSocketHandler.sendToUser("{ \"header\": \"error\", \"body\": " + errorMsg + "}",session);
        }

    }

    public  void updateMessage(String body, Integer pID,WebSocketMessageHandler webSocketHandler, WebSocketSession session)
            throws IOException {

        try{

            Message message = convertToObject(body,pID,session);

            boolean flag = messagesServices.update(message);

            if(flag){
                String msgBody = "{ \"header\": \"updateMsgGot\", \"body\": " + body + "}";
                webSocketHandler.forwardToSubscriber(msgBody);
            }

        } catch (DataAccessException exc) {
            String errorMsg = "\"Error not able to Update\"";
            webSocketHandler.sendToUser("{ \"header\": \"error\", \"body\": " + errorMsg + "}",session);
        }

    }


    public void deleteMessage(String body,Integer pID,WebSocketMessageHandler webSocketHandler,
                              WebSocketSession session) throws IOException {

        try{

            Message message=convertToObject(body,pID,session);

            boolean flag=messagesServices.remove(message);

            if(flag){
                String msgBody = "{ \"header\": \"msgDeleted\", \"body\": " + body + "}";
                webSocketHandler.forwardToSubscriber(msgBody);
            }

        } catch (DataAccessException exc) {
            String errorMsg = "\"Error not able to delete message\"";
            webSocketHandler.sendToUser("{ \"header\": \"error\", \"body\": " + errorMsg + "}",session);
        }


    }

}
