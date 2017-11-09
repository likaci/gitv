package com.gala.video.app.epg.preference;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.AppPreference;

public class AppStorePreference {
    private static final String HOME_APK_DOWNLOAD_URL = "APK_DOWNLOAD_URL";
    private static final String NAME = "TAB_APPSTORE";
    private static final String TAG = "EPG/system/AppStorePreference";

    public static void saveApkDownloadUrl(Context context, String url) {
        new AppPreference(context, NAME).save(HOME_APK_DOWNLOAD_URL, url);
        LogUtils.m1574i(TAG, "saveApkDownloadUrl() -> url = " + url);
    }

    public static String getApkDownloadUrl(Context context) {
        String url = new AppPreference(context, NAME).get(HOME_APK_DOWNLOAD_URL, "");
        LogUtils.m1574i(TAG, "getApkDownloadUrl() -> url = " + url);
        return url;
    }
}
