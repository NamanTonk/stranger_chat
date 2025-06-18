package com.newEra.strangers.chat.model;

public class PendingMessageStatus {
    private String messageId;
    private String receiverId;
    private String senderId;
    private String status;

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId2) {
        this.senderId = senderId2;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId2) {
        this.receiverId = receiverId2;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId2) {
        this.messageId = messageId2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }
}