package com.newEra.strangers.chat.util;

import com.newEra.strangers.chat.comparators.ConversationComparator;
import com.newEra.strangers.chat.comparators.SortOrder;
import com.newEra.strangers.chat.comparators.StMessageComparator;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.MessagePayload;
import com.newEra.strangers.chat.model.PendingMessageStatus;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.model.StUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NewUtil {
    public static void addFriend(StUser stUser, List<StMessage> stMessageList, boolean online) {
        if (!isUserExists(InMemoryCache.friendsConversationList, stUser.getUserId())) {
            StConversation stConversation = new StConversation(stUser, stMessageList);
            stConversation.setOnline(online);
            InMemoryCache.friendsConversationList.add(0, stConversation);
            DbHandlerInstance.dbHandler.addMessages(stMessageList);
            Collections.sort(stMessageList, new StMessageComparator(SortOrder.ASCENDING));
            Collections.sort(InMemoryCache.friendsConversationList, new ConversationComparator(SortOrder.DESCENDING));
        }
    }

    public static void moveBlockedFriend(String userId) {
        StConversation blockedConversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, userId);
        if (blockedConversation != null) {
            InMemoryCache.friendsConversationList.remove(blockedConversation);
            blockedConversation.getStUser().setBlocked(true);
            InMemoryCache.blockedConversationList.add(0, blockedConversation);
            DbHandlerInstance.dbHandler.updateUserBlockStatus(blockedConversation.getStUser().getUserId(), 1);
        }
    }

    public static void moveUnBlockedFriend(String userId) {
        StConversation unblockedConversation = getStConversationFromListByUserId(InMemoryCache.blockedConversationList, userId);
        if (unblockedConversation != null) {
            InMemoryCache.blockedConversationList.remove(unblockedConversation);
            unblockedConversation.getStUser().setBlocked(false);
            InMemoryCache.friendsConversationList.add(unblockedConversation);
            DbHandlerInstance.dbHandler.updateUserBlockStatus(unblockedConversation.getStUser().getUserId(), 0);
        }
    }

    public static void deleteConversation(String userId) {
        StConversation conversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, userId);
        if (conversation != null) {
            InMemoryCache.friendsConversationList.remove(conversation);
            DbHandlerInstance.dbHandler.deleteMessagesByUserId(userId);
            return;
        }
        StConversation conversation2 = getStConversationFromListByUserId(InMemoryCache.blockedConversationList, userId);
        if (conversation2 != null) {
            InMemoryCache.blockedConversationList.remove(conversation2);
            DbHandlerInstance.dbHandler.deleteMessagesByUserId(userId);
        }
    }

    public static boolean isUserOnline(String userId) {
        StConversation conversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, userId);
        if (conversation != null) {
            return conversation.isOnline();
        }
        return false;
    }

    public static MessagePayload convertStMessageToMessagePayload(StMessage stMessage) {
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setMessageId(stMessage.getMsgId());
        messagePayload.setSenderId(InMemoryCache.myself.getUserId());
        messagePayload.setReceiverId(stMessage.getUserId());
        messagePayload.setText(stMessage.getMsg());
        messagePayload.setType(Integer.valueOf(stMessage.getMsgType() == 4 ? 3 : 1));
        messagePayload.setOriginal(stMessage.getOriginalUrl());
        messagePayload.setThumbnail(stMessage.getThumbnailUrl());
        messagePayload.setMsgTime(Long.valueOf(stMessage.getMsgTime()));
        return messagePayload;
    }

    public static StMessage convertMessagePayloadToStMessage(MessagePayload messagePayload, String messageStatus) {
        StMessage stMessage = new StMessage();
        stMessage.setMsgId(messagePayload.getMessageId());
        stMessage.setMsg(messagePayload.getText());
        stMessage.setUserId(messagePayload.getSenderId());
        stMessage.setMsgType(messagePayload.getType().intValue());
        stMessage.setStatus(messageStatus);
        stMessage.setLocalImage(new Image("", 0, 0));
        stMessage.setOriginalUrl(messagePayload.getOriginal());
        stMessage.setThumbnailUrl(messagePayload.getThumbnail());
        stMessage.setMsgTime(messagePayload.getMsgTime().longValue());
        return stMessage;
    }

    public static boolean isUserExists(List<StConversation> stConversationList, String userId) {
        return getStConversationFromListByUserId(stConversationList, userId) != null;
    }

    public static void addMsgInConversationAndSave(StMessage stMessage) {
        StConversation conversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, stMessage.getUserId());
        if (conversation != null && !isMessageExists(stMessage.getMsgId(), conversation.getStMessageList())) {
            conversation.getStMessageList().add(stMessage);
            DbHandlerInstance.dbHandler.addMessage(stMessage);
            Collections.sort(conversation.getStMessageList(), new StMessageComparator(SortOrder.ASCENDING));
            Collections.sort(InMemoryCache.friendsConversationList, new ConversationComparator(SortOrder.DESCENDING));
        }
    }

    public static boolean isMessageExists(String msgId, List<StMessage> stMessageList) {
        return getStMessageFromListById(stMessageList, msgId) != null;
    }

    public static StMessage getStMessageFromListById(List<StMessage> stMessageList, String msgId) {
        if (stMessageList != null && StringUtil.isNotEmptyStr(msgId)) {
            for (int i = 0; i < stMessageList.size(); i++) {
                if (i < stMessageList.size()) {
                    StMessage stMessage = stMessageList.get(i);
                    if (msgId.equalsIgnoreCase(stMessage.getMsgId())) {
                        return stMessage;
                    }
                }
            }
        }
        return null;
    }

    public static void updateTypingStatusInConversation(String userId, boolean typing) {
        StConversation conversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, userId);
        if (conversation != null) {
            conversation.setTyping(typing);
        }
    }

    public static void updateMsgStatusInConversation(PendingMessageStatus pendingMessageStatus) {
        StMessage message = null;
        StConversation conversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, pendingMessageStatus.getSenderId());
        if (conversation != null) {
            message = getStMessageFromListById(conversation.getStMessageList(), pendingMessageStatus.getMessageId());
        }
        if (message != null) {
            message.setStatus(pendingMessageStatus.getStatus());
            DbHandlerInstance.dbHandler.updateMessageStatus(message.getMsgId(), message.getStatus());
        }
    }

    public static void setOnlineStatus(String userId, boolean status, boolean setCurrentTime) {
        StConversation conversation = getStConversationFromListByUserId(InMemoryCache.friendsConversationList, userId);
        if (conversation != null) {
            conversation.setOnline(status);
            if (!status && setCurrentTime) {
                conversation.getStUser().setLastOnlineAt(Long.valueOf(new Date().getTime()));
            }
        }
    }

    public static List<String> getFriendUserIds() {
        List<String> friendIds = new ArrayList<>();
        int i = 0;
        while (i < InMemoryCache.friendsConversationList.size()) {
            try {
                if (i < InMemoryCache.friendsConversationList.size()) {
                    friendIds.add(InMemoryCache.friendsConversationList.get(i).getStUser().getUserId());
                }
                i++;
            } catch (Exception e) {
            }
        }
        return friendIds;
    }

    public static boolean isUserBlocked(String userId) {
        return getStConversationFromListByUserId(InMemoryCache.blockedConversationList, userId) != null;
    }

    public static StConversation getStConversationFromListByUserId(List<StConversation> stConversationList, String userId) {
        if (stConversationList != null && StringUtil.isNotEmptyStr(userId)) {
            int i = 0;
            while (i < stConversationList.size()) {
                try {
                    if (i < stConversationList.size()) {
                        StConversation stConversation = stConversationList.get(i);
                        if (userId.equalsIgnoreCase(stConversation.getStUser().getUserId())) {
                            return stConversation;
                        }
                    }
                    i++;
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
}