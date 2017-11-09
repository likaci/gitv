package com.gala.sdk.plugin;

public class Log {
    public static boolean DEBUG = true;
    public static boolean VERBOSE = true;

    public static void v(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        android.util.Log.w(tag, msg, throwable);
    }

    public static void d(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        android.util.Log.w(tag, msg, throwable);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        android.util.Log.w(tag, msg, throwable);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        android.util.Log.e(tag, msg, throwable);
    }
}
