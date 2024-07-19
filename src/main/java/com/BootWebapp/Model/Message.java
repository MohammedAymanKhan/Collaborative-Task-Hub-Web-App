package com.BootWebapp.Model;

import org.springframework.stereotype.Component;

@Component
public class Message {

    private String msgId;
    private String messageText;
    private Integer senderId;
    private Integer receiverId;
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

    public Message(String msgId, String messageText, int senderId, int receiverId) {
        this.msgId = msgId;
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Message(String msgId, String messageText, int senderId, int receiverId,
                   String receiverName, String senderName) {
        this.msgId = msgId;
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
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

    public Integer getSender() {
        return senderId;
    }

    public void setSender(int senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiver() {
        return receiverId;
    }

    public void setReceiver(int receiverId) {
        this.receiverId = receiverId;
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
                ", sender='" + senderId + '\'' +
                ", receiver='" + receiverId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
