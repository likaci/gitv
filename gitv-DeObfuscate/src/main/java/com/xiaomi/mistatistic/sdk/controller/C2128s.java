package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class C2128s {
    public static int m1850a(Context context, String str, int i) {
        return context.getSharedPreferences("mistat", 0).getInt(str, i);
    }

    public static long m1851a(Context context, String str, long j) {
        return context.getSharedPreferences("mistat", 0).getLong(str, j);
    }

    public static String m1852a(Context context, String str, String str2) {
        return context.getSharedPreferences("mistat", 0).getString(str, str2);
    }

    public static boolean m1853a(Context context, String str) {
        return context.getSharedPreferences("mistat", 0).contains(str);
    }

    public static void m1854b(Context context, String str, int i) {
        Editor edit = context.getSharedPreferences("mistat", 0).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static void m1855b(Context context, String str, long j) {
        Editor edit = context.getSharedPreferences("mistat", 0).edit();
        edit.putLong(str, j);
        edit.commit();
    }

    public static void m1856b(Context context, String str, String str2) {
        Editor edit = context.getSharedPreferences("mistat", 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }
}
