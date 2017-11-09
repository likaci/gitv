package com.gala.tvapi.tv3.cache;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.gala.tvapi.tv3.TVApiConfig;

public abstract class ApiCache {
    protected static void m732a(String str, String str2) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            Editor edit = context.getSharedPreferences("tvapi", 0).edit();
            edit.putString(str, str2);
            edit.commit();
        }
    }

    protected static void m731a(String str, long j) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            Editor edit = context.getSharedPreferences("tvapi", 0).edit();
            edit.putLong(str, j);
            edit.commit();
        }
    }

    protected static String m730a(String str) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            return context.getSharedPreferences("tvapi", 0).getString(str, null);
        }
        return null;
    }

    protected static long m729a(String str) {
        Context context = TVApiConfig.get().getContext();
        if (context != null) {
            return context.getSharedPreferences("tvapi", 0).getLong(str, 0);
        }
        return 0;
    }
}
