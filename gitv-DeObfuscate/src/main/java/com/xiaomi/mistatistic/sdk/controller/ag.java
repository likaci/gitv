package com.xiaomi.mistatistic.sdk.controller;

import com.xiaomi.mistatistic.sdk.C2102a;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

class ag implements C2105e {
    final /* synthetic */ ab f2193a;

    ag(ab abVar) {
        this.f2193a = abVar;
    }

    public void mo4534a() {
        if (MiStatInterface.isExceptionCatcherEnabled() && !MiStatInterface.shouldExceptionUploadImmediately()) {
            for (Throwable a : C2102a.m1763b()) {
                C2102a.m1761a(a);
            }
            C2102a.m1765c();
        }
    }
}
