package com.avatarqing.tools.log.demo.objectparser;

import com.avatarqing.tools.log.loggers.parser.IObjectParser;

import java.util.Map;
import java.util.Set;

/**
 * @author AvatarQing
 */
public class MapParser implements IObjectParser<Map> {

    private static final String INDENT = "    ";

    @Override
    public Class<Map> getClassType() {
        return Map.class;
    }

    @Override
    public Object parseObject(Map map) {
        StringBuilder msg = new StringBuilder(map.getClass().getName() + " [\n");
        Set keys = map.keySet();
        for (Object key : keys) {
            String itemString = INDENT + "%s -> %s\n";
            Object value = map.get(key);
            if (value != null) {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                } else if (value instanceof Character) {
                    value = "\'" + value + "\'";
                }
            }
            msg.append(String.format(itemString, key, value));
        }
        return msg + "]";
    }
}
