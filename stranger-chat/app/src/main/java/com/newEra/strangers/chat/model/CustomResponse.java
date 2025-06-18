package com.newEra.strangers.chat.model;

import com.google.gson.annotations.SerializedName;

public class CustomResponse {
    @SerializedName("error")
    private String error;
    @SerializedName("success")
    private boolean success;

    public CustomResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
