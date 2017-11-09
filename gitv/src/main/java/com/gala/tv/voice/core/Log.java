package com.gala.tv.voice.core;

public class Log {
    public static final boolean LOG = true;

    private Log() {
    }

    public static int d(String str, String str2) {
        return android.util.Log.d(str, str2);
    }

    public static int d(String str, String str2, Throwable th) {
        return android.util.Log.d(str, str2, th);
    }

    public static int w(String str, String str2) {
        return android.util.Log.w(str, str2);
    }

    public static int w(String str, String str2, Throwable th) {
        return android.util.Log.w(str, str2, th);
    }

    public static int e(String str, String str2) {
        return android.util.Log.e(str, str2);
    }

    public static int e(String str, String str2, Throwable th) {
        return android.util.Log.e(str, str2, th);
    }
}
