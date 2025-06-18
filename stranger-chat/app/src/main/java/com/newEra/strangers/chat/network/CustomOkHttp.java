//package com.newEra.strangers.chat.network;
//
//import android.content.Context;
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.OkHttpClient.Builder;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import com.newEra.strangers.chat.database.model.DbKeys;
//import com.newEra.strangers.chat.model.CustomResponse;
//import com.newEra.strangers.chat.model.GetAccountResponse;
//import com.newEra.strangers.chat.model.Keys;
//import com.newEra.strangers.chat.model.StUser;
//import com.newEra.strangers.chat.model.VersionCheckResponse;
//import com.newEra.strangers.chat.model.ad.AdsResponse;
//import com.newEra.strangers.chat.util.Constants;
//import com.newEra.strangers.chat.util.CustomJsonParser;
//
//public class CustomOkHttp {
//    private static OkHttpClient client = new Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build();
//
//    public static void uploadImage(Context context, File file, String room_id, String strangerId) {
//        if (new NetworkChecker().haveNetworkConnection(context)) {
//            client.newCall(new Request.Builder().url(Constants.UPLOAD_IMAGE_URL).addHeader(Keys.ROOM_ID, room_id).addHeader("receiver_id", strangerId).addHeader(Keys.IMAGE_ID, Long.toString(new Date().getTime())).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("chats", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build()).build()).enqueue(new Callback() {
//                public void onFailure(Call call, IOException e) {
//                }
//
//                public void onResponse(Call call, Response response) throws IOException {
//                    CustomResponse customResponse = CustomJsonParser.parseCustomResponse(response.body().string());
//                    if (customResponse == null || customResponse.isSuccess()) {
//                    }
//                }
//            });
//        }
//    }
//
//    public static void uploadFriendChatImage(Context context, File file, String msgId, String senderId, String receiverId, Long sentAt) {
//        if (new NetworkChecker().haveNetworkConnection(context)) {
//            client.newCall(new Request.Builder().url(Constants.UPLOAD_FRIEND_CHAT_IMAGE_URL).addHeader(DbKeys.KEY_MSG_ID, msgId).addHeader("sender_id", senderId).addHeader("receiver_id", receiverId).addHeader("sent_at", Long.toString(sentAt.longValue())).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("chats", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build()).build()).enqueue(new Callback() {
//                public void onFailure(Call call, IOException e) {
//                }
//
//                public void onResponse(Call call, Response response) throws IOException {
//                }
//            });
//        }
//    }
//
//    public static GetAccountResponse fetchDataFromApi(String deviceId) {
//        String url = Constants.FETCH_OR_CREATE_ACCOUNT_URL + ("?device_id=" + deviceId);
//        try {
//            Response response = client.newCall(new Request.Builder().url(url).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("device_id", deviceId).build()).build()).execute();
//            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
//                return CustomJsonParser.parseGetAccountResponse(response.body().string());
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    public static StUser updateUserData(String userId, String nickName, File file) {
//        String url = Constants.FETCH_OR_CREATE_ACCOUNT_URL + ("?account_id=" + userId + "&username=" + nickName);
//        try {
//            Response response = client.newCall(new Request.Builder().url(url).put(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("profiles", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).addFormDataPart("id", userId).build()).build()).execute();
//            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
//                return CustomJsonParser.parseStUser(response.body().string());
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    public static StUser updateUserData(String userId, File file) {
//        String url = Constants.FETCH_OR_CREATE_ACCOUNT_URL + ("?account_id=" + userId);
//        try {
//            Response response = client.newCall(new Request.Builder().url(url).put(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("profiles", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).addFormDataPart("id", userId).build()).build()).execute();
//            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
//                return CustomJsonParser.parseStUser(response.body().string());
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    public static StUser updateUserData(String userId, String nickName) {
//        String url = Constants.FETCH_OR_CREATE_ACCOUNT_URL + ("?account_id=" + userId + "&username=" + nickName);
//        try {
//            Response response = client.newCall(new Request.Builder().url(url).put(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id", userId).build()).build()).execute();
//            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
//                return CustomJsonParser.parseStUser(response.body().string());
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//}
