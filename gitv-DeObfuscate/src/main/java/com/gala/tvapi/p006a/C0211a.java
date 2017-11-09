package com.gala.tvapi.p006a;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public final class C0211a {
    private static final C0211a f881a = new C0211a();
    private Context f882a;

    public static C0211a m566a() {
        return f881a;
    }

    public final void m569a(Context context) {
        this.f882a = context;
    }

    public final void m571a(String str, String str2, String str3) {
        if (this.f882a != null) {
            Editor edit = this.f882a.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            edit.commit();
        }
    }

    public final void m570a(String str, String str2, long j) {
        if (this.f882a != null) {
            Editor edit = this.f882a.getSharedPreferences(str, 0).edit();
            edit.putLong(str2, j);
            edit.commit();
        }
    }

    public final String m568a(String str, String str2) {
        if (this.f882a != null) {
            return this.f882a.getSharedPreferences(str, 0).getString(str2, null);
        }
        return null;
    }

    public final long m567a(String str, String str2) {
        if (this.f882a != null) {
            return this.f882a.getSharedPreferences(str, 0).getLong(str2, 0);
        }
        return 0;
    }
}
