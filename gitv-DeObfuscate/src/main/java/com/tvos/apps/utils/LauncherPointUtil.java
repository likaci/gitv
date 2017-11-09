package com.tvos.apps.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class LauncherPointUtil {
    public static void updateLaunchPoint(Context context, boolean enableLaunchPoint, String launcherPointName) {
        int i = 1;
        LogUtil.m1720d("updating launch point: " + enableLaunchPoint);
        PackageManager pm = context.getPackageManager();
        ComponentName compName = new ComponentName(context, launcherPointName);
        if (!enableLaunchPoint) {
            i = 2;
        }
        try {
            pm.setComponentEnabledSetting(compName, i, 1);
        } catch (Exception e) {
            LogUtil.m1720d("updating launch point failed! ");
            if (e.toString().contains("does not exist")) {
                LogUtil.m1720d(new StringBuilder(String.valueOf(launcherPointName)).append("updating launch point  component not exist...").toString());
            }
            e.printStackTrace();
        }
    }
}
