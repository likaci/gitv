package com.xiaomi.mistatistic.sdk.controller;

import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.mistatistic.sdk.a;

class ag implements e {
    final /* synthetic */ ab a;

    ag(ab abVar) {
        this.a = abVar;
    }

    public void a() {
        if (MiStatInterface.isExceptionCatcherEnabled() && !MiStatInterface.shouldExceptionUploadImmediately()) {
            for (Throwable a : a.b()) {
                a.a(a);
            }
            a.c();
        }
    }
}
