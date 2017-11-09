package com.gala.sdk.plugin;

public class Log {
    public static boolean DEBUG = true;
    public static boolean VERBOSE = true;

    public static void m434v(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void m435v(String tag, String msg, Throwable throwable) {
        android.util.Log.w(tag, msg, throwable);
    }

    public static void m430d(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void m431d(String tag, String msg, Throwable throwable) {
        android.util.Log.w(tag, msg, throwable);
    }

    public static void m436w(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void m437w(String tag, String msg, Throwable throwable) {
        android.util.Log.w(tag, msg, throwable);
    }

    public static void m432e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void m433e(String tag, String msg, Throwable throwable) {
        android.util.Log.e(tag, msg, throwable);
    }
}
