package com.gala.video.lib.framework.core.utils;

import android.os.Process;
import android.util.Log;

public class LogUtils {
    private static final int LEAST_LENGTH = 1;
    private static final String TAG = "LogUtils";
    private static ThreadLocal<Integer> TID_STR_LENGTH = new ThreadLocal();
    private static ThreadLocal<StringBuilder> logBuilder = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() {
            StringBuilder stringBuilder = new StringBuilder(250);
            stringBuilder.append("[TID ").append(Process.myTid()).append("] ");
            LogUtils.TID_STR_LENGTH.set(Integer.valueOf(stringBuilder.length()));
            return stringBuilder;
        }
    };
    public static boolean mIsDebug = true;

    public static void setDebug(boolean isDebug) {
        if (isDebug) {
            i(TAG, "log is open");
        } else {
            i(TAG, "log is close");
        }
        mIsDebug = isDebug;
    }

    public static void d(String tag, Object log) {
        Log.d(tag, wrapLog(log));
    }

    public static void i(String tag, Object log) {
        Log.i(tag, wrapLog(log));
    }

    public static void w(String tag, Object log) {
        if (mIsDebug) {
            Log.w(tag, wrapLog(log));
        }
    }

    public static void e(String tag, Object log) {
        if (mIsDebug) {
            Log.e(tag, wrapLog(log));
        }
    }

    public static void d(Object... logs) {
        if (isLogValid(logs)) {
            d(logs[0].toString(), packageLog(logs));
        }
    }

    private static boolean isLogValid(Object[] logs) {
        return logs != null && logs.length > 1;
    }

    public static void i(Object... logs) {
        if (isLogValid(logs)) {
            i(logs[0].toString(), packageLog(logs));
        }
    }

    public static void w(Object... logs) {
        if (mIsDebug && isLogValid(logs)) {
            w(logs[0].toString(), packageLog(logs));
        }
    }

    public static void e(Object... logs) {
        if (mIsDebug && isLogValid(logs)) {
            e(logs[0].toString(), packageLog(logs));
        }
    }

    public static void d(String tag, Object object, Throwable t) {
        if (mIsDebug) {
            Log.d(tag, wrapLog(object.toString()), t);
        }
    }

    public static void i(String tag, Object log, Throwable t) {
        Log.i(tag, wrapLog(log), t);
    }

    public static void w(String tag, Object log, Throwable t) {
        if (mIsDebug) {
            Log.w(tag, wrapLog(log), t);
        }
    }

    public static void e(String tag, Object log, Throwable t) {
        if (mIsDebug) {
            Log.e(tag, wrapLog(log), t);
        }
    }

    private static String packageLog(Object... logs) {
        StringBuilder builder = getThreadLocalBuilder();
        int length = logs.length;
        for (int i = 1; i < length; i++) {
            builder.append(logs[i]).append(" ");
        }
        return builder.toString();
    }

    private static StringBuilder getThreadLocalBuilder() {
        StringBuilder threadLocalBuilder = (StringBuilder) logBuilder.get();
        threadLocalBuilder.delete(((Integer) TID_STR_LENGTH.get()).intValue(), threadLocalBuilder.length());
        return threadLocalBuilder;
    }

    private static String wrapLog(Object log) {
        return getThreadLocalBuilder().append(log).toString();
    }
}
