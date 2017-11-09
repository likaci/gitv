package com.gala.sdk.utils;

import com.gala.report.LogRecord;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class MyLogUtils {
    private MyLogUtils() {
    }

    public static void d(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(tag, msg, t);
        }
    }

    public static void i(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(tag, msg, t);
        }
    }

    public static void w(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(tag, msg, t);
        }
    }

    public static void e(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(tag, msg, t);
        }
    }

    public static void dd(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(tag, msg);
        }
        LogRecord.d(tag, msg);
    }

    public static void dd(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(tag, msg, t);
        }
        LogRecord.d(tag, msg, t);
    }

    public static void ii(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(tag, msg);
        }
        LogRecord.i(tag, msg);
    }

    public static void ii(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(tag, msg, t);
        }
        LogRecord.i(tag, msg, t);
    }

    public static void ww(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(tag, msg);
        }
        LogRecord.w(tag, msg);
    }

    public static void ww(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(tag, msg, t);
        }
        LogRecord.w(tag, msg, t);
    }

    public static void ee(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(tag, msg);
        }
        LogRecord.e(tag, msg);
    }

    public static void ee(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(tag, msg, t);
        }
        LogRecord.e(tag, msg, t);
    }
}
