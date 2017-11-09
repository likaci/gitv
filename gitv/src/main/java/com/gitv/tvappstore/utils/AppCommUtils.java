package com.gitv.tvappstore.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.gitv.tvappstore.model.VersionInfo;

public class AppCommUtils {
    private static final String LOG_TAG = "AppCommUtils";

    public static PackageInfo getMyPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return info;
        }
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
}
