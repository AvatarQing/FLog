package com.avatarqing.tools.log.loggers.formatter.trace;

import android.os.Build;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author AvatarQing
 */
public final class TraceFormatter {

    public static ITraceFormatter concat(ITraceFormatter... formatter) {
        return frame -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Arrays.stream(formatter)
                        .map(f -> f.apply(frame))
                        .collect(Collectors.joining());
            } else {
                StringBuilder result = new StringBuilder();
                for (ITraceFormatter f : formatter) {
                    result.append(f.apply(frame));
                }
                return result.toString();
            }
        };
    }

    public static ITraceFormatter string(String givenString) {
        return frame -> givenString;
    }

    public static ITraceFormatter lineNumber() {
        return frame -> String.format("(%s:%s)", frame.getFileName(), frame.getLineNumber());
    }

    public static ITraceFormatter methodName() {
        return frame -> String.format("%s()", frame.getMethodName());
    }

    public static ITraceFormatter simpleClassName() {
        return frame -> {
            String className = frame.getClassName();
            int dotIndex = className.lastIndexOf(".");
            if (dotIndex >= 0) {
                return className.substring(dotIndex + 1);
            } else {
                return className;
            }
        };
    }

}
