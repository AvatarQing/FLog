package com.avatarqing.tools.log.demo.objectparser;

import android.os.Bundle;

import com.avatarqing.tools.log.loggers.parser.IObjectParser;

/**
 * @author AvatarQing
 */
public class BundleParser implements IObjectParser<Bundle> {

    private static final String INDENT = "    ";

    @Override
    public Class<Bundle> getClassType() {
        return Bundle.class;
    }

    @Override
    public Object parseObject(Bundle bundle) {
        StringBuilder builder = new StringBuilder(bundle.getClass().getName() + " [\n");
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            builder.append(String.format(INDENT + "'%s' => %s \n", key, value));
        }
        builder.append("]");
        return builder.toString();
    }

}