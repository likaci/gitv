package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

class y extends Handler {
    final /* synthetic */ x a;

    y(x xVar, Looper looper) {
        this.a = xVar;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 31415927:
                long a = s.a(a.a(), "session_begin", 0);
                long a2 = s.a(a.a(), "last_deactivate", 0);
                String a3 = s.a(a.a(), "pv_path", "");
                if (a > 0 && a2 > a) {
                    this.a.a(a.a(), a, a2);
                }
                if (!TextUtils.isEmpty(a3)) {
                    this.a.a(a.a(), a3);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
