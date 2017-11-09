package com.gala.video.app.stub.host;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preference {
    private static final String LOG_TAG = "host/preference";
    private SharedPreferences mSharedPref;

    public static Preference get(Context ctx, String name) {
        return new Preference(ctx, name);
    }

    @SuppressLint({"WorldReadableFiles"})
    public Preference(Context ctx, String name) {
        if (ctx != null) {
            this.mSharedPref = ctx.getSharedPreferences(name, 5);
        } else {
            Log.e(LOG_TAG, "EXCEPTION ,Preference(Context ctx, String name),Context IS NULL");
        }
    }

    public void save(String key, String value) {
        if (this.mSharedPref != null) {
            Log.d(LOG_TAG, "Preference save 【" + key + "," + value + "】");
            this.mSharedPref.edit().putString(key, value).apply();
        }
    }

    public void save(String key, long value) {
        if (this.mSharedPref != null) {
            Log.d(LOG_TAG, "Preference save 【" + key + "," + value + "】");
            this.mSharedPref.edit().putLong(key, value).apply();
        }
    }

    public void save(String key, int value) {
        if (this.mSharedPref != null) {
            Log.d(LOG_TAG, "Preference save【" + key + "," + value + "】");
            this.mSharedPref.edit().putInt(key, value).apply();
        }
    }

    public void save(String key, boolean value) {
        if (this.mSharedPref != null) {
            Log.d(LOG_TAG, "Preference save【" + key + "," + value + "】");
            this.mSharedPref.edit().putBoolean(key, value).apply();
        }
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
}
