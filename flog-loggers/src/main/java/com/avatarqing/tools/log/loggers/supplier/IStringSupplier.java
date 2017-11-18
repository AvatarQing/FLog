package com.avatarqing.tools.log.loggers.supplier;

import com.avatarqing.tools.log.loggers.function.Supplier;

/**
 * @author AvatarQing
 */
@FunctionalInterface
public interface IStringSupplier extends Supplier<String> {
    @Override
    String get();
}