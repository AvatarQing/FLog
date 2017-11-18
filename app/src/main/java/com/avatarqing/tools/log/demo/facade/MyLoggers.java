package com.avatarqing.tools.log.demo.facade;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.avatarqing.tools.log.ILogger;
import com.avatarqing.tools.log.demo.BuildConfig;
import com.avatarqing.tools.log.demo.R;
import com.avatarqing.tools.log.demo.db.LogDbHelper;
import com.avatarqing.tools.log.demo.function.Optional;
import com.avatarqing.tools.log.demo.objectparser.ObjectParsers;
import com.avatarqing.tools.log.loggers.Loggers;
import com.avatarqing.tools.log.loggers.consumer.ConsumeHandler;
import com.avatarqing.tools.log.loggers.consumer.Consumers;
import com.avatarqing.tools.log.loggers.formatter.log.LogFormatter;
import com.avatarqing.tools.log.loggers.formatter.multipletrace.MultipleTraceFormatter;
import com.avatarqing.tools.log.loggers.formatter.trace.TraceFormatter;
import com.avatarqing.tools.log.loggers.runnable.Runnables;
import com.avatarqing.tools.log.loggers.supplier.IStringSupplier;
import com.avatarqing.tools.log.loggers.supplier.StringSuppliers;

import java.io.File;

/**
 * @author AvatarQing
 */
public final class MyLoggers {

    /**
     * FLog-test: shit >>> (MainActivity.java:43) -> logDifferentLevel() | Thread:main
     * FLog-test: Your age is 28, name is The Batman >>> (MainActivity.java:53) -> logVariousParameters() | Thread:main
     * FLog-test: I am in another thread >>> (MainActivity.java:65) -> lambda$logInOtherThread$0$MainActivity() | Thread:Thread-2
     */
    public static ILogger simpleConsole(Context context) {
        String prefixTag = context.getString(R.string.app_name);
        return Optional.of(Loggers.logcat())
                .map(Loggers::ensureMaxLogLength)
                .map(Loggers::appendErrorStackTrace)
                .map(logger -> Loggers.prefixTag(logger, StringSuppliers.string(prefixTag + "-")))
                .map(logger -> Loggers.suffixMessage(
                        logger,
                        StringSuppliers.concatString(
                                StringSuppliers.string(" >>> "),
                                StringSuppliers.trace(
                                        TraceFormatter.concat(
                                                TraceFormatter.lineNumber(),
                                                TraceFormatter.string(" -> "),
                                                TraceFormatter.methodName()
                                        ),
                                        0
                                ),
                                StringSuppliers.string(" | "),
                                StringSuppliers.threadName()
                        )
                        )
                )
                .map(logger -> Loggers.parseMessage(
                        logger,
                        ObjectParsers.intent(),
                        ObjectParsers.collection(),
                        ObjectParsers.map()
                ))
                .get();
    }

