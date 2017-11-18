package com.avatarqing.tools.log.loggers.utils;

import com.avatarqing.tools.log.FLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * @author AvatarQing
 */
public final class StackTraceUtils {

    private static final int ENTRANCE_STACK_OFFSET = 2;
    private static final Class ENTRANCE_CLASS = FLog.class;

    private StackTraceUtils() {
    }

    public static StackTraceElement getInvokeTraceElement(int stackOffset) {
        final StackTraceElement[] traces = new Throwable().getStackTrace();
        for (int i = 0; i < traces.length; i++) {
            StackTraceElement frame = traces[i];
            if (frame.getClassName().equals(ENTRANCE_CLASS.getName())) {
                int destIndex = i + stackOffset + ENTRANCE_STACK_OFFSET;
                if (destIndex < traces.length) {
                    return traces[destIndex];
                } else {
                    break;
                }
            }
        }
        return null;
    }

    public static StackTraceElement[] getInvokeTraceElements(int stackOffset, int methodCount) {
        final StackTraceElement[] traces = new Throwable().getStackTrace();
        for (int i = 0; i < traces.length; i++) {
            StackTraceElement frame = traces[i];
            if (frame.getClassName().equals(ENTRANCE_CLASS.getName())) {
                int startIndex = i + stackOffset + ENTRANCE_STACK_OFFSET;
                if (startIndex < traces.length) {
                    int endIndex = startIndex + methodCount;
                    if (endIndex >= traces.length) {
                        endIndex = traces.length - 1;
                    }
                    return Arrays.copyOfRange(traces, startIndex - 1, endIndex);
                } else {
                    break;
                }
            }
        }
        return null;
    }

    public static String getStackTraceString(Throwable t) {
        if (t == null) {
            return "";
        }
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

}