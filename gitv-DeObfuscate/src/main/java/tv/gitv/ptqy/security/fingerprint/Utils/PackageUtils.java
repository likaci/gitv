package tv.gitv.ptqy.security.fingerprint.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import tv.gitv.ptqy.security.fingerprint.LogMgr;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;

public class PackageUtils {
    public static String getPackageName(Context context) {
        try {
            return context.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return String.valueOf(packageInfo.versionCode);
            }
            return versionName;
        } catch (Exception e) {
            LogMgr.m1896e(Consts.TAG, "getAppVersion failed: name not found.");
            e.printStackTrace();
            return "";
        }
    }
}
