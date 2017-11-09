package com.gala.video.app.epg.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;

public class SearchPreference {
    private static final String KEY_IS_NEW = "new_search";
    private static final String KEY_TAB_NAME = "keyboard";
    private static final String LOG_TAG = "EPG/search/SearchPreference";
    private static final String SHARED_PREF_NAME = "search_keyboard_type";
    private static SharedPreferences mSharedPref;

    public static void setT9Tab(Context ctx) {
        mSharedPref = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        mSharedPref.edit().putInt("keyboard", 1).commit();
        LogUtils.m1574i(LOG_TAG, ">>>> sharedpreferences save t9");
    }

    public static void setFullTab(Context ctx) {
        mSharedPref = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        mSharedPref.edit().putInt("keyboard", 0).commit();
        LogUtils.m1574i(LOG_TAG, ">>>> sharedpreferences save full");
    }

    public static void setExpandTab(Context ctx) {
        mSharedPref = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        mSharedPref.edit().putInt("keyboard", 2).commit();
        LogUtils.m1574i(LOG_TAG, ">>>> sharedpreferences save expand");
    }

    public static int getKeyTab(Context ctx) {
        mSharedPref = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        LogUtils.m1576i(LOG_TAG, ">>>> sharedpreferences get type is ", Integer.valueOf(mSharedPref.getInt("keyboard", 0)));
        return mSharedPref.getInt("keyboard", getDefaultKeyboardType());
    }

    public static boolean isSearchNew(Context ctx) {
        mSharedPref = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        LogUtils.m1576i(LOG_TAG, ">>>> Is New Search:", Boolean.valueOf(mSharedPref.getBoolean(KEY_IS_NEW, true)));
        return mSharedPref.getBoolean(KEY_IS_NEW, true);
    }

    public static void setSearchNew(Context ctx, boolean isNew) {
        mSharedPref = ctx.getSharedPreferences(SHARED_PREF_NAME, 0);
        mSharedPref.edit().putBoolean(KEY_IS_NEW, isNew).commit();
        LogUtils.m1576i(LOG_TAG, ">>>> sharedpreferences setSearchNew: ", Boolean.valueOf(isNew));
    }

    public static int getDefaultKeyboardType() {
        if (Project.getInstance().getBuild().isLitchi()) {
            return 1;
        }
        return 0;
    }
}
