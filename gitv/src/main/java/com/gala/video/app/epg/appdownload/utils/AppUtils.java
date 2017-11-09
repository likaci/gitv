package com.gala.video.app.epg.appdownload.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.gala.video.app.epg.appdownload.AppDownloadManager;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.push.mqttv3.internal.ClientDefaults;
import java.io.File;

public class AppUtils {
    private static final String TAG = "AppUtils";

    public static boolean isInstalled(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            LogRecordUtils.logd(TAG, "isInstalled: packageName -> " + packageName + ", this app not install.");
        }
        if (packageInfo != null) {
            return true;
        }
        return false;
    }

    public static boolean startApp(Context context, String packageName) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return false;
        }
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(packageinfo.packageName);
        ResolveInfo resolveinfo = (ResolveInfo) context.getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
        if (resolveinfo != null) {
            String pkgName = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            intent.setComponent(new ComponentName(pkgName, className));
            context.startActivity(intent);
        }
        return true;
    }

    public static void installApp(Context context, String apkFilePath) {
        LogRecordUtils.logd(TAG, "installApp: apkFilePath -> " + apkFilePath);
        if (!StringUtils.isEmpty((CharSequence) apkFilePath)) {
            try {
                Uri uri = Uri.fromFile(new File(apkFilePath));
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean downloadApp(Context context, PromotionAppInfo promotionAppInfo) {
        return AppDownloadManager.getInstance().downloadApp(context, promotionAppInfo);
    }
}
