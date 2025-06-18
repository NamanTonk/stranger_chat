package com.newEra.strangers.chat.util;

public class StaticControll {
    private static StaticControll staticControll;

    public static StaticControll getInstance() {
        if (staticControll == null)
            staticControll = new StaticControll();
        return staticControll;
    }
    private int currentMonth;

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentDate) {
        this.currentMonth = currentDate;
    }
}
