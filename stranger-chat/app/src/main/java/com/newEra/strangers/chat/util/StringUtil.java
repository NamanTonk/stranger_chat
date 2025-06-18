package com.newEra.strangers.chat.util;

public class StringUtil {
    public static boolean isEmptyStr(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmptyStr(String str) {
        return !isEmptyStr(str);
    }
}