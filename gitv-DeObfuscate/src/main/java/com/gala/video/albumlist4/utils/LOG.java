package com.gala.video.albumlist4.utils;

import android.util.Log;
import org.cybergarage.soap.SOAP;

public class LOG {
    public static void m1207d() {
        m1209d("wanghelong", "");
    }

    public static void m1208d(String msg) {
        m1209d("wanghelong", msg);
    }

    public static void m1209d(String tag, String msg) {
        Log.d(tag, m1206a() + msg);
    }

    public static void m1211e(String tag, String msg) {
        Log.e(tag, m1206a() + msg);
    }

    public static void m1210e(String msg) {
        Log.e("wanghelong", m1206a() + msg);
    }

    private static String m1206a() {
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
