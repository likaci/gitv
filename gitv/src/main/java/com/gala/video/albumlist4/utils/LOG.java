package com.gala.video.albumlist4.utils;

import android.util.Log;
import org.cybergarage.soap.SOAP;

public class LOG {
    public static void d() {
        d("wanghelong", "");
    }

    public static void d(String msg) {
        d("wanghelong", msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, a() + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, a() + msg);
    }

    public static void e(String msg) {
        Log.e("wanghelong", a() + msg);
    }

    private static String a() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return "[ " + stackTrace[6].getFileName().replace(".java", "") + SOAP.DELIM + stackTrace[6].getMethodName() + "-" + stackTrace[5].getFileName().replace(".java", "") + SOAP.DELIM + stackTrace[5].getMethodName() + " ] ";
    }

    public static void backTrace() {
        Log.d("wanghelong", "***********************************");
        backTrace("wanghelong");
    }

    public static void backTrace(String tag) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 4; i < stackTrace.length; i++) {
            Log.e(tag, "        " + stackTrace[i].toString());
        }
    }
}
