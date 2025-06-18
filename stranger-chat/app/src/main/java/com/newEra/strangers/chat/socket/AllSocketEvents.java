package com.newEra.strangers.chat.socket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newEra.strangers.chat.comparators.ConversationComparator;
import com.newEra.strangers.chat.comparators.SortOrder;
import com.newEra.strangers.chat.comparators.StMessageComparator;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.MessageStatus;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.ImgEventData;
import com.newEra.strangers.chat.model.MessagePayload;
import com.newEra.strangers.chat.model.PendingMessageStatus;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.model.StUser;
import com.newEra.strangers.chat.network.NewCustomOkHttp;
import com.newEra.strangers.chat.socket.SocketUtil;
import com.newEra.strangers.chat.util.CustomJsonParser;
import com.newEra.strangers.chat.util.MethodUtil;
import com.newEra.strangers.chat.util.NewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.socket.emitter.Emitter;

public class AllSocketEvents {
    /* access modifiers changed from: private */
    public static Gson gson = new Gson();

    /* access modifiers changed from: private */
    public static void initializePersonalRoomAndEvents() {
        listenPersonalRoomStatus();
        listenOnlineStatus();
        listenFriendTextMsg();
        listenFriendTypingStart();
        listenFriendTypingEnd();
        listenFriendMsgStatus();
        listenOnlineStatusFriendList();
        triggerGetPersonalRoomEvent();
    }

