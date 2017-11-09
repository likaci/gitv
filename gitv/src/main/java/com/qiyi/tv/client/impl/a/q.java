package com.qiyi.tv.client.impl.a;

import com.qiyi.tv.client.impl.Log;

public final class q {
    private static int a = 60;

    public static int a() {
        return a;
    }

    public static void a(int i) {
        Log.d("ServerConfig", "setMaxPageSize(" + i + ") old=" + a);
        if (i > 0) {
            a = i;
        }
    }
}
