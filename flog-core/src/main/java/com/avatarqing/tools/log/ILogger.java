package com.avatarqing.tools.log;

/**
 * @author AvatarQing
 */
@FunctionalInterface
public interface ILogger {
    void log(int priority, String tag, Throwable t, Object message);
}
