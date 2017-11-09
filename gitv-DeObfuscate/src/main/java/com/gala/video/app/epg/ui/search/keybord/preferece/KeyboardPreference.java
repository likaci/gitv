package com.gala.video.app.epg.ui.search.keybord.preferece;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.AppPreference;

public class KeyboardPreference {
    private static final String DOWNLOAD_ADDRESS = "download_address";
    private static final String NAME = "keyboard";
    private static final String TAG = "EPG/KeyboardPreference";

    public static void saveDownloadAddress(Context context, String address) {
        new AppPreference(context, "keyboard").save(DOWNLOAD_ADDRESS, address);
        LogUtils.m1574i(TAG, "saveDownloadAddress() -> address = " + address);
    }

    public static String getDownloadAddress(Context context) {
        String address = new AppPreference(context, "keyboard").get(DOWNLOAD_ADDRESS, "");
        LogUtils.m1574i(TAG, "getApkDownloadUrl() -> address = " + address);
        return address;
    }
}
