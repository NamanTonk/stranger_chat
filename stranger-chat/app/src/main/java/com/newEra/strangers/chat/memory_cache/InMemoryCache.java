package com.newEra.strangers.chat.memory_cache;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

import com.newEra.strangers.chat.model.GetAccountResponse;
import com.newEra.strangers.chat.model.Message;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.model.StUser;
import com.newEra.strangers.chat.model.VersionCheckResponse;
import com.newEra.strangers.chat.model.ad.AdsResponse;

public class InMemoryCache {
    public static List<Message> CURRENT_RANDOM_CHAT_MESSAGES = new ArrayList();
    public static String PERSONAL_ROOM_ID = null;
    public static String RANDOM_CHAT_OTHER_PERSON_ID = null;
    public static String RANDOM_CHAT_ROOM_ID = null;
    public static List<StConversation> blockedConversationList = new ArrayList();
    public static Context context = null;
    public static List<StConversation> friendsConversationList = new ArrayList();
    public static StUser myself;
    public static boolean personalRoomAllotted = false;
    public static int VERSION_CODE = 0;
    public static GetAccountResponse accountResponse;
    public static VersionCheckResponse versionCheckResponse;

}
