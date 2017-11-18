package com.avatarqing.tools.log.demo.objectparser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;

import com.avatarqing.tools.log.loggers.parser.IObjectParser;

import java.lang.reflect.Field;

/**
 * @author AvatarQing
 */
public class IntentParser implements IObjectParser<Intent> {

    private static final String INDENT = "    ";

    private static SparseArray<String> flagMap = new SparseArray<>();

    static {
        Class cla = Intent.class;
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().startsWith("FLAG_")) {
                int value = 0;
                try {
                    Object object = field.get(cla);
                    if (object instanceof Integer || "int".equals(object.getClass().getSimpleName())) {
                        value = (int) object;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flagMap.get(value) == null) {
                    flagMap.put(value, field.getName());
                }
            }
        }
    }

    private BundleParser bundleParser;

    public IntentParser() {
        this.bundleParser = new BundleParser();
    }

    @Override
    public Class<Intent> getClassType() {
        return Intent.class;
    }

    @Override
    public Object parseObject(Intent intent) {
        Bundle extras = intent.getExtras();
        return intent.getClass().getSimpleName() + " [\n" +
                String.format(INDENT + "%s = %s\n", "Scheme", intent.getScheme()) +
                String.format(INDENT + "%s = %s\n", "Action", intent.getAction()) +
                String.format(INDENT + "%s = %s\n", "DataString", intent.getDataString()) +
                String.format(INDENT + "%s = %s\n", "Type", intent.getType()) +
                String.format(INDENT + "%s = %s\n", "Package", intent.getPackage()) +
                String.format(INDENT + "%s = %s\n", "ComponentInfo", intent.getComponent()) +
                String.format(INDENT + "%s = %s\n", "Flags", getFlags(intent.getFlags())) +
                String.format(INDENT + "%s = %s\n", "Categories", intent.getCategories()) +
                String.format(INDENT + "%s = %s\n", "Extras", extras != null ? bundleParser.parseObject(extras) : "null") +
                "]";
    }

    private String getFlags(int flags) {
        String divider = "|";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < flagMap.size(); i++) {
            int flagKey = flagMap.keyAt(i);
            if ((flagKey & flags) == flagKey) {
                builder.append(flagMap.get(flagKey));
                builder.append(" ").append(divider).append(" ");
            }
        }
        if (TextUtils.isEmpty(builder.toString())) {
            builder.append(flags);
        } else if (builder.indexOf(divider) != -1) {
            builder.delete(builder.length() - 2, builder.length());
        }
        return builder.toString();
    }

}