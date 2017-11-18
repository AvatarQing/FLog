package com.avatarqing.tools.log.demo.objectparser;

import com.avatarqing.tools.log.loggers.parser.IObjectParser;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author AvatarQing
 */
public class CollectionParser implements IObjectParser<Collection> {

    private static final String INDENT = "    ";

    @Override
    public Class<Collection> getClassType() {
        return Collection.class;
    }

    @Override
    public Object parseObject(Collection collection) {
        String simpleName = collection.getClass().getName();
        StringBuilder msg = new StringBuilder(String.format("%s size = %s [\n", simpleName, collection.size()));
        if (!collection.isEmpty()) {
            Iterator iterator = collection.iterator();
            int flag = 0;
            while (iterator.hasNext()) {
                String itemString = INDENT + "[%s]:%s%s";
                Object item = iterator.next();
                msg.append(String.format(itemString, flag, item,
                        flag++ < collection.size() - 1 ? ",\n" : "\n"));
            }
        }
        return msg + "]";
    }

}