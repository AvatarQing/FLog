package com.avatarqing.tools.log.loggers.consumer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import com.avatarqing.tools.log.loggers.function.Consumer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author AvatarQing
 */
public class Consumers {

    public static Consumer<String> appendToFile(File logFile) {
        return string -> {
            //noinspection ResultOfMethodCallIgnored
            logFile.getParentFile().mkdirs();
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                writer.println(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static Consumer<ContentValues> insertToDb(SQLiteOpenHelper helper, String tableName) {
        return contentValues -> helper.getWritableDatabase().insert(tableName, null, contentValues);
    }

    public static <T> Consumer<T> doAfterConsume(Consumer<T> consumer, Runnable runnable) {
        return data -> {
            consumer.accept(data);
            runnable.run();
        };
    }

    public static <T> Consumer<T> delegateToHandler(ConsumeHandler<T> handler) {
        return data -> {
            Message message = handler.obtainMessage(0, data);
            handler.sendMessage(message);
        };
    }

}