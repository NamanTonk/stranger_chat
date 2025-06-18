package com.newEra.strangers.chat.database.model;

import com.newEra.strangers.chat.model.Image;

public class StMessage {
    private boolean isImageLoding = false;
    private Image localImage;
    private String msg;
    private String msgId;
    private long msgTime;
    private int msgType;
    private Image originalUrl;
    private String status;
    private Image thumbnailUrl;
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getStatus() {
        return this.status;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public Image getLocalImage() {
        return this.localImage;
    }

    public long getMsgTime() {
        return this.msgTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setLocalImage(Image localImage) {
        this.localImage = localImage;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public Image getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(Image thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Image getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(Image originalUrl) {
        this.originalUrl = originalUrl;
    }

    public boolean isImageLoding() {
        return this.isImageLoding;
    }

    public void setImageLoding(boolean imageLoding) {
        this.isImageLoding = imageLoding;
    }
}
