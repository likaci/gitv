package com.gala.video.app.stub.util;

import android.util.Log;

public class SmallLogUtils {
    public static void m1540i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void m1541i(String tag, String msg, Throwable throwable) {
        Log.i(tag, msg, throwable);
    }

    public static void m1542w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void m1543w(String tag, String msg, Throwable throwable) {
        Log.w(tag, msg, throwable);
    }

    public static void m1538e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void m1539e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }
}
