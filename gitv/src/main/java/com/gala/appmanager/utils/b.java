package com.gala.appmanager.utils;

import android.os.Process;
import android.util.Log;

public class b {
    public static boolean a = true;

    public static void a(String str, Object obj) {
        try {
            Log.d(str, a(obj.toString()));
        } catch (Exception e) {
        }
    }

    public static void b(String str, Object obj) {
        try {
            Log.i(str, a(obj.toString()));
        } catch (Exception e) {
        }
    }

    public static void c(String str, Object obj) {
        try {
            if (a) {
                Log.e(str, a(obj.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void a(String str, Object obj, Throwable th) {
        try {
            if (a) {
                Log.e(str, a(obj.toString()), th);
            }
        } catch (Exception e) {
        }
    }

    private static String a(String str) {
        return "[TID " + Process.myTid() + "] " + str;
    }
}
