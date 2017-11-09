package com.gala.tv.voice.core;

public class Log {
    public static final boolean LOG = true;

    private Log() {
    }

    public static int m525d(String str, String str2) {
        return android.util.Log.d(str, str2);
    }

    public static int m526d(String str, String str2, Throwable th) {
        return android.util.Log.d(str, str2, th);
    }

    public static int m529w(String str, String str2) {
        return android.util.Log.w(str, str2);
    }

    public static int m530w(String str, String str2, Throwable th) {
        return android.util.Log.w(str, str2, th);
    }

    public static int m527e(String str, String str2) {
        return android.util.Log.e(str, str2);
    }

    public static int m528e(String str, String str2, Throwable th) {
        return android.util.Log.e(str, str2, th);
    }
}
