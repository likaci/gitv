package com.gala.video.lib.share.system.preference;

import android.content.Context;
import com.gala.video.lib.share.system.contentprovider.AppContentProvider;

public abstract class AppPreferenceProvider {
    public abstract void clear();

    public abstract String get(String str);

    public abstract String get(String str, String str2);

    public abstract boolean getBoolean(String str, boolean z);

    public abstract int getInt(String str, int i);

    public abstract long getLong(String str, long j);

    public abstract void save(String str, int i);

    public abstract void save(String str, long j);

    public abstract void save(String str, String str2);

    public abstract void save(String str, boolean z);

    public static void save(Context ctx, String name, String key, String value) {
    }

    public static String get(Context ctx, String name, String key) {
        return name;
    }

    public static String get(Context ctx, String name, String key, String defaultValue) {
        return name;
    }

    public static AppPreferenceProvider get(Context ctx, String preferenceName, boolean isSupportPlayerMultiProcess) {
        if (isSupportPlayerMultiProcess) {
            return new AppContentProvider(ctx, preferenceName);
        }
        return new AppPreference(ctx, preferenceName);
    }
}
