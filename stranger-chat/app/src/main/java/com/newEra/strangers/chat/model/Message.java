package com.newEra.strangers.chat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private Date date = new Date();
    private ImageData image;
    private boolean imageScrolled;
    private boolean isLoaded;
    private String text;
    private int type;

    public Message(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public Message(int type, ImageData image) {
        this.type = type;
        this.image = image;
    }

    public int getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }

    public ImageData getImage() {
        return this.image;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    public String getHourMinuteTimeStr() {
        return new SimpleDateFormat("HH:mm").format(this.date);
    }

    public boolean isImageScrolled() {
        return this.imageScrolled;
    }

    public void setImageScrolled(boolean imageScrolled) {
        this.imageScrolled = imageScrolled;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(ImageData image) {
        this.image = image;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
