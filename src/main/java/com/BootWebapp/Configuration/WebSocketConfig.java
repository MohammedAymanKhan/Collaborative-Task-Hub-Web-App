package com.BootWebapp.Configuration;

import com.BootWebapp.WebSocketConnection.CustomHandshakeInterceptor;
import com.BootWebapp.WebSocketConnection.WebSocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketConnection webSocketHandler;
    private final CustomHandshakeInterceptor handshakeInterceptor;

    @Autowired
    public WebSocketConfig(WebSocketConnection webSocketHandler,CustomHandshakeInterceptor handshakeInterceptor){
        this.webSocketHandler = webSocketHandler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/projectReports")
                .addInterceptors(handshakeInterceptor);
    }

}