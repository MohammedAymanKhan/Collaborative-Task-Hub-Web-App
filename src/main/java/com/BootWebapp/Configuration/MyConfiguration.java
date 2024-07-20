package com.BootWebapp.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.support.OriginHandshakeInterceptor;

import java.util.List;

@Configuration
public class MyConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    @Bean
    public OriginHandshakeInterceptor originHandshakeInterceptor(){
        return new OriginHandshakeInterceptor(List.of("http://localhost:8080"));
    }

}

