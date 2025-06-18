package com.newEra.strangers.chat.model;

import java.util.List;

public class MessageStatusWrapper {
    private List<PendingMessageStatus> pendingMessageStatusList;

    public List<PendingMessageStatus> getPendingMessageStatusList() {
        return this.pendingMessageStatusList;
    }

    public void setPendingMessageStatusList(List<PendingMessageStatus> pendingMessageStatusList2) {
        this.pendingMessageStatusList = pendingMessageStatusList2;
    }
}