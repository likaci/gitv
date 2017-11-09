package com.xiaomi.mistatistic.sdk.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.xiaomi.mistatistic.sdk.data.f;
import com.xiaomi.mistatistic.sdk.data.g;

public class x {
    private static x a;
    private static long c = 30000;
    private Handler b = new y(this, Looper.getMainLooper());

    private x() {
    }

    public static x a() {
        if (a == null) {
            a = new x();
        }
        return a;
    }

    private String a(String str, String str2) {
        return TextUtils.isEmpty(str) ? str2 : "," + str2;
    }

    private void a(Context context, long j, long j2) {
        String b = q.b(context.getApplicationContext());
        if (TextUtils.isEmpty(b)) {
            b = "NULL";
        }
        m.a(new g(j, j2, b));
        s.b(context.getApplicationContext(), "session_begin", 0);
        s.b(a.a(), "last_deactivate", 0);
    }

    private void a(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            m.a(new f(str));
            s.b(context, "pv_path", "");
        }
    }

    public void a(Activity activity, String str) {
        this.b.removeMessages(31415927);
        b.a().a(new z(this, activity, str));
    }

    public void b() {
        b.a().a(new aa(this));
        this.b.sendEmptyMessageDelayed(31415927, c);
    }
}
