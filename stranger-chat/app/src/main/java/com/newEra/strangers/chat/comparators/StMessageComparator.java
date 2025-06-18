package com.newEra.strangers.chat.comparators;

import java.util.Comparator;
import com.newEra.strangers.chat.database.model.StMessage;

public class StMessageComparator implements Comparator<StMessage> {
    private SortOrder sortOrder;

    public StMessageComparator(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int compare(StMessage o1, StMessage o2) {
        int x = 0;
        if (o1.getMsgTime() > o2.getMsgTime()) {
            x = 1;
        } else if (o1.getMsgTime() < o2.getMsgTime()) {
            x = -1;
        }
        if (this.sortOrder.equals(SortOrder.DESCENDING)) {
            return x * -1;
        }
        return x;
    }
}
