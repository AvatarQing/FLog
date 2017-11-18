package com.avatarqing.tools.log;

import android.util.Log;

/**
 * @author AvatarQing
 */
public final class FLog {

    private static ILogger logger;

    private FLog() {
    }

    public static void init(ILogger logger) {
        FLog.logger = logger;
    }

    public static void v(String tag, Object message) {
        log(Log.VERBOSE, tag, null, message);
    }

    public static void d(String tag, Object message) {
        log(Log.DEBUG, tag, null, message);
    }

    public static void i(String tag, Object message) {
        log(Log.INFO, tag, null, message);
    }

    public static void w(String tag, Object message) {
        log(Log.WARN, tag, null, message);
    }

    public static void w(String tag, Throwable throwable, Object message) {
        log(Log.WARN, tag, throwable, message);
    }

    public static void w(String tag, Throwable throwable) {
        log(Log.WARN, tag, throwable, null);
    }

    public static void e(String tag, Object message) {
        log(Log.ERROR, tag, null, message);
    }

    public static void e(String tag, Throwable throwable, Object message) {
        log(Log.ERROR, tag, throwable, message);
    }

    public static void e(String tag, Throwable throwable) {
        log(Log.ERROR, tag, throwable, null);
    }

    public static void wtf(String tag, Object message) {
        log(Log.ASSERT, tag, null, message);
    }

    public static void wtf(String tag, Throwable throwable, Object message) {
        log(Log.ASSERT, tag, throwable, message);
    }

    public static void wtf(String tag, Throwable throwable) {
        log(Log.ASSERT, tag, throwable, null);
    }

    private static void log(int priority, String tag, Throwable t, Object message) {
        if (logger != null) {
            logger.log(priority, tag, t, message);
        }
    }
}