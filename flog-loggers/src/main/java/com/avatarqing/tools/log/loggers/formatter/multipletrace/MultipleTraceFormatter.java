package com.avatarqing.tools.log.loggers.formatter.multipletrace;

import com.avatarqing.tools.log.loggers.formatter.trace.ITraceFormatter;

/**
 * @author AvatarQing
 */
public final class MultipleTraceFormatter {

    public static IMultipleTraceFormatter splitToEchelonStack(ITraceFormatter singleFormatter) {
        return traces -> {
            StringBuilder builder = new StringBuilder();
            String indent = "";
            for (int i = traces.length - 1; i > 0; i--) {
                builder
                        .append(indent)
                        .append(singleFormatter.apply(traces[i]))
                        .append("\n");
                indent += "    ";
            }
            return builder.toString();
        };
    }

}