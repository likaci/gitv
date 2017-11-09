package com.xcrash.crashreporter.utils;

import android.text.TextUtils;
import android.util.Log;

public class DebugLog {
    private static final String TAG = "Xcrash";
    private static boolean enable = false;
    public static final CircularLogBuffer logBuffer = new CircularLogBuffer();
    public static final CircularLogBuffer viewTraceBuffer = new CircularLogBuffer(64);

    public static void setLogSize(int logSize) {
        logBuffer.logSize = logSize;
    }

    public static void enable() {
        enable = true;
    }

    public static boolean isDebug() {
        return enable;
    }

    public static void enableLogBuffer(boolean enable) {
        logBuffer.enabled = enable;
    }

    public static void m1740d(String tag, Object... msg) {
        if (msg != null && !TextUtils.isEmpty(tag)) {
            String logStr = concateString(msg);
            logBuffer.log(TAG, "D", logStr);
            if (enable) {
                Log.d(tag, logStr);
            }
        }
    }

    public static void m1742i(String tag, Object... msg) {
        if (msg != null && !TextUtils.isEmpty(tag) && enable) {
            Log.i(tag, concateString(msg));
        }
    }

    public static void log(String tag, Object... msg) {
        if (msg != null && !TextUtils.isEmpty(tag) && enable) {
            Log.i(tag, concateString(msg));
        }
    }

    public static void m1743v(String tag, Object... msg) {
        if (msg != null && !TextUtils.isEmpty(tag) && enable) {
            Log.v(tag, concateString(msg));
        }
    }

    public static void m1744w(String tag, Object... msg) {
        if (msg != null && !TextUtils.isEmpty(tag) && enable) {
            Log.w(tag, concateString(msg));
        }
    }

    public static void m1741e(String tag, Object... msg) {
        if (msg != null && !TextUtils.isEmpty(tag)) {
            String logStr = concateString(msg);
            logBuffer.log(TAG, "E", logStr);
            if (enable) {
                Log.e(tag, logStr);
            }
        }
    }

    private static String concateString(Object... msg) {
        StringBuilder sb = new StringBuilder(100);
        for (Object obj : msg) {
            if (obj != null) {
                sb.append(String.valueOf(obj));
            }
        }
        return sb.toString();
    }
}
