package com.BootWebapp.WenSocketConnection;

import com.BootWebapp.Model.RequestHeader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class WebSocketConnection extends AbstractWebSocketHandler {

    private final Map<Integer, WebSocketMessageHandler> msgHandler=new HashMap<>();
    private final ConfigurableApplicationContext applicationContext;
    private final ObjectMapper converter;

    @Autowired
    public WebSocketConnection(ConfigurableApplicationContext applicationContext, ObjectMapper converter) {
        this.applicationContext = applicationContext;
        this.converter = converter;
    }

    public WebSocketMessageHandler getWebSocketMessageHandler() {
        return applicationContext.getBean(WebSocketMessageHandler.class);
    }

    public void afterConnectionEstablished(WebSocketSession session) {

        HttpHeaders httpHeaders = session.getHandshakeHeaders();

        List<String> cookieHeaders = httpHeaders.get("Cookie");

        for (String str : cookieHeaders.get(0).split(";")) {
            if(str.contains("email")) {
                session.getAttributes().put("email",str.substring(7));
                System.out.println(str.substring(7));
            }
        }

    }


    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String str=message.getPayload();
        String[] data=str.substring(1, str.length() - 1).split(",",2);

        data[0]="{"+data[0]+"}";
        data[1]=data[1].substring(14);

        RequestHeader header=converter.readValue(data[0], RequestHeader.class);

        if(header.getHeader().equals("/subscribe")){
            subscribe(session,Integer.parseInt(data[1].substring(1,data[1].length()-1)));
        }

        if(session.getAttributes().containsKey("projectId")){
            Integer pId= (Integer)session.getAttributes().get("projectId");
            WebSocketMessageHandler webSocketHandler=msgHandler.get(pId);
            webSocketHandler.dispatchMessages(header,data[1],session,pId);
        }

    }

    private void subscribe(WebSocketSession session,Integer projId) throws IOException {

        if(session.getAttributes().containsKey("projectId"))
            msgHandler.get(session.getAttributes().get("projectId")).remove(session);

        if(!msgHandler.containsKey(projId)) {
            msgHandler.put(projId,getWebSocketMessageHandler());
        }

        msgHandler.get(projId).register(session);
        session.getAttributes().put("projectId", projId);
    }

    private void unSubscribe(WebSocketSession session){
        if(session.getAttributes().containsKey("projectId")){
            Integer pId=(Integer)session.getAttributes().get("projectId");
            msgHandler.get(pId).remove(session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        unSubscribe(session);
        System.out.println(exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        unSubscribe(session);
        System.out.println("Connection Closed");
    }



}
