package com.gala.sdk.plugin.server.utils;

import android.os.Bundle;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.download.DownloadInfo;
import com.gala.sdk.plugin.server.upgrade.UpgradeInfo;

public class DataUtils {
    private static final String EXTRA_APPINFO = "appinfo";
    private static final String TAG = "DataUtils";

    public static void putAppInfo(Bundle bundle, AppInfo info) {
        if (Log.VERBOSE) {
            Log.v(TAG, "putAppInfo(bundle=" + bundle + ", info=" + info + ")");
        }
        bundle.putParcelable("appinfo", info);
    }

    public static AppInfo getAppInfo(Bundle bundle) {
        if (Log.VERBOSE) {
            Log.v(TAG, "getAppInfo<<(bundle=" + bundle + ")");
        }
        AppInfo info = (AppInfo) bundle.getParcelable("appinfo");
        if (Log.VERBOSE) {
            Log.v(TAG, "getAppInfo>>() return " + info);
        }
        return info;
    }

    public static DownloadInfo covertToDownloadInfo(UpgradeInfo upgradeInfo, String savePath) {
        if (Log.VERBOSE) {
            Log.v(TAG, "covertToDownloadInfo<<(upgradeInfo=" + upgradeInfo + ", savePath=" + savePath + ")");
        }
        DownloadInfo downloadInfo = new DownloadInfo(upgradeInfo.getUrl(), savePath);
        downloadInfo.setMd5(upgradeInfo.getMd5());
        if (Log.VERBOSE) {
            Log.v(TAG, "covertToDownloadInfo>>() return " + downloadInfo);
        }
        return downloadInfo;
    }
}
