package com.gala.sdk.utils;

import com.gala.report.LogRecord;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class MyLogUtils {
    private MyLogUtils() {
    }

    public static void m462d(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(tag, msg);
        }
    }

    public static void m463d(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1569d(tag, msg, t);
        }
    }

    public static void m466i(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(tag, msg);
        }
    }

    public static void m467i(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1575i(tag, msg, t);
        }
    }

    public static void m468w(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(tag, msg);
        }
    }

    public static void m469w(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1578w(tag, msg, t);
        }
    }

    public static void m464e(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(tag, msg);
        }
    }

    public static void m465e(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1572e(tag, msg, t);
        }
    }

    public static void dd(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(tag, msg);
        }
        LogRecord.m392d(tag, msg);
    }

    public static void dd(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1569d(tag, msg, t);
        }
        LogRecord.m393d(tag, msg, t);
    }

    public static void ii(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(tag, msg);
        }
        LogRecord.m396i(tag, msg);
    }

    public static void ii(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1575i(tag, msg, t);
        }
        LogRecord.m397i(tag, msg, t);
    }

    public static void ww(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(tag, msg);
        }
        LogRecord.m400w(tag, msg);
    }

    public static void ww(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1578w(tag, msg, t);
        }
        LogRecord.m401w(tag, msg, t);
    }

    public static void ee(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(tag, msg);
        }
        LogRecord.m394e(tag, msg);
    }

    public static void ee(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1572e(tag, msg, t);
        }
        LogRecord.m395e(tag, msg, t);
    }
}
