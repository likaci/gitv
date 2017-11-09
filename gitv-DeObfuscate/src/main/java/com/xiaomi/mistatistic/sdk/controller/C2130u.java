package com.xiaomi.mistatistic.sdk.controller;

import android.text.TextUtils;
import com.xiaomi.mistatistic.sdk.controller.p037a.C2108c;

class C2130u implements C2108c {
    final /* synthetic */ C2129t f2219a;

    C2130u(C2129t c2129t) {
        this.f2219a = c2129t;
    }

    public void mo4535a(String str, long j) {
        if (TextUtils.isEmpty(str)) {
            C2129t.f2217b = false;
            return;
        }
        this.f2219a.f2218a = j;
        this.f2219a.m1860a(str);
    }
}
