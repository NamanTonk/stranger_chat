package com.newEra.strangers.chat.database.impl;

import com.newEra.strangers.chat.database.model.DbKeys;
import com.newEra.strangers.chat.database.model.ProfileImage;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.model.StUser;

public class DbQueryHelper {
    public static String userCreateTableQuery() {
        return "CREATE TABLE user (user_id VARCHAR PRIMARY KEY NOT NULL, nick_name VARCHAR(20) NOT NULL, blocked TINYINT(1) NOT NULL, profile_image_url VARCHAR NOT NULL )";
    }

    public static String userProfileCreateTableQuery() {
        return "CREATE TABLE user_profile (user_id VARCHAR PRIMARY KEY NOT NULL, image_name VARCHAR NOT NULL, profile_image_url VARCHAR NOT NULL )";
    }

    public static String messageCreateTableQuery() {
        return "CREATE TABLE message (msg_id VARCHAR PRIMARY KEY NOT NULL, user_id VARCHAR NOT NULL, msg TEXT, msg_status VARCHAR(12) NOT NULL, msg_type INTEGER NOT NULL, local_image_path VARCHAR(12), thumbnail_image_url VARCHAR(12), original_image_url VARCHAR(12), image_width INTEGER, image_height INTEGER, msg_time INTEGER )";
    }

    public static String dropTableQuery(String table) {
        return "DROP TABLE IF EXISTS " + table;
    }

    public static String addUserQuery(StUser stUser) {
        return "INSERT INTO user (user_id, nick_name, blocked, profile_image_url) VALUES('" + stUser.getUserId() + "', '" + stUser.getNickName() + "', " + String.valueOf(stUser.isBlocked() ? 0 : 1) + ", '" + stUser.getProfileImageUrl() + "')";
    }

    public static String addMessageQuery(StMessage stMessage) {
        return "INSERT INTO message (user_id, msg_id, msg, msg_status, msg_type, local_image_path, thumbnail_image_url, original_image_url, image_width, image_height, msg_time) VALUES('" + stMessage.getUserId() + "', '" + stMessage.getMsgId() + "', '" + stMessage.getMsg() + "', '" + stMessage.getStatus() + "', " + Integer.toString(stMessage.getMsgType()) + ", '" + stMessage.getLocalImage().getUrl() + "', '" + stMessage.getThumbnailUrl().getUrl() + "', '" + stMessage.getOriginalUrl().getUrl() + "', " + stMessage.getOriginalUrl().getWidth() + ", " + stMessage.getOriginalUrl().getHeight() + ", " + stMessage.getMsgTime() + ")";
    }

    public static String nickNameUpdateQuery(String userId, String nickName) {
        return "UPDATE user SET nick_name = '" + nickName + "' WHERE " + "user_id" + " = '" + userId + "'";
    }

    public static String profilePicUpdateQuery(String userId, String nickName) {
        return "UPDATE user SET profile_image_url = '" + nickName + "' WHERE " + "user_id" + " = '" + userId + "'";
    }

    public static String userBlockUpdateQuery(String userId, int blocked) {
        return "UPDATE user SET blocked = " + String.valueOf(blocked) + " WHERE " + "user_id" + " = '" + userId + "'";
    }

    public static String messageStatusUpdateQuery(String msgId, String status) {
        return "UPDATE message SET msg_status = '" + status + "' WHERE " + DbKeys.KEY_MSG_ID + " = '" + msgId + "'";
    }

    public static String messageLocalImagePathUpdateQuery(String msgId, String imagePath) {
        return "UPDATE message SET local_image_path = '" + imagePath + "' WHERE " + DbKeys.KEY_MSG_ID + " = '" + msgId + "'";
    }

    public static String selectUsersByBlockedQuery(int blocked) {
        return "SELECT * FROM user WHERE blocked = " + String.valueOf(blocked);
    }

    public static String selectAllMessagesQuery() {
        return "SELECT * FROM message ORDER BY msg_time ASC";
    }

    public static String selectUserByIdQuery(String userId) {
        return "SELECT * FROM user WHERE user_id = '" + userId + "'";
    }

    public static String selectMessagesByUserIdQuery(String userId) {
        return "SELECT * FROM message WHERE user_id = '" + userId + "' ORDER BY " + DbKeys.KEY_MSG_TIME + " ASC";
    }

    public static String getDeleteMessagesByUserIdQuery(String userId) {
        return "DELETE FROM message WHERE user_id = '" + userId + "'";
    }

    public static String getDeleteMessagesByMsgIdQuery(String msgId) {
        return "DELETE FROM message WHERE msg_id = '" + msgId + "'";
    }

    public static String userProfileImageUpdateQuery(ProfileImage profileImage) {
        return "UPDATE user_profile SET image_name = '" + profileImage.getImageName() + "', " + DbKeys.KEY_PROFILE_IMAGE_URL + " = '" + profileImage.getImagePath() + "' WHERE " + "user_id" + " = '" + profileImage.getUserId() + "'";
    }

    public static String selectUserProfileImageByIdQuery(String userId) {
        return "SELECT * FROM user_profile WHERE user_id = '" + userId + "'";
    }

    public static String deleteImageProfileByIdQuery(String userId) {
        return "DELETE FROM user_profile WHERE user_id = '" + userId + "'";
    }

    public static String addUserProfileImageQuery(ProfileImage profileImage) {
        return "INSERT INTO user_profile (user_id, image_name, profile_image_url) VALUES('" + profileImage.getUserId() + "', '" + profileImage.getImageName() + "', '" + profileImage.getImagePath() + "')";
    }
}
