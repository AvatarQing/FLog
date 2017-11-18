package com.avatarqing.tools.log.loggers.supplier;

import android.os.Build;

import com.avatarqing.tools.log.loggers.formatter.multipletrace.IMultipleTraceFormatter;
import com.avatarqing.tools.log.loggers.formatter.trace.ITraceFormatter;
import com.avatarqing.tools.log.loggers.utils.StackTraceUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author AvatarQing
 */
public final class StringSuppliers {

    public static IStringSupplier concatString(IStringSupplier... factories) {
        return () -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Arrays.stream(factories)
                        .map(IStringSupplier::get)
                        .collect(Collectors.joining());
            } else {
                StringBuilder result = new StringBuilder();
                for (IStringSupplier factory : factories) {
                    result.append(factory.get());
                }
                return result.toString();
            }
        };
    }

    public static IStringSupplier string(String givenString) {
        return () -> givenString;
    }

    public static IStringSupplier threadName() {
        return () -> String.format("Thread:%s", Thread.currentThread().getName());
    }

    public static IStringSupplier trace(ITraceFormatter formatter, int stackOffset) {
        return () -> {
            StackTraceElement frame = StackTraceUtils.getInvokeTraceElement(stackOffset);
            return frame != null ? formatter.apply(frame) : "";
        };
    }

    public static IStringSupplier multipleTrace(IMultipleTraceFormatter formatter, int stackOffset, int methodCount) {
        return () -> {
            StackTraceElement[] traces = StackTraceUtils.getInvokeTraceElements(stackOffset, methodCount);
            return traces != null ? formatter.apply(traces) : "";
        };
    }

}