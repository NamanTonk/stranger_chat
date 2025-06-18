package com.newEra.strangers.chat.model.ad;

import com.google.gson.annotations.SerializedName;

public class AdsResponse {
    @SerializedName("admob")
    private AdmobAdDto admob;
    @SerializedName("facebook")
    private AdmobAdDto facebook;

    public AdsResponse(AdmobAdDto admob) {
        this.admob = admob;
    }

    public AdsResponse(){}

    public AdmobAdDto getAdmob() {
        return this.admob;
    }

    public void setAdmob(AdmobAdDto admob) {
        this.admob = admob;
    }

    public AdmobAdDto getFacebook() {
        return this.facebook;
    }

    public void setFacebook(AdmobAdDto facebook) {
        this.facebook = facebook;
    }
}
