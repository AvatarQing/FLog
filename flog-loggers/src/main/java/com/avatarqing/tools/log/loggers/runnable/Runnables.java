package com.avatarqing.tools.log.loggers.runnable;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author AvatarQing
 */
public class Runnables {

    public static Runnable execSQL(SQLiteOpenHelper helper, String sql) {
        return () -> helper.getWritableDatabase().execSQL(sql);
    }

}