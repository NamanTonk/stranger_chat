package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetAccountResponse {
    @SerializedName("birthday")
    private String birthday;

    @SerializedName("blocked")
    private boolean blocked;

    private BlockedDto blockedDto;

    @SerializedName("description")
    private String description;

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("email")
    private String email;

    @SerializedName("fbAccountId")
    private String fbAccountId;

    @SerializedName("fcmToken")
    private String fcmToken;

    @SerializedName("friendList")
    private List<StUser> friendList;

    @SerializedName("gender")
    private String gender;

    @SerializedName("interests")
    private String interests;

    @SerializedName("livesIn")
    private String livesIn;

    @SerializedName("nickName")
    private String nickName;

    @SerializedName("profileUrl")
    private String profileUrl;

    @SerializedName("userId")
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName2) {
        this.nickName = nickName2;
    }

    public String getProfileUrl() {
        return this.profileUrl;
    }

    public void setProfileUrl(String profileUrl2) {
        this.profileUrl = profileUrl2;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId2) {
        this.deviceId = deviceId2;
    }

    public List<StUser> getFriendList() {
        return this.friendList;
    }

    public void setFriendList(List<StUser> friendList2) {
        this.friendList = friendList2;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public void setBlocked(boolean blocked2) {
        this.blocked = blocked2;
    }

    public BlockedDto getBlockedDto() {
        return this.blockedDto;
    }

    public void setBlockedDto(BlockedDto blockedDto2) {
        this.blockedDto = blockedDto2;
    }

    public String getFcmToken() {
        return this.fcmToken;
    }

    public void setFcmToken(String fcmToken2) {
        this.fcmToken = fcmToken2;
    }

    public String getFbAccountId() {
        return this.fbAccountId;
    }

    public void setFbAccountId(String fbAccountId2) {
        this.fbAccountId = fbAccountId2;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender2) {
        this.gender = gender2;
    }

    public String getLivesIn() {
        return this.livesIn;
    }

    public void setLivesIn(String livesIn2) {
        this.livesIn = livesIn2;
    }

    public String getInterests() {
        return this.interests;
    }

    public void setInterests(String interests2) {
        this.interests = interests2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday2) {
        this.birthday = birthday2;
    }

}
