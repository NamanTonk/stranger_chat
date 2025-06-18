package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("image_id")
    private String imageId;
    @SerializedName("original")
    private Image original;
    @SerializedName("room_id")
    private String roomId;
    @SerializedName("thumbnail")
    private Image thumbnail;
    @SerializedName("user_id")
    private String userId;

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageId() {
        return this.imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

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
