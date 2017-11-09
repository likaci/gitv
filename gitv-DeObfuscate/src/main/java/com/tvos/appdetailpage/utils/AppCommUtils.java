package com.tvos.appdetailpage.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.tvos.appdetailpage.model.AppDetailInfo;
import com.tvos.appdetailpage.model.VersionInfo;
import com.tvos.appdetailpage.ui.AppStoreDetailActivity;
import com.tvos.appmanager.IAppManager;
import com.tvos.appmanager.model.IAppInfo;

public class AppCommUtils {
    public static final int ACTION_MODE_DOWNLOAD = 1;
    public static final int ACTION_MODE_DOWNLOADING = 7;
    public static final int ACTION_MODE_INSTALL = 2;
    public static final int ACTION_MODE_INSTALLED_STICK = 4;
    public static final int ACTION_MODE_INSTALLED_UPGRADE_STICK = 5;
    public static final int ACTION_MODE_SYSTEM_APP = 6;
    public static final int ACTION_MODE_UPGRADING = 8;

    public static int getAppState(AppDetailInfo appDetailInfo, Context mContext) {
        IAppInfo localAppInfo = getLocalAppInfo(appDetailInfo.getAppPackageName());
        if (localAppInfo == null) {
            if (isCompleteAppPkgExist(appDetailInfo.getAppDownloadUrl())) {
                return 2;
            }
            return 1;
        } else if (localAppInfo.isSystemApp()) {
            return 6;
        } else {
            if (isAppHasUpgrade(mContext, appDetailInfo.getAppVersion(), appDetailInfo.getAppPackageName())) {
                return 5;
            }
            return 4;
        }
    }

    public static boolean isNetError() {
        int netState = NetWorkManager.getInstance().getNetState();
        return netState == 0 || netState == 3 || netState == 4;
    }

    public static PackageInfo getMyPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return info;
        }
    }

    public static boolean shouldAppFilter(String pkgname) {
        if (pkgname == null) {
            return false;
        }
        for (String filterApp : AppStoreDetailActivity.getFilterPkgNameList()) {
            if (filterApp.equals(pkgname)) {
                return true;
            }
        }
        return false;
    }

    public static IAppInfo getLocalAppInfo(String appPackageName) {
        if (StringUtils.isEmpty(appPackageName)) {
            return null;
        }
        IAppManager appManager = AppStoreDetailActivity.getAppManager();
        if (appManager != null) {
            IAppInfo localApp = appManager.getOriginAppInfoByPkg(appPackageName);
            if (localApp != null) {
                return localApp;
            }
        }
        return null;
    }

    public static VersionInfo getVersion(Context mContext, String pkgName) throws NameNotFoundException {
        if (mContext == null) {
            throw new NullPointerException("--getVersion () mContext is null.");
        }
        PackageInfo packInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
        VersionInfo mVersion = new VersionInfo();
        mVersion.setVerCode(packInfo.versionCode);
        mVersion.setVerName(packInfo.versionName);
        return mVersion;
    }

    public static boolean isAppHasUpgrade(Context mContext, String mAppLatestVersion, String mAppPackageName) {
        if (StringUtils.isEmpty(mAppLatestVersion)) {
            return false;
        }
        try {
            if (Integer.parseInt(mAppLatestVersion) > getVersion(mContext, mAppPackageName).getVerCode()) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppInstalled(String mAppPackageName) {
        if (StringUtils.isEmpty(mAppPackageName)) {
            return false;
        }
        IAppManager appManager = AppStoreDetailActivity.getAppManager();
        if (appManager == null || appManager.getOriginAppInfoByPkg(mAppPackageName) == null) {
            return false;
        }
        return true;
    }

    public static boolean isCompleteAppPkgExist(String filePath) {
        IAppManager appManager = AppStoreDetailActivity.getAppManager();
        if (appManager == null || appManager.getAppInfoByPath(filePath) == null) {
            return false;
        }
        return true;
    }
}
