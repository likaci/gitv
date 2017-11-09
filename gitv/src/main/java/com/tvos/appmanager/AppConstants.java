package com.tvos.appmanager;

public class AppConstants {
    public static final String ANDROID_PKG_NAME = "com.android";
    public static final String ANDROID_SETTINGS_NAME = "com.android.settings";
    public static final int APP_STATUS_INIT = 0;
    public static final int APP_STATUS_INTALLED = 1;
    public static final int APP_STATUS_STARTED = 2;
    public static final int APP_STATUS_UNINSTALLED = 3;
    public static final String[] PKG_BLACK_LIST = new String[]{ANDROID_PKG_NAME};
    public static final String[] PKG_WHITE_LIST = new String[]{TVOS_PKG_NAME, ANDROID_SETTINGS_NAME};
    public static final String TVOS_PKG_NAME = "com.tvos";
}
