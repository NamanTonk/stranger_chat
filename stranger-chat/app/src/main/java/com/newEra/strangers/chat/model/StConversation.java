package com.newEra.strangers.chat.model;

import java.util.List;
import com.newEra.strangers.chat.database.model.StMessage;

public class StConversation {
    private boolean online = false;
    private List<StMessage> stMessageList;
    private StUser stUser;
    private boolean typing = false;

    public StConversation(StUser stUser, List<StMessage> stMessageList) {
        this.stUser = stUser;
        this.stMessageList = stMessageList;
    }

    public StUser getStUser() {
        return this.stUser;
    }

    public void setStUser(StUser stUser) {
        this.stUser = stUser;
    }

    public List<StMessage> getStMessageList() {
        return this.stMessageList;
    }

    public void setStMessageList(List<StMessage> stMessageList) {
        this.stMessageList = stMessageList;
    }

    public boolean isOnline() {
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isTyping() {
        return this.typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }
}
