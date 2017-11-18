package com.avatarqing.tools.log.loggers.formatter.trace;

import com.avatarqing.tools.log.loggers.function.Function;

/**
 * @author AvatarQing
 */
@FunctionalInterface
public interface ITraceFormatter extends Function<StackTraceElement, String> {
    @Override
    String apply(StackTraceElement frame);
}