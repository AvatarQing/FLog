package com.avatarqing.tools.log.demo.objectparser;

import android.content.Intent;
import android.os.Bundle;

import com.avatarqing.tools.log.loggers.parser.IObjectParser;

import java.util.Collection;
import java.util.Map;

/**
 * @author AvatarQing
 */
public final class ObjectParsers {

    public static IObjectParser<Map> map() {
        return new MapParser();
    }

    public static IObjectParser<Intent> intent() {
        return new IntentParser();
    }

    public static IObjectParser<Bundle> bundle() {
        return new BundleParser();
    }

    public static IObjectParser<Collection> collection() {
        return new CollectionParser();
    }

}