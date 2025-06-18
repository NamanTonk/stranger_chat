package com.newEra.strangers.chat.database.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.newEra.strangers.chat.database.model.DbKeys;
import com.newEra.strangers.chat.database.model.ProfileImage;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.StUser;

public class DbHandler extends SQLiteOpenHelper {
    private SQLiteDatabase readableDb = getReadableDatabase();
    private SQLiteDatabase writableDb = getWritableDatabase();

    public DbHandler(Context context) {
        super(context, DbKeys.DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DbQueryHelper.userCreateTableQuery());
            db.execSQL(DbQueryHelper.messageCreateTableQuery());
            db.execSQL(DbQueryHelper.userProfileCreateTableQuery());
        } catch (SQLException e) {
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DbQueryHelper.dropTableQuery(DbKeys.TABLE_USER));
            db.execSQL(DbQueryHelper.dropTableQuery("message"));
            db.execSQL(DbQueryHelper.dropTableQuery(DbKeys.TABLE_USER_PROFILE));
        } catch (SQLException e) {
        }
        onCreate(db);
    }

    public boolean addUser(StUser stUser) {
        try {
            this.writableDb.execSQL(DbQueryHelper.addUserQuery(stUser));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addMessages(List<StMessage> stMessageList) {
        for (StMessage stMessage : stMessageList) {
            try {
                this.writableDb.execSQL(DbQueryHelper.addMessageQuery(stMessage));
            } catch (SQLException e) {
            }
        }
        return true;
    }

    public boolean addMessage(StMessage stMessage) {
        try {
            this.writableDb.execSQL(DbQueryHelper.addMessageQuery(stMessage));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateNickName(String userId, String nickName) {
        try {
            this.writableDb.execSQL(DbQueryHelper.nickNameUpdateQuery(userId, nickName));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateProfilePic(String userId, String imageUrl) {
        try {
            this.writableDb.execSQL(DbQueryHelper.profilePicUpdateQuery(userId, imageUrl));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateUserBlockStatus(String userId, int blocked) {
        try {
            this.writableDb.execSQL(DbQueryHelper.userBlockUpdateQuery(userId, blocked));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateMessageStatus(String msgId, String status) {
        try {
            this.writableDb.execSQL(DbQueryHelper.messageStatusUpdateQuery(msgId, status));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateLocalImagePath(String msgId, String imagePath) {
        try {
            this.writableDb.execSQL(DbQueryHelper.messageLocalImagePathUpdateQuery(msgId, imagePath));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<StUser> getFriends() {
        List<StUser> stUsersList = new ArrayList();
        try {
            Cursor cursor = this.readableDb.rawQuery(DbQueryHelper.selectUsersByBlockedQuery(1), null);
            if (cursor.moveToFirst()) {
                do {
                    StUser stUser = new StUser();
                    stUser.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
                    stUser.setNickName(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_NICK_NAME)));
                    stUser.setProfileImageUrl(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_PROFILE_IMAGE_URL)));
                    stUser.setBlocked(false);
                    stUsersList.add(stUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        }
        return stUsersList;
    }

    public List<StUser> getBlockedUsers() {
        List<StUser> stUsersList = new ArrayList();
        try {
            Cursor cursor = this.readableDb.rawQuery(DbQueryHelper.selectUsersByBlockedQuery(0), null);
            if (cursor.moveToFirst()) {
                do {
                    StUser stUser = new StUser();
                    stUser.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
                    stUser.setNickName(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_NICK_NAME)));
                    stUser.setProfileImageUrl(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_PROFILE_IMAGE_URL)));
                    stUser.setBlocked(true);
                    stUsersList.add(stUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        }
        return stUsersList;
    }

    public List<StMessage> getMessagesByUserId(String userId) {
        List<StMessage> stMessageList = new ArrayList();
        try {
            Cursor cursor = this.readableDb.rawQuery(DbQueryHelper.selectMessagesByUserIdQuery(userId), null);
            if (cursor.moveToFirst()) {
                do {
                    StMessage stMessage = new StMessage();
                    stMessage.setMsgId(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_MSG_ID)));
                    stMessage.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
                    stMessage.setMsg(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_MSG)));
                    stMessage.setStatus(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_MSG_STATUS)));
                    stMessage.setMsgType(cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_MSG_TYPE)));
                    stMessage.setMsgTime(cursor.getLong(cursor.getColumnIndex(DbKeys.KEY_MSG_TIME)));
                    Image localImage = new Image(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_LOCAL_IMAGE_PATH)), cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_IMAGE_WIDTH)), cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_IMAGE_HEIGHT)));
                    Image thumbnailImage = new Image(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_THUMBNAIL_IMAGE_URL)), cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_IMAGE_WIDTH)), cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_IMAGE_HEIGHT)));
                    Image originalImage = new Image(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_ORIGINAL_IMAGE_URL)), cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_IMAGE_WIDTH)), cursor.getInt(cursor.getColumnIndex(DbKeys.KEY_IMAGE_HEIGHT)));
                    stMessage.setLocalImage(localImage);
                    stMessage.setThumbnailUrl(thumbnailImage);
                    stMessage.setOriginalUrl(originalImage);
                    stMessageList.add(stMessage);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        }
        return stMessageList;
    }

    public boolean deleteMessagesByUserId(String userId) {
        try {
            this.writableDb.execSQL(DbQueryHelper.getDeleteMessagesByUserIdQuery(userId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteMessagesByMsgId(String msgId) {
        try {
            this.writableDb.execSQL(DbQueryHelper.getDeleteMessagesByMsgIdQuery(msgId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateProfileImage(ProfileImage profileImage) {
        try {
            this.writableDb.execSQL(DbQueryHelper.userProfileImageUpdateQuery(profileImage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ProfileImage getProfileImageById(String userId) {
        ProfileImage profileImage = null;
        try {
            Cursor cursor = this.readableDb.rawQuery(DbQueryHelper.selectUserProfileImageByIdQuery(userId), null);
            if (!cursor.moveToFirst()) {
                return null;
            }
            do {
                ProfileImage profileImage2 = profileImage;
                try {
                    profileImage = new ProfileImage();
                    profileImage.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
                    profileImage.setImageName(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_IMAGE_NAME)));
                    profileImage.setImagePath(cursor.getString(cursor.getColumnIndex(DbKeys.KEY_PROFILE_IMAGE_URL)));
                } catch (Exception e) {
                    return profileImage2;
                }
            } while (cursor.moveToNext());
            return profileImage;
        } catch (Exception e2) {
            return profileImage;
        }
    }

    public boolean addProfileImage(ProfileImage profileImage) {
        try {
            this.writableDb.execSQL(DbQueryHelper.addUserProfileImageQuery(profileImage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteProfileImageById(String userId) {
        try {
            this.writableDb.execSQL(DbQueryHelper.deleteImageProfileByIdQuery(userId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
