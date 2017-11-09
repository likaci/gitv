package com.gala.tvapi.a;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public final class a {
    private static final a a = new a();
    private Context f439a;

    public static a a() {
        return a;
    }

    public final void a(Context context) {
        this.f439a = context;
    }

    public final void a(String str, String str2, String str3) {
        if (this.f439a != null) {
            Editor edit = this.f439a.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            edit.commit();
        }
    }

    public final void a(String str, String str2, long j) {
        if (this.f439a != null) {
            Editor edit = this.f439a.getSharedPreferences(str, 0).edit();
            edit.putLong(str2, j);
            edit.commit();
        }
    }

    public final String m79a(String str, String str2) {
        if (this.f439a != null) {
            return this.f439a.getSharedPreferences(str, 0).getString(str2, null);
        }
        return null;
    }

    public final long a(String str, String str2) {
        if (this.f439a != null) {
            return this.f439a.getSharedPreferences(str, 0).getLong(str2, 0);
        }
        return 0;
    }
}
