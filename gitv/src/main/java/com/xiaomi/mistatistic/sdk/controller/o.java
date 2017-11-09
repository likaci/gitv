package com.xiaomi.mistatistic.sdk.controller;

import android.util.Log;

public class o {
    private static boolean a = false;

    public static void a() {
        a = true;
    }

    public void a(String str) {
        if (a) {
            Log.v("MI_STAT", str);
        }
    }

    public void a(String str, Throwable th) {
        if (a) {
            Log.e("MI_STAT", str, th);
        }
    }
}
