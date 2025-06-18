package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("height")
    private int height;
    @SerializedName("url")
    private String url;
    @SerializedName("width")
    private int width;

    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return this.url;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
