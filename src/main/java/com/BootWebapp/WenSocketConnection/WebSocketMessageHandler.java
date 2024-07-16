package com.BootWebapp.WenSocketConnection;

import com.BootWebapp.WebSocketController.MessagesController;
import com.BootWebapp.WebSocketController.ProjectReportController;
import com.BootWebapp.Model.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Component
@Scope("prototype")
public class WebSocketMessageHandler {

    private ProjectReportController projRepController;
    private MessagesController messagesController;

    private final Set<WebSocketSession> sessionSet = new HashSet<>();
    private final WebSocketMessageHandler webSocketHandler;
    public WebSocketMessageHandler() {
        this.webSocketHandler=this;
    }

    @Autowired
    public void setProjRepController(ProjectReportController projRepController) {
        this.projRepController = projRepController;
    }

    @Autowired
    public void setMessagesController(MessagesController messagesController) {
        this.messagesController = messagesController;
    }

    public void dispatchMessages(RequestHeader header, String body, WebSocketSession session, Integer pId) throws IOException {

        /* to get all project Details and Messages of particular Project */
        if(header.getHeader().equals("/subscribe")){
            projRepController.getProjectReport(webSocketHandler,session,pId);
            messagesController.getMessages(pId,session,webSocketHandler);
        }else {

            /* For Project Details */
            switch (header.getHeader()) {
                case "/insert":
                    projRepController.insertNewProjReport(body, pId, webSocketHandler, session);
                    break;
                case "/update":
                    projRepController.updateProjRep(body, webSocketHandler, session);
                    break;
                case "/delete":
                    projRepController.deleteProjReport(body, webSocketHandler, session);
                    break;

            }

            /* For Chat Messages */
            switch (header.getHeader()) {
                case "/gotMsg":
                    messagesController.saveMessage(body, pId, webSocketHandler, session);
                    break;
                case "/updateMessage":
                    messagesController.updateMessage(body, pId, webSocketHandler, session);
                    break;
                case "/deleteMsg":
                    messagesController.deleteMessage(body, pId, webSocketHandler, session);
                    break;

                /* No Such Operation error message send back to client */
                default:
                    String errorMsg = "\"Internal Server error\"";
                    String message = "{ \"header\": \"error\", \"body\": " + errorMsg + "}";
                    this.sendToUser(message, session);
            }
        }

    }

    public void forwardToSubscriber(String message) throws IOException {

        WebSocketMessage<String> webSocketMessage = new TextMessage(message);

        for (WebSocketSession session : sessionSet) {
            session.sendMessage(webSocketMessage);
        }
    }

    public void forwardMessages(String message,WebSocketSession UserSession) throws IOException {

        WebSocketMessage<String> webSocketMessage = new TextMessage(message);

        for (WebSocketSession session : sessionSet) {
            if (!UserSession.equals(session)) {
                session.sendMessage(webSocketMessage);
            }
        }

    }

    public void sendToUser(String message, WebSocketSession session) throws IOException {

        session.sendMessage(new TextMessage(message));

    }

    public void register(WebSocketSession session) {
        sessionSet.add(session);
    }

    public void remove(WebSocketSession session) {
        sessionSet.remove(session);
    }


}
















