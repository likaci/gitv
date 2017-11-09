package com.gala.video.lib.framework.core.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    public static boolean isNetworkAvaliable() {
        int state = 0;
        try {
            state = NetWorkManager.getInstance().getNetState();
        } catch (Exception e) {
        }
        boolean avaliable = state == 1 || state == 2;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isNetworkAvaliable() state=" + state + ", return " + avaliable);
        }
        return avaliable;
    }

    public static boolean isWifiConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == 1) {
            return true;
        }
        return false;
    }
}
