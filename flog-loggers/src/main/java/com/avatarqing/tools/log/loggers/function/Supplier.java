package com.avatarqing.tools.log.loggers.function;

/**
 * @author AvatarQing
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}