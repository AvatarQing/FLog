package com.avatarqing.tools.log.loggers.formatter.multipletrace;

import com.avatarqing.tools.log.loggers.function.Function;

/**
 * @author AvatarQing
 */
@FunctionalInterface
public interface IMultipleTraceFormatter extends Function<StackTraceElement[], String> {
    @Override
    String apply(StackTraceElement[] stackTraceElements);
}