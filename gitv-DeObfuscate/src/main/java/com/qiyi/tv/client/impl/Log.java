package com.qiyi.tv.client.impl;

public class Log {
    public static final boolean LOG = true;

    private Log() {
    }

    public static int m1620d(String tag, String msg) {
        return android.util.Log.d(tag, msg);
    }

    public static int m1621d(String tag, String msg, Throwable tr) {
        return android.util.Log.d(tag, msg, tr);
    }

    public static int m1624w(String tag, String msg) {
        return android.util.Log.w(tag, msg);
    }

    public static int m1625w(String tag, String msg, Throwable tr) {
        return android.util.Log.w(tag, msg, tr);
    }

    public static int m1622e(String tag, String msg) {
        return android.util.Log.e(tag, msg);
    }

    public static int m1623e(String tag, String msg, Throwable tr) {
        return android.util.Log.e(tag, msg, tr);
    }
}
