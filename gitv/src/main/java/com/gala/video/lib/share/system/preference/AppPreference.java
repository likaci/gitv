package com.gala.video.lib.share.system.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class AppPreference extends AppPreferenceProvider {
    private static final String LOG_TAG = "SYSTEM/preference/AppPreference";
    private SharedPreferences mSharedPref;

    public static AppPreference get(Context ctx, String name) {
        return new AppPreference(ctx, name);
    }

    @SuppressLint({"WorldReadableFiles"})
    public AppPreference(Context ctx, String name) {
        if (ctx != null) {
            this.mSharedPref = ctx.getSharedPreferences(name, 5);
        } else {
            LogUtils.e(LOG_TAG, "EXCEPTION ---- AppPreference(Context ctx, String name) --- Context IS NULL");
        }
    }

    public void save(String key, String value) {
        if (this.mSharedPref != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppPreference --- save --- 【", key, ",", value, "】");
            }
            this.mSharedPref.edit().putString(key, value).apply();
        }
    }

    public void save(String key, long value) {
        if (this.mSharedPref != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppPreference --- save --- 【", key, ",", Long.valueOf(value), "】");
            }
            this.mSharedPref.edit().putLong(key, value).apply();
        }
    }

    public void save(String key, int value) {
        if (this.mSharedPref != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppPreference --- save --- 【", key, ",", Integer.valueOf(value), "】");
            }
            this.mSharedPref.edit().putInt(key, value).apply();
        }
    }

    public void save(String key, boolean value) {
        if (this.mSharedPref != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppPreference --- save --- 【", key, ",", Boolean.valueOf(value), "】");
            }
            this.mSharedPref.edit().putBoolean(key, value).apply();
        }
    }

    public String get(String key) {
        return get(key, "");
    }

    public String get(String key, String defaultValue) {
        return this.mSharedPref == null ? defaultValue : this.mSharedPref.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return this.mSharedPref == null ? defaultValue : this.mSharedPref.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return this.mSharedPref == null ? defaultValue : this.mSharedPref.getLong(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.mSharedPref == null ? defaultValue : this.mSharedPref.getBoolean(key, defaultValue);
    }

    public static void save(Context ctx, String name, String key, String value) {
        AppPreference preference = get(ctx, name);
        if (preference != null) {
            preference.save(key, value);
        }
    }

    public static String get(Context ctx, String name, String key) {
        return get(ctx, name, key, "");
    }

    public static String get(Context ctx, String name, String key, String defaultValue) {
        AppPreference preference = get(ctx, name);
        return preference == null ? defaultValue : preference.get(key, defaultValue);
    }

    public void clear() {
        if (this.mSharedPref != null) {
            this.mSharedPref.edit().clear().apply();
        }
    }
}
