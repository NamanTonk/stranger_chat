package com.newEra.strangers.chat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import androidx.core.content.FileProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.MessageStatus;
import com.newEra.strangers.chat.database.model.ProfileImage;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.Message;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.model.StUser;

public class MethodUtil {
    public static final Random random = new Random();

    public static File createImageFile() {
        try {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.GALLERY_DIR_SENT);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder.getAbsolutePath() + "/" + Long.toString(new Date().getTime()) + ".png");
            file.createNewFile();
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public static File createCameraTempImageFile(Context context) {
        File file = null;
        try {
            return File.createTempFile(String.valueOf(new Date().getTime()), ".png", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        } catch (IOException e) {
            return file;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) 550) / ((float) width);
        float scaleHeight = ((float) ((height * 550) / width)) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static File getFileFromBitmap(Context context, Bitmap bitmap) {
        try {
            File f = new File(context.getCacheDir(), Long.toString(new Date().getTime()) + ".jpg");
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 0, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    public static File saveBitmap(Bitmap bitmap, int messageType, String imageName) {
        File folder;
        File file;
        if (messageType == 4) {
            folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.GALLERY_DIR_SENT);
        } else if (messageType == 3) {
            folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.GALLERY_DIR_RECEIVED);
        } else {
            folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.GALLERY_DIR_PROFILE);
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (imageName == null) {
            file = new File(folder.getAbsolutePath() + "/" + Long.toString(new Date().getTime()) + ".png");
        } else {
            file = new File(folder.getAbsolutePath() + "/" + imageName);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e2) {
        }
        return file;
    }

    public static List<StMessage> getStMsgFromMsgs(List<Message> messageList, StUser friend) {
        List<StMessage> stMessageList = new ArrayList();
        for (Message message : messageList) {
            StMessage stMessage = new StMessage();
            stMessage.setMsgId(generateMsgId());
            stMessage.setUserId(friend.getUserId());
            stMessage.setMsg(message.getText());
            stMessage.setMsgType(message.getType());
            stMessage.setStatus(MessageStatus.SEEN);
            stMessage.setMsgTime(message.getDate().getTime());
            Image localImage = new Image("", 0, 0);
            Image thumbnailImage = new Image("", 0, 0);
            Image originalImage = new Image("", 0, 0);
            if (message.getType() == 4 || message.getType() == 3) {
                thumbnailImage.setUrl(message.getImage().getThumbnail().getUrl());
                thumbnailImage.setWidth(message.getImage().getThumbnail().getWidth());
                thumbnailImage.setHeight(message.getImage().getThumbnail().getHeight());
                originalImage.setUrl(message.getImage().getOriginal().getUrl());
                originalImage.setWidth(message.getImage().getOriginal().getWidth());
                originalImage.setHeight(message.getImage().getOriginal().getHeight());
                if (message.getType() == 3) {
                    try {
                        localImage.setUrl(FileProvider.getUriForFile(InMemoryCache.context, Constants.FILE_PROVIDER_AUTHORITY, saveBitmap(BitmapFactory.decodeStream(new URL(originalImage.getUrl()).openConnection().getInputStream()), 3, null)).toString());
                        localImage.setWidth(originalImage.getWidth());
                        localImage.setHeight(originalImage.getHeight());
                    } catch (Exception e) {
                    }
                } else if (message.getType() == 4) {
                    localImage.setUrl(originalImage.getUrl());
                    localImage.setWidth(originalImage.getWidth());
                    localImage.setHeight(originalImage.getHeight());
                }
            }
            stMessage.setLocalImage(localImage);
            stMessage.setThumbnailUrl(thumbnailImage);
            stMessage.setOriginalUrl(originalImage);
            stMessageList.add(stMessage);
        }
        return stMessageList;
    }

    public static String generateMsgId() {
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + Constants.alphabatesAndNumbers[random.nextInt(36)];
        }
        return id;
    }

    public static String getMsgTimeString(long time) {
        SimpleDateFormat withoutDate = new SimpleDateFormat("hh:mm a");
        Date msgDate = new Date(time);
        return String.valueOf(msgDate.getDate()) + " " + String.valueOf(Constants.MONTH_NAMES[msgDate.getMonth()]) + " '" + String.valueOf(msgDate.getYear() - 100) + "  " + withoutDate.format(msgDate).replace("am", "AM").replace("pm", "PM");
    }

    public static String getLastMsgTimeStr(long time) {
        SimpleDateFormat withoutDate = new SimpleDateFormat("hh:mm a");
        Date msgDate = new Date(time);
        return msgDate.getDate() == new Date().getDate() ? withoutDate.format(msgDate).replace("am", "AM").replace("pm", "PM") : String.valueOf(msgDate.getDate()) + " " + String.valueOf(Constants.MONTH_NAMES[msgDate.getMonth()]) + " " + String.valueOf((msgDate.getYear() + 2000) - 100);
    }

