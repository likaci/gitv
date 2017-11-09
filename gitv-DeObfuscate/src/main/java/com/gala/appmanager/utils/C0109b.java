package com.gala.appmanager.utils;

import android.os.Process;
import android.util.Log;

public class C0109b {
    public static boolean f370a = true;

    public static void m236a(String str, Object obj) {
        try {
            Log.d(str, C0109b.m235a(obj.toString()));
        } catch (Exception e) {
        }
    }

    public static void m238b(String str, Object obj) {
        try {
            Log.i(str, C0109b.m235a(obj.toString()));
        } catch (Exception e) {
        }
    }

    public static void m239c(String str, Object obj) {
        try {
            if (f370a) {
                Log.e(str, C0109b.m235a(obj.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m237a(String str, Object obj, Throwable th) {
        try {
            if (f370a) {
                Log.e(str, C0109b.m235a(obj.toString()), th);
            }
        } catch (Exception e) {
        }
    }

    private static String m235a(String str) {
        return "[TID " + Process.myTid() + "] " + str;
    }
}
