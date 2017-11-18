package com.avatarqing.tools.log.loggers.formatter.log;

import android.os.Process;

import com.avatarqing.tools.log.loggers.utils.StackTraceUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author AvatarQing
 */
public class LogFormatter {

    public static ILogFormatter<String> logcatStyleLineString() {
        return (priority, tag, throwable, message) -> {
            int processId = Process.myPid();
            long threadId = Thread.currentThread().getId();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                    .format(System.currentTimeMillis());
            String finalMessage = String.valueOf(message);
            if (throwable != null) {
                finalMessage += "\n" + StackTraceUtils.getStackTraceString(throwable);
            }
            return String.format(Locale.getDefault(), "%s/%d-%d/%d/%s: %s", timestamp, processId, threadId, priority, tag, finalMessage);
        };
    }

}