    public static String getLastOnlineAtStr(long time) {
        SimpleDateFormat withoutDate = new SimpleDateFormat("hh:mm a");
        Date msgDate = new Date(time);
        Date currentDate = new Date();
        String str = withoutDate.format(msgDate).replace("am", "AM").replace("pm", "PM");
        if (msgDate.getDate() == currentDate.getDate()) {
            return "last online today at " + str;
        }
        return "last online on " + String.valueOf(msgDate.getDate()) + " " + String.valueOf(Constants.MONTH_NAMES[msgDate.getMonth()]) + " '" + String.valueOf(msgDate.getYear() - 100) + "  " + str;
    }

    public static boolean isUserExists(List<StConversation> stConversationList, String userId) {
        for (StConversation stConversation : stConversationList) {
            if (userId.equals(stConversation.getStUser().getUserId())) {
                return true;
            }
        }
        return false;
    }

    public static int getNoOfUnseenMsgs(List<StMessage> stMessageList) {
        int num = 0;
        for (StMessage stMessage : stMessageList) {
            if ((stMessage.getMsgType() == 3 || stMessage.getMsgType() == 1) && !stMessage.getStatus().equals(MessageStatus.SEEN)) {
                num++;
            }
        }
        return num;
    }

    public static void saveProfileImagesAsync(Context context) {
        try {
            ProfileImage profileImageDb;
            String imageNameInDb;
            String[] stringsOnServer;
            String imageNameServer;
            for (StConversation stConversation : InMemoryCache.friendsConversationList) {
                profileImageDb = DbHandlerInstance.dbHandler.getProfileImageById(stConversation.getStUser().getUserId());
                if (profileImageDb != null) {
                    imageNameInDb = profileImageDb.getImageName();
                    stringsOnServer = stConversation.getStUser().getProfileImageUrl().split("/");
                    imageNameServer = stringsOnServer[stringsOnServer.length - 1];
                    if (imageNameInDb.equals(imageNameServer)) {
                        stConversation.getStUser().setProfileImageUrl(profileImageDb.getImagePath());
                    } else {
                        saveProfileImage(stConversation, new ProfileImage(stConversation.getStUser().getUserId(), imageNameServer, stConversation.getStUser().getProfileImageUrl()), context);
                    }
                } else {
                    stringsOnServer = stConversation.getStUser().getProfileImageUrl().split("/");
                    addProfileImage(stConversation, new ProfileImage(stConversation.getStUser().getUserId(), stringsOnServer[stringsOnServer.length - 1], stConversation.getStUser().getProfileImageUrl()), context);
                }
            }
            for (StConversation stConversation2 : InMemoryCache.blockedConversationList) {
                profileImageDb = DbHandlerInstance.dbHandler.getProfileImageById(stConversation2.getStUser().getUserId());
                if (profileImageDb != null) {
                    String[] stringsInDb = profileImageDb.getImagePath().split("/");
                    imageNameInDb = stringsInDb[stringsInDb.length - 1];
                    stringsOnServer = stConversation2.getStUser().getProfileImageUrl().split("/");
                    imageNameServer = stringsOnServer[stringsOnServer.length - 1];
                    if (imageNameInDb.equals(imageNameServer)) {
                        stConversation2.getStUser().setProfileImageUrl(profileImageDb.getImagePath());
                    } else {
                        saveProfileImage(stConversation2, new ProfileImage(stConversation2.getStUser().getUserId(), imageNameServer, stConversation2.getStUser().getProfileImageUrl()), context);
                    }
                } else {
                    stringsOnServer = stConversation2.getStUser().getProfileImageUrl().split("/");
                    addProfileImage(stConversation2, new ProfileImage(stConversation2.getStUser().getUserId(), stringsOnServer[stringsOnServer.length - 1], stConversation2.getStUser().getProfileImageUrl()), context);
                }
            }
        } catch (Exception e) {
        }
    }

    private static void saveProfileImage(StConversation stConversation, ProfileImage profileImage, Context context) {
        try {
            String localImageUri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER_AUTHORITY, saveBitmap(BitmapFactory.decodeStream(new URL(profileImage.getImagePath()).openConnection().getInputStream()), -1, stConversation.getStUser().getUserId() + ".png")).toString();
            profileImage.setImagePath(localImageUri);
            DbHandlerInstance.dbHandler.updateProfileImage(profileImage);
            stConversation.getStUser().setProfileImageUrl(localImageUri);
        } catch (Exception e) {
        }
    }

    private static void addProfileImage(StConversation stConversation, ProfileImage profileImage, Context context) {
        try {
            String localImageUri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER_AUTHORITY, saveBitmap(BitmapFactory.decodeStream(new URL(profileImage.getImagePath()).openConnection().getInputStream()), -1, stConversation.getStUser().getUserId() + ".png")).toString();
            profileImage.setImagePath(localImageUri);
            DbHandlerInstance.dbHandler.addProfileImage(profileImage);
            stConversation.getStUser().setProfileImageUrl(localImageUri);
        } catch (Exception e) {
        }
    }
}
