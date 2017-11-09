package com.gala.imageprovider.p000private;

import android.content.Context;

public final class C0120D {
    private static C0120D f533a;

    private C0120D() {
    }

    public static synchronized C0120D m274a() {
        C0120D c0120d;
        synchronized (C0120D.class) {
            if (f533a == null) {
                f533a = new C0120D();
            }
            c0120d = f533a;
        }
        return c0120d;
    }

    public static void m275a(Context context) {
        C0126b.m298a();
        C0126b.m299a(context);
    }
}
