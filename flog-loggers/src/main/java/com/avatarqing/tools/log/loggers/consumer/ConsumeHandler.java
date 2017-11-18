package com.avatarqing.tools.log.loggers.consumer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.avatarqing.tools.log.loggers.function.Consumer;

/**
 * @author AvatarQing
 */
public class ConsumeHandler<T> extends Handler {

    private Consumer<T> consumer;

    public ConsumeHandler(Looper looper, Consumer<T> consumer) {
        super(looper);
        this.consumer = consumer;
    }

    @Override
    public void handleMessage(Message msg) {
        consumer.accept((T) msg.obj);
    }

}