package com.tvos.apps.utils;

import android.util.Log;

public class LogUtils {
    private static boolean isDebug = true;

    public static void enableDebug(boolean enable) {
        isDebug = enable;
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
}
