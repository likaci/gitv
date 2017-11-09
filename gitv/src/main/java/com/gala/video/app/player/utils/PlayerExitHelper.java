package com.gala.video.app.player.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerExitHelper.Wrapper;

public class PlayerExitHelper extends Wrapper {
    public static final String SHARED_PREF_NAME = "player_playerview";
    private static final String SHARED_PREF_TAG_CAROUSEL_HINT_SHOW = "carousel_hint_show";
    private static final String SKIP_AD = "skip_ad_tip";
    private static final String SKIP_AD_COUNT = "skip_ad_count";
    private static final String TAG = "Player/PlayerExitHelper";
    private SharedPreferences mSharedPref;

    public void clearCarouselSharePre(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clearCarouselSharePre()");
        }
        clearSkipAdCount(context);
        if (context != null) {
            this.mSharedPref = context.getSharedPreferences(SHARED_PREF_NAME, 5);
        }
        save(SHARED_PREF_TAG_CAROUSEL_HINT_SHOW, false);
    }

    private void save(String key, boolean value) {
        if (this.mSharedPref != null) {
            boolean success = this.mSharedPref.edit().putBoolean(key, value).commit();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "success" + success);
            }
        }
    }

    private void clearSkipAdCount(Context context) {
        if (context != null) {
            this.mSharedPref = context.getSharedPreferences(SKIP_AD, 5);
            this.mSharedPref.edit().putInt(SKIP_AD_COUNT, 0).commit();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "clearSkipAdCount()");
            }
        }
    }
}
