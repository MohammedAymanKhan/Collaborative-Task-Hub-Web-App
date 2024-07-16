package com.BootWebapp.Model;

import org.springframework.stereotype.Component;

@Component
public class RequestHeader {

    private String header;

    public RequestHeader(){}

    public RequestHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
