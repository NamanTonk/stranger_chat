package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;

public class StUser {

    @SerializedName("about")
    private String about;

    @SerializedName("age")
    private String age;

    @SerializedName("appPackage")
    private String appPackage;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("blocked")
    private boolean blocked = false;

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("email")
    private String email;

    @SerializedName("fbAccountId")
    private String fbAccountId;

    @SerializedName("fcmToken")
    private String fcmToken;

    @SerializedName("gender")
    private String gender;

    @SerializedName("interests")
    private String interests;

    @SerializedName("lastOnlineAt")
    private Long lastOnlineAt;

    @SerializedName("livesIn")
    private String livesIn;

    @SerializedName("nickName")
    private String nickName;

    @SerializedName("profileImageUrl")
    private String profileImageUrl;

    @SerializedName("userId")
    private String userId;


    public StUser(String userId, String deviceId, String nickName, String profileImageUrl, boolean blocked) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.blocked = blocked;
    }

    public StUser() {
    }

    public String getUserId() {
        return this.userId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getLastOnlineAt() {
        return this.lastOnlineAt;
    }

    public void setLastOnlineAt(long lastOnlineAt) {
        this.lastOnlineAt = lastOnlineAt;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbAccountId() {
        return fbAccountId;
    }

    public void setFbAccountId(String fbAccountId) {
        this.fbAccountId = fbAccountId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setLastOnlineAt(Long lastOnlineAt) {
        this.lastOnlineAt = lastOnlineAt;
    }

    public String getLivesIn() {
        return livesIn;
    }

    public void setLivesIn(String livesIn) {
        this.livesIn = livesIn;
    }
}
