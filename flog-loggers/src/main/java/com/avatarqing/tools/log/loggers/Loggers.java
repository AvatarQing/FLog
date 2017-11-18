package com.avatarqing.tools.log.loggers;

import android.os.Build;
import android.util.Log;

import com.avatarqing.tools.log.ILogger;
import com.avatarqing.tools.log.loggers.formatter.log.ILogFormatter;
import com.avatarqing.tools.log.loggers.function.Consumer;
import com.avatarqing.tools.log.loggers.function.IntPredicate;
import com.avatarqing.tools.log.loggers.parser.IObjectParser;
import com.avatarqing.tools.log.loggers.supplier.IStringSupplier;
import com.avatarqing.tools.log.loggers.utils.StackTraceUtils;

import java.util.Arrays;

/**
 * @author AvatarQing
 */
public final class Loggers {

    public static ILogger sequence(ILogger... loggers) {
        return (priority, tag, t, message) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Arrays.stream(loggers)
                        .forEach(logger -> logger.log(priority, tag, t, message));
            } else {
                for (ILogger logger : loggers) {
                    logger.log(priority, tag, t, message);
                }
            }
        };
    }

    public static ILogger filterLevel(ILogger logger, IntPredicate predicate) {
        return (priority, tag, t, message) -> {
            if (predicate.test(priority)) {
                logger.log(priority, tag, t, message);
            }
        };
    }

    public static ILogger splitLine(ILogger logger) {
        return (priority, tag, t, rawMessage) -> {
            String[] lines = String.valueOf(rawMessage).split(System.getProperty("line.separator"));
            for (String line : lines) {
                logger.log(priority, tag, t, line);
            }
        };
    }

    public static ILogger ensureMaxLogLength(ILogger logger) {
        return (priority, tag, t, rawMessage) -> {
            final int maxLogLength = 4000;
            String message = String.valueOf(rawMessage);
            if (message.length() < maxLogLength) {
                logger.log(priority, tag, t, message);
                return;
            }
            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + maxLogLength);
                    String part = message.substring(i, end);
                    logger.log(priority, tag, t, part);
                    i = end;
                } while (i < newline);
            }
        };
    }

    public static ILogger string(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> logger.log(priority, tag, t, supplier.get());
    }

    public static ILogger prefixTag(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> {
            tag = supplier.get() + tag;
            logger.log(priority, tag, t, message);
        };
    }

    public static ILogger suffixTag(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> {
            tag = tag + supplier.get();
            logger.log(priority, tag, t, message);
        };
    }

    public static ILogger prefixMessage(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> {
            message = supplier.get() + message;
            logger.log(priority, tag, t, message);
        };
    }

    public static ILogger suffixMessage(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> {
            message = message + supplier.get();
            logger.log(priority, tag, t, message);
        };
    }

    public static ILogger parseMessage(ILogger logger, IObjectParser... parsers) {
        return (priority, tag, t, message) -> {
            Object parsedMessage = message;
            if (!(message instanceof String)) {
                for (IObjectParser parser : parsers) {
                    if (parser.canParse(message)) {
                        parsedMessage = parser.castObjectThenParse(message);
                        break;
                    }
                }
            }
            logger.log(priority, tag, t, parsedMessage);
        };
    }

    public static ILogger appendErrorStackTrace(ILogger logger) {
        return (priority, tag, t, message) -> {
            String stackTraceText = StackTraceUtils.getStackTraceString(t);
            String parsedMessage = String.valueOf(message) + "\n" + stackTraceText;
            logger.log(priority, tag, t, parsedMessage);
        };
    }

    public static ILogger dummy() {
        return (priority, tag, t, message) -> {
        };
    }

    public static ILogger logcat() {
        return (priority, tag, t, message) -> {
            if (priority == Log.ASSERT) {
                Log.wtf(String.valueOf(tag), String.valueOf(message));
            } else {
                Log.println(priority, String.valueOf(tag), String.valueOf(message));
            }
        };
    }

    public static <T> ILogger formatThenConsume(ILogFormatter<T> formatter, Consumer<T> consumer) {
        return (priority, tag, t, message) -> {
            T formattedMessage = formatter.format(priority, tag, t, message);
            consumer.accept(formattedMessage);
        };
    }
}