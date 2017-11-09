package com.xiaomi.mistatistic.sdk;

public class BuildSetting {
    private static boolean a = false;

    public static boolean isTest() {
        return a;
    }

    public static void setTest(boolean z) {
        a = z;
    }
}
