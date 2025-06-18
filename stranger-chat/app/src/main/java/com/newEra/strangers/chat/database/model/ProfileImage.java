package com.newEra.strangers.chat.database.model;

public class ProfileImage {
    private String imageName;
    private String imagePath;
    private String userId;

    public ProfileImage(String userId, String imageName, String imagePath) {
        this.userId = userId;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public ProfileImage(){}

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