    /**
     * Pretty-test: ┌───────────────────────────────────────
     * Pretty-test: │ Thread:main
     * Pretty-test: ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
     * Pretty-test: │ MainActivity.testLog()  (MainActivity.java:30)
     * Pretty-test: │     MainActivity.logExamples()  (MainActivity.java:36)
     * Pretty-test: │         MainActivity.logVariousParameters()  (MainActivity.java:53)
     * Pretty-test: ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
     * Pretty-test: │ Your age is 28, name is The Batman
     * Pretty-test: └───────────────────────────────────────
     */
    public static ILogger prettyConsole(String prefixTag) {
        final char TOP_LEFT_CORNER = '┌';
        final char BOTTOM_LEFT_CORNER = '└';
        final char MIDDLE_CORNER = '├';
        final String HORIZONTAL_LINE = "│";
        final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
        final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
        final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
        final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
        final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;
        return Optional.of(Loggers.logcat())
                .map(logger -> {
                    ILogger middleBorderLogger = Loggers.string(logger, StringSuppliers.string(MIDDLE_BORDER));
                    IStringSupplier horizontalLineSupplier = StringSuppliers.string(HORIZONTAL_LINE + " ");
                    return Loggers.sequence(
                            Loggers.string(logger, StringSuppliers.string(TOP_BORDER)),
                            Loggers.string(logger, StringSuppliers.concatString(
                                    horizontalLineSupplier,
                                    StringSuppliers.threadName())
                            ),
                            middleBorderLogger,
                            Optional.of(logger)
                                    .map(l -> Loggers.prefixMessage(l, horizontalLineSupplier))
                                    .map(Loggers::splitLine)
                                    .map(l -> Loggers.string(l, StringSuppliers.multipleTrace(
                                            MultipleTraceFormatter.splitToEchelonStack(
                                                    TraceFormatter.concat(
                                                            TraceFormatter.simpleClassName(),
                                                            TraceFormatter.string("."),
                                                            TraceFormatter.methodName(),
                                                            TraceFormatter.string("  "),
                                                            TraceFormatter.lineNumber()
                                                    )
                                            ),
                                            0,
                                            3
                                            ))
                                    )
                                    .get(),
                            middleBorderLogger,
                            Optional.of(logger)
                                    .map(l -> Loggers.prefixMessage(l, horizontalLineSupplier))
                                    .map(Loggers::splitLine)
                                    .map(l -> Loggers.parseMessage(l,
                                            ObjectParsers.intent(),
                                            ObjectParsers.collection(),
                                            ObjectParsers.map()
                                    ))
                                    .get(),
                            Loggers.string(logger, StringSuppliers.string(BOTTOM_BORDER))
                    );
                })
                .map(logger -> Loggers.prefixTag(logger, StringSuppliers.string(prefixTag + "-")))
                .get();
    }

    /**
     * 2017-11-18 08:33:34.417/15201-1/2/test: Your age is 28, name is The Batman
     * 2017-11-18 08:33:34.417/15201-1/3/test: Hello World
     * 2017-11-18 08:33:34.417/15201-1/7/test: Something bad happened
     */
    public static ILogger saveToFile(Looper looper) {
        return Optional.of("log.txt")
                .map(fileName -> new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID + File.separator + fileName))
                .map(logFile -> Loggers.formatThenConsume(
                        LogFormatter.logcatStyleLineString(),
                        Consumers.delegateToHandler(new ConsumeHandler<>(looper, Consumers.appendToFile(logFile)))
                ))
                .map(logger -> Loggers.parseMessage(
                        logger,
                        ObjectParsers.intent(),
                        ObjectParsers.collection(),
                        ObjectParsers.map()
                ))
                .map(logger -> Loggers.filterLevel(logger, level -> level >= Log.WARN))
                .get();
    }

    /**
     * id|timestamp     |priority|tag  |trace                             |message
     * --|--------------|--------|-----|----------------------------------|------------
     * 1 |1511011940346 |5       |test |(MainActivity.java:44)->onCreate()|Hello World
     * 2 |1511011940354 |6       |test |(MainActivity.java:55)->doWork()  |Good Work
     * 3 |1511011940354 |7       |me   |(Utility.java:113)->doSomething() |Some error
     */
    public static ILogger saveToDb(Looper looper, Context context) {
        LogDbHelper logDbHelper = new LogDbHelper(context);
        return Optional.of(Consumers.insertToDb(logDbHelper, LogDbHelper.TABLE_NAME))
                .map(consumer -> Consumers.doAfterConsume(consumer, Runnables.execSQL(logDbHelper, LogDbHelper.getRollingSql(1000))))
                .map(consumer -> new ConsumeHandler<>(looper, consumer))
                .map(Consumers::delegateToHandler)
                .map(consumer -> Loggers.formatThenConsume(MyLogFormatter.toContentValues(), consumer))
                .map(logger -> Loggers.filterLevel(logger, level -> level >= Log.WARN))
                .get();
    }

}