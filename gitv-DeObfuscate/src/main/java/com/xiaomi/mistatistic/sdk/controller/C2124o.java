package com.xiaomi.mistatistic.sdk.controller;

import android.util.Log;

public class C2124o {
    private static boolean f2214a = false;

    public static void m1839a() {
        f2214a = true;
    }

    public void m1840a(String str) {
        if (f2214a) {
            Log.v("MI_STAT", str);
        }
    }

    public void m1841a(String str, Throwable th) {
        if (f2214a) {
            Log.e("MI_STAT", str, th);
        }
    }
}
