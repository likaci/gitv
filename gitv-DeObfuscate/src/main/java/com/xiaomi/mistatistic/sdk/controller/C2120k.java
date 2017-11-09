package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class C2120k extends Handler {
    final /* synthetic */ C2119j f2211a;

    C2120k(C2119j c2119j, Looper looper) {
        this.f2211a = c2119j;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 1023:
                C2112b.m1804b().m1805a(new C2121l(this));
                return;
            default:
                return;
        }
    }
}
