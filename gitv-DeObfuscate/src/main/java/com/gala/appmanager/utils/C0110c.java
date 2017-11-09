package com.gala.appmanager.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import com.gala.appmanager.appinfo.AppInfo;

public class C0110c {
    public static Resources m240a(Context context, String str) {
        try {
            Class cls = Class.forName("android.content.res.AssetManager");
            Object newInstance = cls.getConstructor((Class[]) null).newInstance((Object[]) null);
            cls.getDeclaredMethod("addAssetPath", new Class[]{String.class}).invoke(newInstance, new Object[]{str});
            Resources resources = context.getResources();
            return (Resources) Resources.class.getConstructor(new Class[]{newInstance.getClass(), resources.getDisplayMetrics().getClass(), resources.getConfiguration().getClass()}).newInstance(new Object[]{newInstance, resources.getDisplayMetrics(), resources.getConfiguration()});
        } catch (Exception e) {
            Log.d("ParseApkUtil", e.toString());
            return null;
        }
    }

    public static AppInfo m241a(Context context, String str) {
        Log.d("ParseApkUtil", "preinstall:" + str);
        PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 1);
        Resources a = C0110c.m240a(context, str);
        if (packageArchiveInfo == null) {
            return null;
        }
        AppInfo appInfo = new AppInfo(packageArchiveInfo.applicationInfo);
        try {
            CharSequence text = a.getText(packageArchiveInfo.applicationInfo.labelRes);
            if (text != null) {
                appInfo.setAppName(text.toString());
            }
        } catch (NotFoundException e) {
            Log.d("ParseApkUtil", "没有找到应用的名字\n" + e.toString());
            String str2 = packageArchiveInfo.applicationInfo.name;
            String str3 = packageArchiveInfo.applicationInfo.packageName;
            if (str2 != null && str2 != "") {
                appInfo.setAppName(packageArchiveInfo.applicationInfo.name + "");
            } else if (str3 == null || str3 == "") {
                Log.d("ParseApkUtil", "应用没有对应的包名");
            } else {
                appInfo.setAppName(str3);
            }
        } catch (Exception e2) {
            Log.d("ParseApkUtil", e2.toString());
        }
        try {
            appInfo.setAppIcon(a.getDrawable(packageArchiveInfo.applicationInfo.icon));
            return appInfo;
        } catch (NotFoundException e3) {
            Log.d("ParseApkUtil", "没有找到应用的图标\n" + e3.toString());
            return appInfo;
        } catch (Exception e22) {
            Log.d("ParseApkUtil", e22.toString());
            return appInfo;
        }
    }
}
