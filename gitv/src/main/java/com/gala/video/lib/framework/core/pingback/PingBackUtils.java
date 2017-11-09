package com.gala.video.lib.framework.core.pingback;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;
import java.util.UUID;

public class PingBackUtils {
    private static String sTabName = "";
    private static String sTabSrc;

    public static String createEventId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void setTabSrc(String tabSrc) {
        sTabSrc = tabSrc;
    }

    public static String getTabSrc() {
        return sTabSrc;
    }

    public static String getTabName() {
        return sTabName;
    }

    public static void setTabName(String tabName) {
        sTabName = tabName;
    }

    public static String getLauncherPackageName(Context context) {
        String str = "";
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            List<ResolveInfo> resList = context.getPackageManager().queryIntentActivities(intent, 32);
            ResolveInfo defaultRes = context.getPackageManager().resolveActivity(intent, 32);
            String defaultStr = "";
            if (defaultRes != null) {
                defaultStr = defaultRes.activityInfo.packageName;
                if (defaultStr.equals("android")) {
                    defaultStr = "";
                }
            }
            if (resList != null && resList.size() > 0) {
                str = str + defaultStr;
                for (ResolveInfo r : resList) {
                    String temp = r.activityInfo.packageName;
                    if (!temp.equals(defaultStr)) {
                        str = str + "|" + temp;
                    }
                }
                if (!str.isEmpty() && str.length() > 1 && str.indexOf("|") == 0) {
                    str = str.substring(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
