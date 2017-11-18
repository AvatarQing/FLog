package com.avatarqing.tools.log.loggers.formatter.log;

/**
 * @author AvatarQing
 */
@FunctionalInterface
public interface ILogFormatter<T> {
    T format(int priority, String tag, Throwable t, Object message);
}