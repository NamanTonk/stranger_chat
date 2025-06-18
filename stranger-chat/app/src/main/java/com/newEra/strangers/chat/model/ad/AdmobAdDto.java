package com.newEra.strangers.chat.model.ad;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AdmobAdDto {
    @SerializedName("ads")
    private List<AdUnitDto> ads;
    @SerializedName("app_id")
    private String appId;

    public AdmobAdDto(String appId, List<AdUnitDto> ads) {
        this.appId = appId;
        this.ads = ads;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<AdUnitDto> getAds() {
        return this.ads;
    }

    public void setAds(List<AdUnitDto> ads) {
        this.ads = ads;
    }
}
