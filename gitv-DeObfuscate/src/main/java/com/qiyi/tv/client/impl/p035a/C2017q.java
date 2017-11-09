package com.qiyi.tv.client.impl.p035a;

import com.qiyi.tv.client.impl.Log;

public final class C2017q {
    private static int f2124a = 60;

    public static int m1717a() {
        return f2124a;
    }

    public static void m1718a(int i) {
        Log.m1620d("ServerConfig", "setMaxPageSize(" + i + ") old=" + f2124a);
        if (i > 0) {
            f2124a = i;
        }
    }
}
