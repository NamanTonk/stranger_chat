package com.newEra.strangers.chat.model.ad;

import com.google.gson.annotations.SerializedName;

public class AdUnitDto {
    @SerializedName("actions_count")
    private int actionsCount;
    @SerializedName("is_disabled")
    private boolean disabled;
    @SerializedName("format")
    private String format;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public AdUnitDto(String id, String name, String format, int actionsCount, boolean disabled) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.actionsCount = actionsCount;
        this.disabled = disabled;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getActionsCount() {
        return this.actionsCount;
    }

    public void setActionsCount(int actionsCount) {
        this.actionsCount = actionsCount;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
