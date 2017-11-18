package com.avatarqing.tools.log.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author AvatarQing
 */
public class LogDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "log";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_TRACE = "trace";
    public static final String COLUMN_MESSAGE = "message";

    private static final String DB_NAME = "log.db";
    private static final int DB_VERSION = 1;

    public LogDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + COLUMN_PRIORITY + " INTEGER,"
                + COLUMN_TAG + " TEXT,"
                + COLUMN_TRACE + " TEXT,"
                + COLUMN_MESSAGE + " TEXT"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static String getRollingSql(int maxRecordCount) {
        String countSql = "SELECT COUNT(" + COLUMN_ID + ")" + " FROM " + TABLE_NAME;
        String selectBadRecordSql = "SELECT " + COLUMN_ID
                + " FROM " + TABLE_NAME
                + " ORDER BY " + COLUMN_ID + " DESC"
                + " LIMIT (" + countSql + ")"
                + " OFFSET " + maxRecordCount;
        return "DELETE FROM " + TABLE_NAME
                + " WHERE (" + countSql + ") > " + maxRecordCount
                + " AND " + COLUMN_ID + " IN (" + selectBadRecordSql + ")";
    }

}