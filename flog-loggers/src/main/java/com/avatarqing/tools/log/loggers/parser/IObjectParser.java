package com.avatarqing.tools.log.loggers.parser;

/**
 * @author AvatarQing
 */
public interface IObjectParser<T> {

    Class<T> getClassType();

    Object parseObject(T object);

    default boolean canParse(Object object) {
        return object != null && getClassType().isAssignableFrom(object.getClass());
    }

    default Object castObjectThenParse(Object object) {
        return parseObject((T) object);
    }

}