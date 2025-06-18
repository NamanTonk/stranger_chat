package com.newEra.strangers.chat.database;

import android.content.Context;
import com.newEra.strangers.chat.database.impl.DbHandler;

public class DbHandlerInstance {
    public static DbHandler dbHandler = null;

    public static void initializeDbHandler(Context context) {
        dbHandler = new DbHandler(context);
    }
}
