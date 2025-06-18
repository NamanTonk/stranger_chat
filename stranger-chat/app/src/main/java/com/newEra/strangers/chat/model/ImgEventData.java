package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;

public class ImgEventData {
    @SerializedName("original")
    private Image original;
    @SerializedName("thumbnail")
    private Image thumbnail;

    public Image getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getOriginal() {
        return this.original;
    }

    public void setOriginal(Image original) {
        this.original = original;
    }
}
