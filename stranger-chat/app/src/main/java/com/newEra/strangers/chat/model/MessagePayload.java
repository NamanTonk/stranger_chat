package com.newEra.strangers.chat.model;

public class MessagePayload {
    private String messageId;
    private Long msgTime;
    private Image original;
    private String receiverId;
    private String senderId;
    private String text;
    private Image thumbnail;
    private Integer type;

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

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type2) {
        this.type = type2;
    }

    public Image getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Image thumbnail2) {
        this.thumbnail = thumbnail2;
    }

    public Image getOriginal() {
        return this.original;
    }

    public void setOriginal(Image original2) {
        this.original = original2;
    }

    public Long getMsgTime() {
        return this.msgTime;
    }

    public void setMsgTime(Long msgTime2) {
        this.msgTime = msgTime2;
    }
}