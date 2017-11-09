package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public abstract class C2111a {
    private static Context f2178a;
    private static String f2179b;
    private static String f2180c;
    private static String f2181d;

    public static Context m1779a() {
        return f2178a;
    }

    public static void m1780a(Context context, String str, String str2, String str3) {
        f2178a = context.getApplicationContext();
        f2179b = str;
        f2180c = str2;
        f2181d = str3;
    }

    public static String m1781b() {
        return f2179b;
    }

    public static String m1782c() {
        return f2180c;
    }

    public static String m1783d() {
        return f2181d;
    }

    public static String m1784e() {
        try {
            PackageInfo packageInfo = f2178a.getPackageManager().getPackageInfo(f2178a.getPackageName(), 16384);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (NameNotFoundException e) {
        }
        return null;
    }
}
