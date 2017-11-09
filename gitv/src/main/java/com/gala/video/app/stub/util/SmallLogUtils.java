package com.gala.video.app.stub.util;

import android.util.Log;

public class SmallLogUtils {
    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        Log.i(tag, msg, throwable);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        Log.w(tag, msg, throwable);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }
}
