package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public abstract class a {
    private static Context a;
    private static String b;
    private static String c;
    private static String d;

    public static Context a() {
        return a;
    }

    public static void a(Context context, String str, String str2, String str3) {
        a = context.getApplicationContext();
        b = str;
        c = str2;
        d = str3;
    }

    public static String b() {
        return b;
    }

    public static String c() {
        return c;
    }

    public static String d() {
        return d;
    }

    public static String e() {
        try {
            PackageInfo packageInfo = a.getPackageManager().getPackageInfo(a.getPackageName(), 16384);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (NameNotFoundException e) {
        }
        return null;
    }
}
