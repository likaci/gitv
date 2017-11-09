package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class k extends Handler {
    final /* synthetic */ j a;

    k(j jVar, Looper looper) {
        this.a = jVar;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 1023:
                b.b().a(new l(this));
                return;
            default:
                return;
        }
    }
}
