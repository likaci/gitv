package com.push.pushservice.utils;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "Push";
    private static boolean mDebug = true;

    public static void setDebug(boolean isDebug) {
        mDebug = isDebug;
    }

    public static void logd(String log) {
        if (mDebug) {
            Log.d(TAG, log);
        }
    }

    public static void logi(String log) {
        if (mDebug) {
            Log.i(TAG, log);
        }
    }

    public static void loge(String log) {
        if (mDebug) {
            Log.e(TAG, log);
        }
    }

    public static void logd(String tag, String log) {
        if (mDebug) {
            Log.d(TAG, "[" + tag + "] " + log);
        }
    }

    public static void logi(String tag, String log) {
        if (mDebug) {
            Log.i(TAG, "[" + tag + "] " + log);
        }
    }

    public static void loge(String tag, String log) {
        if (mDebug) {
            Log.e(TAG, "[" + tag + "] " + log);
        }
    }
}
