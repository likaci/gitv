package com.tvos.apps.utils.sys;

import android.util.Log;
import com.tvos.apps.utils.PropUtil;

public class GameBoxSysComUtils {
    private static final String PROP_NAME_OTADEBUG = "persist.ubootenv.otadebug";
    private static final String PROP_VALUE_OTADEBUG_DEV = "develop";
    private static final String TAG = GameBoxSysComUtils.class.getSimpleName();

    public static boolean isDevMode() {
        boolean isDev = false;
        String otaVersion = null;
        try {
            otaVersion = PropUtil.getProp("persist.ubootenv.otadebug");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PROP_VALUE_OTADEBUG_DEV.equals(otaVersion)) {
            isDev = true;
        }
        Log.i(TAG, "GameBox current " + (isDev ? "is" : "is not") + " in developer mode.");
        return isDev;
    }
}
