package com.gala.video.app.epg.home.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.view.View;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfoWithDocument;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.system.preference.AppPreference;

public class PromotionUtil {
    public static final int DIRECTION_DOWN = 3;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_UP = 4;
    public static final String KEY_POKER_PROMOTION = "poker_app";
    private static final String PROMOTION_STATUS = "promotion_app";

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppAlreadyInstalled(Context context, String packageName, String type) {
        boolean systemInstalled = isPackageInstalled(packageName, context.getPackageManager());
        if (systemInstalled) {
            setAppInstalled(context, type, true);
        }
        return systemInstalled;
    }

    public static boolean judgementAppShouldShow(Context context, PromotionMessage pokerMsg, String type) {
        if (AppPreference.get(context, PROMOTION_STATUS).getBoolean(type, false) || pokerMsg == null) {
            return false;
        }
        PromotionAppInfo localAppInfo = getPromotionAppInfo(pokerMsg);
        if (localAppInfo == null) {
            return false;
        }
        String appPckName = localAppInfo.getAppPckName();
        if (TextUtils.isEmpty(appPckName) || isAppAlreadyInstalled(context, appPckName, type)) {
            return false;
        }
        return true;
    }

    public static boolean judgmentNextFocusIsSelf(int direction, View view) {
        switch (direction) {
            case 1:
                if (view.getNextFocusLeftId() != view.getId()) {
                    return false;
                }
                return true;
            case 2:
                if (view.getNextFocusRightId() != view.getId()) {
                    return false;
                }
                return true;
            case 3:
                return view.getNextFocusDownId() == view.getId();
            case 4:
                if (view.getNextFocusUpId() != view.getId()) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public static void setAppInstalled(Context context, String type, boolean isInstall) {
        AppPreference.get(context, PROMOTION_STATUS).save(type, isInstall);
    }

    public static PromotionAppInfo getPromotionAppInfo(PromotionMessage message) {
        if (message != null) {
            PromotionAppInfoWithDocument document = message.getDocument();
            if (document != null) {
                PromotionAppInfo appInfo = document.getAppInfo();
                if (appInfo != null) {
                    return appInfo;
                }
            }
        }
        return null;
    }

    public static PromotionAppInfoWithDocument getPromotionDocument(PromotionMessage message) {
        if (message != null) {
            PromotionAppInfoWithDocument document = message.getDocument();
            if (document != null) {
                return document;
            }
        }
        return null;
    }

    public static boolean isAppSupport(String appUri) {
        return (TextUtils.isEmpty(appUri) || "none".equals(appUri)) ? false : true;
    }
}
