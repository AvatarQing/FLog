package com.avatarqing.tools.log.demo;

import android.app.Application;
import android.os.Environment;
import android.os.HandlerThread;

import com.avatarqing.tools.log.FLog;
import com.avatarqing.tools.log.demo.facade.MyLoggers;
import com.avatarqing.tools.log.demo.function.Optional;
import com.avatarqing.tools.log.ILogger;
import com.avatarqing.tools.log.loggers.Loggers;

import java.io.File;

/**
 * @author AvatarQing
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        HandlerThread handlerThread = new HandlerThread("LogToFile");
        handlerThread.start();

        ILogger simpleLogger = MyLoggers.simpleConsole(this);
        ILogger handlerFileLogger = MyLoggers.saveToFile(handlerThread.getLooper());
        ILogger handlerDbLogger = MyLoggers.saveToDb(handlerThread.getLooper(), this);

        ILogger finalLogger = Loggers.sequence(simpleLogger, handlerFileLogger, handlerDbLogger);
        FLog.init(finalLogger);
    }

}