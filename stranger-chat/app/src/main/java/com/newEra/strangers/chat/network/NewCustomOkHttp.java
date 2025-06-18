package com.newEra.strangers.chat.network;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.gson.Gson;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.GetAccountResponse;
import com.newEra.strangers.chat.model.ImageData;
import com.newEra.strangers.chat.model.MessagePayload;
import com.newEra.strangers.chat.model.MessageStatusWrapper;
import com.newEra.strangers.chat.model.PendingMessageStatus;
import com.newEra.strangers.chat.model.StUser;
import com.newEra.strangers.chat.model.VersionCheckResponse;
import com.newEra.strangers.chat.model.ad.AdsResponse;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;
import com.newEra.strangers.chat.util.Constants;
import com.newEra.strangers.chat.util.CustomJsonParser;
import com.newEra.strangers.chat.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewCustomOkHttp {
    public static final String ADD_FRIEND_URL = "http://api.makechums.com/user/{user_id}/friend/{friend_id}/add";
    public static final String ADS_URL = "http://api.makechums.com/ads";
    public static final String BLOCK_FRIEND_URL = "http://api.makechums.com/user/{user_id}/friend/{friend_id}/block";
    public static final String CHAT_SERVER_URL = "http://api.makechums.com";
    public static final String FETCH_CREATE_ACCOUNT_URL = "http://api.makechums.com/user";
    public static final String FRIEND_ID_KEY_TO_REPLACE = "{friend_id}";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String PENDING_MESSAGES_URL = "http://api.makechums.com/pending/message";
    public static final String PENDING_STATUS_BULK_URL = "http://api.makechums.com/pending/status/bulk";
    public static final String PENDING_STATUS_URL = "http://api.makechums.com/pending/status";
    public static final String REMOVE_FRIEND_URL = "http://api.makechums.com/user/{user_id}/friend/{friend_id}/remove";
    public static final String REPORT_FRIEND_URL = "http://api.makechums.com/user/{user_id}/friend/{friend_id}/report";
    public static final String SUBMIT_FEEDBACK_URL = "http://api.makechums.com/user/{user_id}/feedback";
    public static final String UPDATE_ACCOUNT_URL = "http://api.makechums.com/user/{user_id}";
    public static final String UPDATE_FCM_TOKEN_URL = "http://api.makechums.com/user/{user_id}/fcm";
    public static final String UPLOAD_IMAGE_URL = "http://api.makechums.com/image";
    public static final String USER_ID_KEY_TO_REPLACE = "{user_id}";
    public static final String VERSION_CHECK_URL = "http://api.makechums.com/user/version/check";
    /* access modifiers changed from: private */
    public static String VERSION_CODE_HEADER_KEY = "version";
    public static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();
    /* access modifiers changed from: private */
    public static Gson gson = new Gson();

    public static GetAccountResponse fetchDataFromApi(String deviceId, boolean resetAccount) {
        try {
            //Response response = client.newCall(new Request.Builder().url(FETCH_CREATE_ACCOUNT_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("device_id", deviceId).addFormDataPart("reset", String.valueOf(resetAccount)).build()).build()).execute();
            Response response = client.newCall(new Request.Builder().url(FETCH_CREATE_ACCOUNT_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("device_id", deviceId).addFormDataPart("reset", String.valueOf(resetAccount)).build()).build()).execute();
            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                return CustomJsonParser.parseGetAccountResponse(response.body().string());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String saveUserDataWithoutImage(StUser user) {
        try {
            Response response = client.newCall(new Request.Builder().url(UPDATE_ACCOUNT_URL.replace(USER_ID_KEY_TO_REPLACE, user.getUserId())).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).put(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("username", user.getNickName()).addFormDataPart("gender", StringUtil.isEmptyStr(user.getGender()) ? "" : user.getGender()).addFormDataPart("lives_in", StringUtil.isEmptyStr(user.getLivesIn()) ? "" : user.getLivesIn()).addFormDataPart("birthday", StringUtil.isEmptyStr(user.getBirthday()) ? "" : user.getBirthday()).addFormDataPart("description", StringUtil.isEmptyStr(user.getAbout()) ? "" : user.getAbout()).build()).build()).execute();
            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                return response.body().string();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String saveUserDataWithImage(StUser user, File file) {
        try {
            Response response = client.newCall(new Request.Builder().url(UPDATE_ACCOUNT_URL.replace(USER_ID_KEY_TO_REPLACE, user.getUserId())).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).put(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("user_id", user.getUserId()).addFormDataPart("username", user.getNickName()).addFormDataPart("gender", StringUtil.isEmptyStr(user.getGender()) ? "" : user.getGender()).addFormDataPart("lives_in", StringUtil.isEmptyStr(user.getLivesIn()) ? "" : user.getLivesIn()).addFormDataPart("birthday", StringUtil.isEmptyStr(user.getBirthday()) ? "" : user.getBirthday()).addFormDataPart("description", StringUtil.isEmptyStr(user.getAbout()) ? "" : user.getAbout()).addFormDataPart(Scopes.PROFILE, file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build()).build()).execute();
            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                return response.body().string();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static StUser addFriend(final String userId, final String friendId) {
        try {
            return (StUser) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass1 */

                public StUser call() throws Exception {
                    try {
                        String url = NewCustomOkHttp.ADD_FRIEND_URL.replace(NewCustomOkHttp.USER_ID_KEY_TO_REPLACE, userId).replace(NewCustomOkHttp.FRIEND_ID_KEY_TO_REPLACE, friendId);
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(url).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(RequestBody.create((MediaType) null, new byte[0])).build()).execute();
                        if (response == null || !response.isSuccessful() || response.body() == null) {
                            return null;
                        }
                        return CustomJsonParser.parseStUser(response.body().string());
                    } catch (Exception e) {
                        System.out.println("");
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean blockUnblockFriend(final String userId, final String friendId, final boolean block) {
        try {
            return (Boolean) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass2 */

                public Boolean call() throws Exception {
                    try {
                        String url = NewCustomOkHttp.BLOCK_FRIEND_URL.replace(NewCustomOkHttp.USER_ID_KEY_TO_REPLACE, userId).replace(NewCustomOkHttp.FRIEND_ID_KEY_TO_REPLACE, friendId);
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(url).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("block", String.valueOf(block)).build()).build()).execute();
                        if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                            return Boolean.valueOf(Boolean.parseBoolean(response.body().string()));
                        }
                    } catch (Exception e) {
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean removeFriend(final String userId, final String friendId) {
        try {
            return (Boolean) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass3 */

                public Boolean call() throws Exception {
                    try {
                        String url = NewCustomOkHttp.REMOVE_FRIEND_URL.replace(NewCustomOkHttp.USER_ID_KEY_TO_REPLACE, userId).replace(NewCustomOkHttp.FRIEND_ID_KEY_TO_REPLACE, friendId);
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(url).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(RequestBody.create((MediaType) null, new byte[0])).build()).execute();
                        if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                            return Boolean.valueOf(Boolean.parseBoolean(response.body().string()));
                        }
                    } catch (Exception e) {
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean reportFriend(final String userId, final String friendId, final String text) {
        try {
            return (Boolean) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass4 */

                public Boolean call() throws Exception {
                    try {
                        String url = NewCustomOkHttp.REPORT_FRIEND_URL.replace(NewCustomOkHttp.USER_ID_KEY_TO_REPLACE, userId).replace(NewCustomOkHttp.FRIEND_ID_KEY_TO_REPLACE, friendId);
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(url).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("text", text).build()).build()).execute();
                        if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                            return Boolean.valueOf(Boolean.parseBoolean(response.body().string()));
                        }
                    } catch (Exception e) {
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public static List<MessagePayload> getPendingMessages(String userId) {
        try {
            Response response = client.newCall(new Request.Builder().url("http://api.makechums.com/pending/message?user_id=" + userId).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).get().build()).execute();
            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                return CustomJsonParser.parsePendingMessages(response.body().string());
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static Boolean savePendingMessage(final MessagePayload messagePayload) {
        try {
            return (Boolean) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass5 */

                public Boolean call() throws Exception {
                    try {
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(NewCustomOkHttp.PENDING_MESSAGES_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(RequestBody.create(NewCustomOkHttp.JSON, NewCustomOkHttp.gson.toJson(messagePayload))).build()).execute();
                        if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                            return Boolean.valueOf(Boolean.parseBoolean(response.body().string()));
                        }
                    } catch (Exception e) {
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public static List<PendingMessageStatus> getPendingMessageStatuses(String userId) {
        try {
            Response response = client.newCall(new Request.Builder().url("http://api.makechums.com/pending/status?user_id=" + userId).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).get().build()).execute();
            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                return CustomJsonParser.parsePendingMessageStatuses(response.body().string());
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void savePendingMessageStatus(PendingMessageStatus pendingMessageStatus) {
        try {
            client.newCall(new Request.Builder().url(PENDING_STATUS_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(RequestBody.create(JSON, gson.toJson(pendingMessageStatus))).build()).enqueue(new Callback() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass6 */

                public void onFailure(Call call, IOException e) {
                }

                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        } catch (Exception e) {
        }
    }

    public static void savePendingMessageStatusBulk(MessageStatusWrapper messageStatusWrapper) {
        try {
            client.newCall(new Request.Builder().url(PENDING_STATUS_BULK_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(RequestBody.create(JSON, gson.toJson(messageStatusWrapper))).build()).enqueue(new Callback() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass7 */

                public void onFailure(Call call, IOException e) {
                }

                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        } catch (Exception e) {
        }
    }

    public static ImageData uploadImage(final File file) {
        try {
            return (ImageData) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass8 */

                public ImageData call() throws Exception {
                    try {
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(NewCustomOkHttp.UPLOAD_IMAGE_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build()).build()).execute();
                        if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                            return (ImageData) NewCustomOkHttp.gson.fromJson(response.body().string(), ImageData.class);
                        }
                    } catch (Exception e) {
                        System.out.println("");
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public static void updateFcmToken(String userId, String fcmToken) {
        try {
            String url = UPDATE_FCM_TOKEN_URL.replace(USER_ID_KEY_TO_REPLACE, userId);
            client.newCall(new Request.Builder().url(url).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(AppMeasurement.FCM_ORIGIN, fcmToken).build()).build()).enqueue(new Callback() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass9 */

                public void onFailure(Call call, IOException e) {
                }

                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        } catch (Exception e) {
        }
    }

    public static VersionCheckResponse checkVersionUpdate(int versionCode) {
        VersionCheckResponse versionCheckResponse = null;
        try {
            Response response = client.newCall(new Request.Builder().url(VERSION_CHECK_URL + ("?version_code=" + String.valueOf(versionCode))).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).get().build()).execute();
            if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                versionCheckResponse = CustomJsonParser.parseVersionCheckResponse(response.body().string());
            }
        } catch (Exception e) {
        }
        if (versionCheckResponse == null || versionCheckResponse.getMessagePopUp() == null || versionCheckResponse.getMessagePopUp().length() <= 0 || (!versionCheckResponse.isForceUpdate() && !versionCheckResponse.isRecommendUpdate())) {
            return null;
        }
        return versionCheckResponse;
    }

    public static Boolean submitFeedback(final String userId, final String feedback) {
        try {
            return (Boolean) CustomThreadPoolInstance.threadPoolExecutor.executeAndWait(new Callable() {
                /* class strangers.chat.network.NewCustomOkHttp.AnonymousClass10 */

                public Boolean call() throws Exception {
                    try {
                        String url = NewCustomOkHttp.SUBMIT_FEEDBACK_URL.replace(NewCustomOkHttp.USER_ID_KEY_TO_REPLACE, userId);
                        Response response = NewCustomOkHttp.client.newCall(new Request.Builder().url(url).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(NewCustomOkHttp.VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("feedback", feedback).build()).build()).execute();
                        if (!(response == null || !response.isSuccessful() || response.body() == null)) {
                            return Boolean.valueOf(Boolean.parseBoolean(response.body().string()));
                        }
                    } catch (Exception e) {
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public static AdsResponse getAdsDetails() {
        try {
            Response response = client.newCall(new Request.Builder().url(ADS_URL).addHeader(Constants.APP_PACKAGE_NAME_API_HEADER_KEY, "strangers.chat").addHeader(VERSION_CODE_HEADER_KEY, String.valueOf(InMemoryCache.VERSION_CODE)).get().build()).execute();
            if (response == null || !response.isSuccessful() || response.body() == null) {
                return null;
            }
            return CustomJsonParser.parseAdsResponse(response.body().string());
        } catch (Exception e) {
            return null;
        }
    }
}