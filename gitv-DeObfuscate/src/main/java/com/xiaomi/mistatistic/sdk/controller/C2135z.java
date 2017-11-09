package com.xiaomi.mistatistic.sdk.controller;

import android.app.Activity;
import android.text.TextUtils;

class C2135z implements C2105e {
    final /* synthetic */ Activity f2226a;
    final /* synthetic */ String f2227b;
    final /* synthetic */ C2133x f2228c;

    C2135z(C2133x c2133x, Activity activity, String str) {
        this.f2228c = c2133x;
        this.f2226a = activity;
        this.f2227b = str;
    }

    public void mo4534a() {
        String str;
        long currentTimeMillis = System.currentTimeMillis();
        long a = C2128s.m1851a(this.f2226a.getApplicationContext(), "session_begin", 0);
        long a2 = C2128s.m1851a(this.f2226a.getApplicationContext(), "last_deactivate", 0);
        String a3 = C2128s.m1852a(this.f2226a.getApplicationContext(), "pv_path", "");
        if (a <= 0) {
            C2128s.m1855b(this.f2226a.getApplicationContext(), "session_begin", currentTimeMillis);
        } else if (a2 <= 0) {
            C2128s.m1855b(this.f2226a.getApplicationContext(), "session_begin", currentTimeMillis);
            if (!TextUtils.isEmpty(a3)) {
                this.f2228c.m1874a(this.f2226a, a3);
                a3 = "";
            }
        }
        if (a2 > 0 && currentTimeMillis - a2 > C2133x.f2223c) {
            this.f2228c.m1873a(this.f2226a, a, a2);
            if (TextUtils.isEmpty(a3)) {
                str = a3;
            } else {
                this.f2228c.m1874a(this.f2226a, a3);
                str = "";
            }
            C2128s.m1855b(this.f2226a.getApplicationContext(), "session_begin", currentTimeMillis);
            a3 = str;
        }
        str = TextUtils.isEmpty(this.f2227b) ? this.f2226a.getClass().getName() : this.f2227b;
        CharSequence packageName = this.f2226a.getPackageName();
        if (str.startsWith(packageName)) {
            str = str.replace(packageName, "");
        }
        if (!a3.endsWith(str)) {
            C2128s.m1856b(this.f2226a.getApplicationContext(), "pv_path", this.f2228c.m1872a(a3, str));
        }
    }
}
