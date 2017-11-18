package com.avatarqing.tools.log.demo;

import android.app.Activity;
import android.os.Bundle;

import com.avatarqing.tools.log.FLog;
import com.avatarqing.tools.log.demo.facade.MyLoggers;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author AvatarQing
 */
public class MainActivity extends Activity {

    private static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testLog();
    }

    private void testLog() {
        logExamples();

        FLog.init(MyLoggers.prettyConsole("Pretty"));
        logExamples();
    }

    private void logExamples() {
        logInOtherThread();
        logDifferentLevel();
        logVariousParameters();
        logObject();
    }

    private void logDifferentLevel() {
        FLog.v(TAG, "shit");
        FLog.d(TAG, "shit");
        FLog.i(TAG, "shit");
        FLog.w(TAG, "shit");
        FLog.e(TAG, "shit");
        FLog.wtf(TAG, "shit");
    }

    private void logVariousParameters() {
        FLog.v(TAG, null);
        FLog.v(null, "null tag");
        FLog.v(null, null);
        FLog.d(TAG, String.format(Locale.getDefault(), "Your age is %d, name is %s", 28, "The Batman"));
        FLog.w(TAG, new IllegalStateException("Custom Error"), "throw custom exception");
        FLog.w(TAG, new IllegalAccessException("You have no access right"), String.format(Locale.getDefault(), "User name is %s, age is %d", "Niko", 20));
        FLog.w(TAG, new IllegalStateException("Shit happened"));
    }

    private void logObject() {
        FLog.d(TAG, getIntent());
        FLog.w(TAG, Arrays.asList("Grand Theft Auto", "OverWatch", "Assassin's Creed"));
    }

    private void logInOtherThread() {
        new Thread(() -> FLog.d(TAG, "I am in another thread")).start();
    }

}