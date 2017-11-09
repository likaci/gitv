package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

class C2134y extends Handler {
    final /* synthetic */ C2133x f2225a;

    C2134y(C2133x c2133x, Looper looper) {
        this.f2225a = c2133x;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 31415927:
                long a = C2128s.m1851a(C2111a.m1779a(), "session_begin", 0);
                long a2 = C2128s.m1851a(C2111a.m1779a(), "last_deactivate", 0);
                String a3 = C2128s.m1852a(C2111a.m1779a(), "pv_path", "");
                if (a > 0 && a2 > a) {
                    this.f2225a.m1873a(C2111a.m1779a(), a, a2);
                }
                if (!TextUtils.isEmpty(a3)) {
                    this.f2225a.m1874a(C2111a.m1779a(), a3);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