    public static void listenConnectedEvent() {
        SocketUtil.socket.on("connected", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass1 */

            public void call(Object... args) {
                AllSocketEvents.initializePersonalRoomAndEvents();
            }
        });
    }

    public static void triggerGetPersonalRoomEvent() {
        SocketUtil.socket.emit("get_personal_room", InMemoryCache.myself.getUserId(), gson.toJson(NewUtil.getFriendUserIds()), "strangers.chat");
    }

    public static void triggerSendFriendMsg(StMessage stMessage, StUser stUser) {
        stMessage.setStatus(MessageStatus.SENT);
        MessagePayload messagePayload = NewUtil.convertStMessageToMessagePayload(stMessage);
        boolean isUserOnline = NewUtil.isUserOnline(stMessage.getUserId());
        if (isUserOnline) {
            SocketUtil.socket.emit("send_friend_msg", stMessage.getUserId(), gson.toJson(messagePayload), Boolean.valueOf(isUserOnline));
        } else {
            NewCustomOkHttp.savePendingMessage(messagePayload);
            triggerFriendFcmNotification(stUser.getFcmToken(), InMemoryCache.myself.getNickName(), stMessage.getMsgType() == 4 ? "IMAGE" : stMessage.getMsg(), Long.valueOf(stMessage.getMsgTime()), stUser.getAppPackage());
        }
        NewUtil.addMsgInConversationAndSave(stMessage);
    }

    public static void triggerFriendFcmNotification(String receiverFcmToken, String senderUserName, String text, Long sentAt, String receiverPackageName) {
        SocketUtil.socket.emit("send_friend_fcm_msg", receiverFcmToken, senderUserName, text, sentAt, receiverPackageName);
    }

    public static void triggerFriendTypingStart(String userId) {
        if (NewUtil.isUserOnline(userId)) {
            SocketUtil.socket.emit("send_friend_typing_start", InMemoryCache.myself.getUserId(), userId);
        }
    }

    public static void triggerFriendTypingEnd(String userId) {
        if (NewUtil.isUserOnline(userId)) {
            SocketUtil.socket.emit("send_friend_typing_end", InMemoryCache.myself.getUserId(), userId);
        }
    }

    public static void triggerFriendMessageStatus(PendingMessageStatus pendingMessageStatus) {
        if (NewUtil.isUserOnline(pendingMessageStatus.getReceiverId())) {
            SocketUtil.socket.emit("send_friend_msg_status", pendingMessageStatus.getReceiverId(), gson.toJson(pendingMessageStatus));
            return;
        }
        NewCustomOkHttp.savePendingMessageStatus(pendingMessageStatus);
    }

    public static void listenPersonalRoomStatus() {
        SocketUtil.socket.on("personal_room_status", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass2 */

            public void call(Object... args) {
                try {
                    InMemoryCache.personalRoomAllotted = Boolean.parseBoolean(args[0].toString());
                    if (!InMemoryCache.personalRoomAllotted) {
                        AllSocketEvents.triggerGetPersonalRoomEvent();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public static void listenFriendTextMsg() {
        SocketUtil.socket.on("receive_friend_msg", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass3 */

            public void call(Object... args) {
                StUser stUser;
                try {
                    StMessage stMessage = NewUtil.convertMessagePayloadToStMessage((MessagePayload) AllSocketEvents.gson.fromJson(args[0].toString(), MessagePayload.class), MessageStatus.RECEIVED);
                    if (!NewUtil.isUserExists(InMemoryCache.friendsConversationList, stMessage.getUserId()) && !NewUtil.isUserExists(InMemoryCache.blockedConversationList, stMessage.getUserId()) && (stUser = NewCustomOkHttp.addFriend(InMemoryCache.myself.getUserId(), stMessage.getUserId())) != null && !stUser.isBlocked()) {
                        StConversation stConversation = new StConversation(stUser, new ArrayList());
                        stConversation.setOnline(true);
                        InMemoryCache.friendsConversationList.add(0, stConversation);
                        Collections.sort(InMemoryCache.friendsConversationList, new ConversationComparator(SortOrder.DESCENDING));
                    }
                    if (!NewUtil.isUserBlocked(stMessage.getUserId())) {
                        NewUtil.addMsgInConversationAndSave(stMessage);
                        PendingMessageStatus pendingMessageStatus = new PendingMessageStatus();
                        pendingMessageStatus.setMessageId(stMessage.getMsgId());
                        pendingMessageStatus.setSenderId(InMemoryCache.myself.getUserId());
                        pendingMessageStatus.setReceiverId(stMessage.getUserId());
                        pendingMessageStatus.setStatus(MessageStatus.DELIVERED);
                        AllSocketEvents.triggerFriendMessageStatus(pendingMessageStatus);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public static void listenOnlineStatus() {
        SocketUtil.socket.on("friend_online_status", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass4 */

            public void call(Object... args) {
                try {
                    NewUtil.setOnlineStatus(args[0].toString(), Boolean.parseBoolean(args[1].toString()), true);
                } catch (Exception e) {
                }
            }
        });
    }

    public static void listenOnlineStatusFriendList() {
        SocketUtil.socket.on("my_friends_status", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass5 */

            public void call(Object... args) {
                try {
                    Map<String, String> mapIdToOnlineStatus = (Map) AllSocketEvents.gson.fromJson(args[0].toString(), new TypeToken<Map<String, String>>() {
                        /* class strangers.chat.socket.AllSocketEvents.AnonymousClass5.AnonymousClass1 */
                    }.getType());
                    if (mapIdToOnlineStatus != null && mapIdToOnlineStatus.size() > 0) {
                        for (Map.Entry<String, String> entry : mapIdToOnlineStatus.entrySet()) {
                            NewUtil.setOnlineStatus((String) entry.getKey(), Boolean.parseBoolean((String) entry.getValue()), false);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public static void listenFriendTypingStart() {
        SocketUtil.socket.on("receive_friend_typing_start", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass6 */

            public void call(Object... args) {
                try {
                    NewUtil.updateTypingStatusInConversation(args[0].toString(), true);
                } catch (Exception e) {
                }
            }
        });
    }

    public static void listenFriendTypingEnd() {
        SocketUtil.socket.on("receive_friend_typing_end", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass7 */

            public void call(Object... args) {
                try {
                    NewUtil.updateTypingStatusInConversation(args[0].toString(), false);
                } catch (Exception e) {
                }
            }
        });
    }

    public static void listenFriendMsgStatus() {
        SocketUtil.socket.on("receive_friend_msg_status", new Emitter.Listener() {
            /* class strangers.chat.socket.AllSocketEvents.AnonymousClass8 */

            public void call(Object... args) {
                try {
                    NewUtil.updateMsgStatusInConversation((PendingMessageStatus) AllSocketEvents.gson.fromJson(args[0].toString(), PendingMessageStatus.class));
                } catch (Exception e) {
                }
            }
        });
    }

}