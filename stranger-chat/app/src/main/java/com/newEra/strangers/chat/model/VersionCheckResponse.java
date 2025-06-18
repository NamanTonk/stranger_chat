package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;

public class VersionCheckResponse {
    @SerializedName("mandatory_upgrade")
    private boolean forceUpdate;
    @SerializedName("message")
    private String messagePopUp;
    @SerializedName("recommend_upgrade")
    private boolean recommendUpdate;

    public VersionCheckResponse(boolean forceUpdate, boolean recommendUpdate, String messagePopUp) {
        this.forceUpdate = forceUpdate;
        this.recommendUpdate = recommendUpdate;
        this.messagePopUp = messagePopUp;
    }

    public boolean isForceUpdate() {
        return this.forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isRecommendUpdate() {
        return this.recommendUpdate;
    }

    public void setRecommendUpdate(boolean recommendUpdate) {
        this.recommendUpdate = recommendUpdate;
    }

    public String getMessagePopUp() {
        return this.messagePopUp;
    }

    public void setMessagePopUp(String messagePopUp) {
        this.messagePopUp = messagePopUp;
    }
}
