package com.xiaomi.mistatistic.sdk.controller;

import android.app.Activity;
import android.text.TextUtils;

class z implements e {
    final /* synthetic */ Activity a;
    final /* synthetic */ String b;
    final /* synthetic */ x c;

    z(x xVar, Activity activity, String str) {
        this.c = xVar;
        this.a = activity;
        this.b = str;
    }

    public void a() {
        String str;
        long currentTimeMillis = System.currentTimeMillis();
        long a = s.a(this.a.getApplicationContext(), "session_begin", 0);
        long a2 = s.a(this.a.getApplicationContext(), "last_deactivate", 0);
        String a3 = s.a(this.a.getApplicationContext(), "pv_path", "");
        if (a <= 0) {
            s.b(this.a.getApplicationContext(), "session_begin", currentTimeMillis);
        } else if (a2 <= 0) {
            s.b(this.a.getApplicationContext(), "session_begin", currentTimeMillis);
            if (!TextUtils.isEmpty(a3)) {
                this.c.a(this.a, a3);
                a3 = "";
            }
        }
        if (a2 > 0 && currentTimeMillis - a2 > x.c) {
            this.c.a(this.a, a, a2);
            if (TextUtils.isEmpty(a3)) {
                str = a3;
            } else {
                this.c.a(this.a, a3);
                str = "";
            }
            s.b(this.a.getApplicationContext(), "session_begin", currentTimeMillis);
            a3 = str;
        }
        str = TextUtils.isEmpty(this.b) ? this.a.getClass().getName() : this.b;
        CharSequence packageName = this.a.getPackageName();
        if (str.startsWith(packageName)) {
            str = str.replace(packageName, "");
        }
        if (!a3.endsWith(str)) {
            s.b(this.a.getApplicationContext(), "pv_path", this.c.a(a3, str));
        }
    }
}
