package com.BootWebapp.Model;

import org.springframework.stereotype.Component;

@Component
public class Message {

    private String msgId;
    private String messageText;
    private String sender;
    private String receiver;
    private String receiverName;
    private String senderName;

    public static String getFormattedMsgId(String msgId, Integer pId){
        return pId+"."+msgId;
    }

    public static String convertBackToNormalMsgId(String msgId){
        return msgId.split("\\.")[1];
    }

    public Message(){}

    public Message(Integer msgId){
        this.msgId=msgId.toString();
    }

    public Message(String msgId,String messageText,String senderName){
        this.msgId=msgId;
        this.messageText=messageText;
        this.senderName=senderName;
    }

    public Message(String msgId, String messageText, String sender, String receiver) {
        this.msgId = msgId;
        this.messageText = messageText;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message(String msgId, String messageText, String sender, String receiver,
                   String receiverName, String senderName) {
        this.msgId = msgId;
        this.messageText = messageText;
        this.sender = sender;
        this.receiver = receiver;
        this.receiverName = receiverName;
        this.senderName = senderName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId='" + msgId + '\'' +
                ", messageText='" + messageText + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
