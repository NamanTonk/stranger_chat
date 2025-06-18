package com.newEra.strangers.chat.comparators;

import java.util.Comparator;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.model.StConversation;

public class ConversationComparator implements Comparator<StConversation> {
    private SortOrder sortOrder;

    public ConversationComparator(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int compare(StConversation o1, StConversation o2) {
        int x;
        if (o1.getStMessageList().size() == 0 && o2.getStMessageList().size() == 0) {
            x = 0;
        } else if (o1.getStMessageList().size() == 0) {
            x = -1;
        } else if (o2.getStMessageList().size() == 0) {
            x = 1;
        } else if (((StMessage) o1.getStMessageList().get(o1.getStMessageList().size() - 1)).getMsgTime() > ((StMessage) o2.getStMessageList().get(o2.getStMessageList().size() - 1)).getMsgTime()) {
            x = 1;
        } else if (((StMessage) o1.getStMessageList().get(o1.getStMessageList().size() - 1)).getMsgTime() < ((StMessage) o2.getStMessageList().get(o2.getStMessageList().size() - 1)).getMsgTime()) {
            x = -1;
        } else {
            x = 0;
        }
        if (this.sortOrder.equals(SortOrder.DESCENDING)) {
            return x * -1;
        }
        return x;
    }
}
