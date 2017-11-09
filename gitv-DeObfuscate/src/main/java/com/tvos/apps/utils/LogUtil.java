package com.tvos.apps.utils;

import android.util.Log;

public class LogUtil {
    private static boolean CLOSEALLLOG = false;
    private static String TAG = "LogUtil";
    private static boolean isDebug = true;

    private LogUtil() {
    }

    public static void enableDebug(boolean enable) {
        isDebug = enable;
    }

    public static void closeAllLog() {
        CLOSEALLLOG = true;
    }

    public static void setTag(String Tag) {
        TAG = Tag;
    }

    public static void m1730v(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.v(TAG, buildMessage(msg));
        }
    }

    public static void m1732v(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.v(TAG, buildMessage(msg));
        }
    }

    public static void m1731v(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.v(TAG, buildMessage(msg), thr);
        }
    }

    public static void m1720d(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(TAG, buildMessage(msg));
        }
    }

    public static void m1719d() {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(TAG, buildMessage(""));
        }
    }

    public static void m1723d(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.d(TAG, buildMessage(msg));
        }
    }

    public static void m1722d(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(TAG, buildMessage(msg), thr);
        }
    }

    public static void m1721d(String tag, String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(tag, msg);
        }
    }

    public static void m1727i(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.i(TAG, buildMessage(msg));
        }
    }

    public static void m1728i(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.i(TAG, buildMessage(msg), thr);
        }
    }

    public static void m1729i(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.i(TAG, buildMessage(msg));
        }
    }

    public static void m1724e(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.e(TAG, buildMessage(msg));
        }
    }

    public static void m1726e(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.e(TAG, buildMessage(msg));
        }
    }

    public static void m1725e(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.e(TAG, buildMessage(msg), thr);
        }
    }

    public static void m1733w(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.w(TAG, buildMessage(msg));
        }
    }

    public static void m1735w(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.w(TAG, buildMessage(msg));
        }
    }

    public static void m1734w(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.w(TAG, buildMessage(msg), thr);
        }
    }

    public static void m1736w(Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.w(TAG, buildMessage(""), thr);
        }
    }

    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return caller.getClassName() + "." + caller.getMethodName() + "(): " + msg;
    }
}
