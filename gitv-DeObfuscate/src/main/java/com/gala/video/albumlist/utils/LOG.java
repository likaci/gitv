package com.gala.video.albumlist.utils;

import android.util.Log;
import org.cybergarage.soap.SOAP;

public class LOG {
    public static void m868d() {
        m870d("wanghelong", "");
    }

    public static void m869d(String msg) {
        m870d("wanghelong", msg);
    }

    public static void m870d(String tag, String msg) {
        Log.d(tag, m867a() + msg);
    }

    public static void m872e(String tag, String msg) {
        Log.e(tag, m867a() + msg);
    }

    public static void m871e(String msg) {
        Log.e("wanghelong", m867a() + msg);
    }

    private static String m867a() {
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
