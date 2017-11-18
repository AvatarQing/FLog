package com.avatarqing.tools.log.demo.facade;

import android.content.ContentValues;

import com.avatarqing.tools.log.loggers.formatter.log.ILogFormatter;
import com.avatarqing.tools.log.loggers.formatter.trace.TraceFormatter;
import com.avatarqing.tools.log.loggers.utils.StackTraceUtils;

import static com.avatarqing.tools.log.demo.db.LogDbHelper.COLUMN_MESSAGE;
import static com.avatarqing.tools.log.demo.db.LogDbHelper.COLUMN_PRIORITY;
import static com.avatarqing.tools.log.demo.db.LogDbHelper.COLUMN_TAG;
import static com.avatarqing.tools.log.demo.db.LogDbHelper.COLUMN_TIMESTAMP;
import static com.avatarqing.tools.log.demo.db.LogDbHelper.COLUMN_TRACE;

/**
 * @author AvatarQing
 */
public class MyLogFormatter {

    public static ILogFormatter<ContentValues> toContentValues() {
        return (priority, tag, t, message) -> {
            long timestamp = System.currentTimeMillis();
            String content = t == null ? String.valueOf(message) : message + " -> " + t.getMessage();
            StackTraceElement traceElement = StackTraceUtils.getInvokeTraceElement(0);
            String trace = traceElement == null ? "" :
                    TraceFormatter.concat(
                            TraceFormatter.lineNumber(),
                            TraceFormatter.string(" -> "),
                            TraceFormatter.methodName()
                    ).apply(traceElement);

            ContentValues values = new ContentValues();
            values.put(COLUMN_TIMESTAMP, timestamp);
            values.put(COLUMN_PRIORITY, priority);
            values.put(COLUMN_TAG, tag);
            values.put(COLUMN_TRACE, trace);
            values.put(COLUMN_MESSAGE, content);
            return values;
        };
    }

}