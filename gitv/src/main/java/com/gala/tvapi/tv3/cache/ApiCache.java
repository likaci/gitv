package com.gala.tvapi.tv3.cache;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.gala.tvapi.tv3.TVApiConfig;

public abstract class ApiCache {
    protected static void a(String str, String str2) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            Editor edit = context.getSharedPreferences("tvapi", 0).edit();
            edit.putString(str, str2);
            edit.commit();
        }
    }

    protected static void a(String str, long j) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            Editor edit = context.getSharedPreferences("tvapi", 0).edit();
            edit.putLong(str, j);
            edit.commit();
        }
    }

    protected static String m93a(String str) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            return context.getSharedPreferences("tvapi", 0).getString(str, null);
        }
        return null;
    }

    protected static long a(String str) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            return context.getSharedPreferences("tvapi", 0).getLong(str, 0);
        }
        return 0;
    }
}
