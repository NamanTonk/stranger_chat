package com.newEra.strangers.chat.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.newEra.strangers.chat.model.CustomResponse;
import com.newEra.strangers.chat.model.GetAccountResponse;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.ImageData;
import com.newEra.strangers.chat.model.ImgEventData;
import com.newEra.strangers.chat.model.MessagePayload;
import com.newEra.strangers.chat.model.PendingMessageStatus;
import com.newEra.strangers.chat.model.StUser;
import com.newEra.strangers.chat.model.VersionCheckResponse;
import com.newEra.strangers.chat.model.ad.AdsResponse;

import java.util.Arrays;
import java.util.List;

public class CustomJsonParser {
    private static Gson gson = new GsonBuilder().create();

    public static AdsResponse parseAdsResponse(String jsonString) {
        return (AdsResponse) gson.fromJson(jsonString, AdsResponse.class);
    }

    public static ImageData getImageData(String jsonString) {
        return (ImageData) gson.fromJson(jsonString, ImageData.class);
    }

    public static ImgEventData parseImageEventData(String jsonString) {
        return (ImgEventData) gson.fromJson(jsonString, ImgEventData.class);
    }

    public static Image parseImage(String jsonString) {
        return (Image) gson.fromJson(jsonString, Image.class);
    }

    public static CustomResponse parseCustomResponse(String str) {
        return (CustomResponse) gson.fromJson(str, CustomResponse.class);
    }

    public static VersionCheckResponse parseVersionCheckResponse(String str) {
        return (VersionCheckResponse) gson.fromJson(str, VersionCheckResponse.class);
    }

    public static GetAccountResponse parseGetAccountResponse(String str) {
        return (GetAccountResponse) gson.fromJson(str, GetAccountResponse.class);
    }

    public static StUser parseStUser(String str) {
        return (StUser) gson.fromJson(str, StUser.class);
    }
    public static List<MessagePayload> parsePendingMessages(String str) {
        return gson.fromJson(str, new TypeToken< List<MessagePayload>>() {}.getType());
    }

    public static List<PendingMessageStatus> parsePendingMessageStatuses(String str) {
        return gson.fromJson(str, new TypeToken< List<PendingMessageStatus>>() {}.getType());
    }


}
