package com.gala.video.lib.framework.core.utils;

import android.os.Build;
import android.util.Log;

public class AppGenKeyUtils {
    public static String getKey() {
        StringBuilder sb = new StringBuilder();
        sb.append("BOARD:").append(Build.BOARD).append("---");
        sb.append("BRAND:").append(Build.BRAND).append("---");
        sb.append("CPU_ABI:").append(Build.CPU_ABI).append("---");
        sb.append("MANUFACTURER:").append(Build.MANUFACTURER).append("---");
        sb.append("DEVICE:").append(Build.DEVICE).append("---");
        sb.append("MANUFACTURER:").append(Build.MANUFACTURER).append("---");
        sb.append("MANUFACTURER:").append(Build.MANUFACTURER).append("---");
        sb.append("MODEL:").append(Build.MODEL).append("---");
        sb.append("PRODUCT:").append(Build.PRODUCT).append("---");
        sb.append("MODEL:").append(Build.MODEL).append("---");
        String res = MD5Util.MD5(sb.toString());
        Log.i("GENKEY", res);
        return res;
    }
}
