package com.xiaomi.mistatistic.sdk.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.xiaomi.mistatistic.sdk.data.C2142f;
import com.xiaomi.mistatistic.sdk.data.C2143g;

public class C2133x {
    private static C2133x f2222a;
    private static long f2223c = 30000;
    private Handler f2224b = new C2134y(this, Looper.getMainLooper());

    private C2133x() {
    }

    public static C2133x m1870a() {
        if (f2222a == null) {
            f2222a = new C2133x();
        }
        return f2222a;
    }

    private String m1872a(String str, String str2) {
        return TextUtils.isEmpty(str) ? str2 : "," + str2;
    }

    private void m1873a(Context context, long j, long j2) {
        String b = C2126q.m1847b(context.getApplicationContext());
        if (TextUtils.isEmpty(b)) {
            b = "NULL";
        }
        C2122m.m1837a(new C2143g(j, j2, b));
        C2128s.m1855b(context.getApplicationContext(), "session_begin", 0);
        C2128s.m1855b(C2111a.m1779a(), "last_deactivate", 0);
    }

    private void m1874a(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            C2122m.m1837a(new C2142f(str));
            C2128s.m1856b(context, "pv_path", "");
        }
    }

    public void m1878a(Activity activity, String str) {
        this.f2224b.removeMessages(31415927);
        C2112b.m1802a().m1805a(new C2135z(this, activity, str));
    }

    public void m1879b() {
        C2112b.m1802a().m1805a(new aa(this));
        this.f2224b.sendEmptyMessageDelayed(31415927, f2223c);
    }
}
