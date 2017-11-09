package com.tvos.appmanager.model;

import android.graphics.drawable.Drawable;

public interface IAppInfo {
    String getAppAuthor();

    Drawable getAppIcon();

    byte[] getAppIconData();

    String getAppInstalledTime();

    String getAppName();

    String getAppPath();

    String getAppVersion();

    int getAppVersionCode();

    String getPkgName();

    long getRunningTime();

    long getStartTime();

    int getStatus();

    boolean isStarted();

    boolean isSystemApp();
}
