package com.gala.video.lib.share.system.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.AppPreferenceProvider;

public class AppContentProvider extends AppPreferenceProvider {
    private static String AUTHORITY = null;
    private static final String LOG_TAG = "SYSTEM/contentprovider/AppProvider";
    private static String preference = "preference";
    private ContentResolver mContentResolver;
    private Context mContext;
    private String tableName = "";
    private final Uri uri;

    public static AppContentProvider get(Context ctx, String name) {
        return new AppContentProvider(ctx, name);
    }

    public AppContentProvider(Context ctx, String name) {
        if (ctx != null) {
            this.mContext = ctx;
            if (AUTHORITY == null) {
                AUTHORITY = this.mContext.getPackageName() + ".lib.share.system.contentprovider.BaseContentProvider";
            }
        }
        if (this.mContentResolver == null) {
            this.mContentResolver = this.mContext.getContentResolver();
        }
        this.tableName = name;
        this.uri = Uri.parse("content://" + AUTHORITY + "/" + preference);
    }

    public void updateOrInsert(String module, String key, String value) {
        boolean hasfound = false;
        int displayModuleIndex = -1;
        int displayKeyIndex = -1;
        Cursor cursor = this.mContentResolver.query(this.uri, null, null, null, null);
        if (cursor != null) {
            displayModuleIndex = cursor.getColumnIndex("module");
            displayKeyIndex = cursor.getColumnIndex(Album.KEY);
        }
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String keyChecked = cursor.getString(displayKeyIndex);
                String moduleChecked = cursor.getString(displayModuleIndex);
                if (key.equals(keyChecked) && module.equals(moduleChecked)) {
                    ContentValues values = new ContentValues();
                    values.put("value", value);
                    this.mContentResolver.update(this.uri, values, "module=? and key=?", new String[]{module, key});
                    hasfound = true;
                }
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (!hasfound) {
            values = new ContentValues();
            values.put("module", module);
            values.put(Album.KEY, key);
            values.put("value", value);
            this.mContentResolver.insert(this.uri, values);
        }
    }

    public void save(String key, long value) {
        if (this.mContentResolver != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppProvider --- save --- 【", key, ",", Long.valueOf(value), "】");
            }
            updateOrInsert(this.tableName, key, String.valueOf(value));
        }
    }

    public void save(String key, int value) {
        if (this.mContentResolver != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppProvider --- save --- 【", key, ",", Integer.valueOf(value), "】");
            }
            updateOrInsert(this.tableName, key, String.valueOf(value));
        }
    }

    public void save(String key, String value) {
        if (this.mContentResolver != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppProvider --- save --- 【", key, ",", value, "】");
            }
            updateOrInsert(this.tableName, key, value);
        }
    }

    public void save(String key, boolean value) {
        if (this.mContentResolver != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "AppProvider --- save --- 【", key, ",", Boolean.valueOf(value), "】");
            }
            updateOrInsert(this.tableName, key, String.valueOf(value));
        }
    }

    public String get(String key) {
        return get(key, "");
    }

    public String get(String key, String defaultValue) {
        if (this.mContentResolver == null) {
            return defaultValue;
        }
        Cursor cursor = this.mContentResolver.query(this.uri, new String[]{"value"}, "module=? and key=?", new String[]{this.tableName, key}, null);
        String queryValue = defaultValue;
        int displayValueIndex = 0;
        if (cursor != null) {
            displayValueIndex = cursor.getColumnIndex("value");
        }
        if (cursor != null && cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                queryValue = cursor.getString(displayValueIndex);
            }
            cursor.close();
        }
        return queryValue;
    }

    public int getInt(String key, int defaultValue) {
        if (this.mContentResolver != null) {
            try {
                defaultValue = Integer.parseInt(get(key, String.valueOf(defaultValue)));
            } catch (NumberFormatException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(LOG_TAG, e);
                }
            }
        }
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {
        if (this.mContentResolver != null) {
            try {
                defaultValue = Long.parseLong(get(key, String.valueOf(defaultValue)));
            } catch (NumberFormatException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(LOG_TAG, e);
                }
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (this.mContentResolver != null) {
            try {
                defaultValue = Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
            } catch (NumberFormatException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(LOG_TAG, e);
                }
            }
        }
        return defaultValue;
    }

    public static void save(Context ctx, String name, String key, String value) {
        AppContentProvider appProvider = get(ctx, name);
        if (appProvider != null) {
            appProvider.save(key, value);
        }
    }

    public static String get(Context ctx, String name, String key) {
        return get(ctx, name, key, "");
    }

    public static String get(Context ctx, String name, String key, String defaultValue) {
        return get(ctx, name).get(key, defaultValue);
    }

    public void clear() {
        if (this.mContentResolver != null) {
            this.mContentResolver.delete(this.uri, "module=?", new String[]{this.tableName});
        }
    }
}
