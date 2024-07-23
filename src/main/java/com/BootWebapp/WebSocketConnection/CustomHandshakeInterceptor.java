package com.BootWebapp.WebSocketConnection;

import com.BootWebapp.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.OriginHandshakeInterceptor;

import java.util.Map;

@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    private final OriginHandshakeInterceptor originHandshakeInterceptor;

    @Autowired
    public CustomHandshakeInterceptor(OriginHandshakeInterceptor originHandshakeInterceptor){
        this.originHandshakeInterceptor = originHandshakeInterceptor;
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest serverRequest) {
            return serverRequest.getServletRequest().getSession(false);
        }else {
            return null;
        }
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        boolean sameOriginFlag = originHandshakeInterceptor.beforeHandshake(request,response,wsHandler,attributes);

        if(sameOriginFlag) {

            HttpSession session = this.getSession(request);

            if (session != null) {
                User user = (User)session.getAttribute("user");
                attributes.put("user_id",user.getUser_id());
                attributes.put("userName",user.getFirst_name()+" "+user.getLast_name());
            } else {
                return false;
            }
        }

        return  true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
