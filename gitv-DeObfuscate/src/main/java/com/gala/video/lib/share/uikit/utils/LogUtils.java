package com.gala.video.lib.share.uikit.utils;

import android.os.Process;
import android.util.Log;

public class LogUtils {
    private static final int LEAST_LENGTH = 1;
    private static final String TAG = "LogUtils";
    private static ThreadLocal<Integer> TID_STR_LENGTH = new ThreadLocal();
    private static ThreadLocal<StringBuilder> logBuilder = new C18311();
    public static boolean mIsDebug = true;

    static class C18311 extends ThreadLocal<StringBuilder> {
        C18311() {
        }

        protected StringBuilder initialValue() {
            StringBuilder stringBuilder = new StringBuilder(250);
            stringBuilder.append("[TID ").append(Process.myTid()).append("] ");
            LogUtils.TID_STR_LENGTH.set(Integer.valueOf(stringBuilder.length()));
            return stringBuilder;
        }
    }

    public static void setDebug(boolean isDebug) {
        if (isDebug) {
            m1591i(TAG, "log is open");
        } else {
            m1591i(TAG, "log is close");
        }
        mIsDebug = isDebug;
    }

    public static void m1585d(String tag, Object log) {
        Log.d(tag, wrapLog(log));
    }

    public static void m1591i(String tag, Object log) {
        Log.i(tag, wrapLog(log));
    }

    public static void m1594w(String tag, Object log) {
        if (mIsDebug) {
            Log.w(tag, wrapLog(log));
        }
    }

    public static void m1588e(String tag, Object log) {
        if (mIsDebug) {
            Log.e(tag, wrapLog(log));
        }
    }

    public static void m1587d(Object... logs) {
        if (isLogValid(logs)) {
            m1585d(logs[0].toString(), packageLog(logs));
        }
    }

    private static boolean isLogValid(Object[] logs) {
        return logs != null && logs.length > 1;
    }

    public static void m1593i(Object... logs) {
        if (isLogValid(logs)) {
            m1591i(logs[0].toString(), packageLog(logs));
        }
    }

    public static void m1596w(Object... logs) {
        if (mIsDebug && isLogValid(logs)) {
            m1594w(logs[0].toString(), packageLog(logs));
        }
    }

    public static void m1590e(Object... logs) {
        if (mIsDebug && isLogValid(logs)) {
            m1588e(logs[0].toString(), packageLog(logs));
        }
    }

    public static void m1586d(String tag, Object object, Throwable t) {
        if (mIsDebug) {
            Log.d(tag, wrapLog(object.toString()), t);
        }
    }

    public static void m1592i(String tag, Object log, Throwable t) {
        Log.i(tag, wrapLog(log), t);
    }

    public static void m1595w(String tag, Object log, Throwable t) {
        if (mIsDebug) {
            Log.w(tag, wrapLog(log), t);
        }
    }

    public static void m1589e(String tag, Object log, Throwable t) {
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
