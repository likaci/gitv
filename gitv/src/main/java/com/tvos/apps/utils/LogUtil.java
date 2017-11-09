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

    public static void v(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.v(TAG, buildMessage(msg));
        }
    }

    public static void v(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.v(TAG, buildMessage(msg));
        }
    }

    public static void v(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.v(TAG, buildMessage(msg), thr);
        }
    }

    public static void d(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(TAG, buildMessage(msg));
        }
    }

    public static void d() {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(TAG, buildMessage(""));
        }
    }

    public static void d(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.d(TAG, buildMessage(msg));
        }
    }

    public static void d(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(TAG, buildMessage(msg), thr);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.i(TAG, buildMessage(msg));
        }
    }

    public static void i(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.i(TAG, buildMessage(msg), thr);
        }
    }

    public static void i(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.i(TAG, buildMessage(msg));
        }
    }

    public static void e(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.e(TAG, buildMessage(msg));
        }
    }

    public static void e(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.e(TAG, buildMessage(msg));
        }
    }

    public static void e(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.e(TAG, buildMessage(msg), thr);
        }
    }

    public static void w(String msg) {
        if (isDebug && !CLOSEALLLOG) {
            Log.w(TAG, buildMessage(msg));
        }
    }

    public static void w(String msg, boolean isForce) {
        if (!CLOSEALLLOG) {
            Log.w(TAG, buildMessage(msg));
        }
    }

    public static void w(String msg, Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.w(TAG, buildMessage(msg), thr);
        }
    }

    public static void w(Throwable thr) {
        if (isDebug && !CLOSEALLLOG) {
            Log.w(TAG, buildMessage(""), thr);
        }
    }

    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return caller.getClassName() + "." + caller.getMethodName() + "(): " + msg;
    }
}
