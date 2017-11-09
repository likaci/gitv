package com.tvos.apps.utils;

import android.util.Log;

public class LogUtils {
    private static boolean isDebug = true;

    public static void enableDebug(boolean enable) {
        isDebug = enable;
    }

    public static void m1737d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void m1739i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void m1738e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
}
