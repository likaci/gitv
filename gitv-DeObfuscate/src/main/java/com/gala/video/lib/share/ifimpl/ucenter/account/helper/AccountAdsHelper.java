package com.gala.video.lib.share.ifimpl.ucenter.account.helper;

import android.content.Context;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.system.preference.AppPreference;

public class AccountAdsHelper {
    public static final String SHARED_PREF_SKIPAD = "player_skip_ad";
    private static final String TAG = "UtilsPreference";

    public static void handelCheckVipAccountException(ApiException e) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "handelCheckVipAccountException(), exception code:" + e.getCode());
        }
        Context ctx = AppRuntimeEnv.get().getApplicationContext();
        if (!StringUtils.isEmpty(e.getCode())) {
            new AppPreference(ctx, SHARED_PREF_SKIPAD).save(SHARED_PREF_SKIPAD, false);
        }
    }

    public static void handelCheckVipAccount(ApiResultKeepaliveInterval result) {
        Context ctx = AppRuntimeEnv.get().getApplicationContext();
        if (result != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "handelCheckVipAccount(), checksign=" + result.checkSign());
            }
            if (result.checkSign()) {
                new AppPreference(ctx, SHARED_PREF_SKIPAD).save(SHARED_PREF_SKIPAD, true);
            } else {
                new AppPreference(ctx, SHARED_PREF_SKIPAD).save(SHARED_PREF_SKIPAD, false);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "handelCheckVipAccount(), result=" + result);
        }
    }

    public static boolean isShouldSkipAd(Context ctx) {
        boolean isShouldSkipAd = new AppPreference(ctx, SHARED_PREF_SKIPAD).getBoolean(SHARED_PREF_SKIPAD, false);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isShouldSkipAd, return " + isShouldSkipAd);
        }
        return isShouldSkipAd;
    }

    public static void updateSkipAdState(Context context, boolean isSkipAd) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateSkipAdState: " + isSkipAd);
        }
        new AppPreference(context, SHARED_PREF_SKIPAD).save(SHARED_PREF_SKIPAD, isSkipAd);
    }

    public static void clearSkipAd(Context ctx) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clearSkipAd");
        }
        new AppPreference(ctx, SHARED_PREF_SKIPAD).save(SHARED_PREF_SKIPAD, false);
    }
}
