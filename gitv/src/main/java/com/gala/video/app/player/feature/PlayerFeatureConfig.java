package com.gala.video.app.player.feature;

import android.text.TextUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PlayerFeatureConfig {
    private static final String TAG = "PlayerFeatureConfig";
    private static String[][] sUuidMap;

    static {
        String[][] strArr = new String[5][];
        strArr[0] = new String[]{"20151008165644517WcSMVdcd11107", "20151008165818053iSuCfKWc11108"};
        strArr[1] = new String[]{"20150528161251520iFmhhbNz10660", "20150915104016040NlciRhVW11081"};
        strArr[2] = new String[]{"20150331225606162DhRDLBgW10561", "20150915103743209JvKXBQwV11080"};
        strArr[3] = new String[]{"20150331230058062wNjxNebB10563", "20150915103441729oVUlIida11078"};
        strArr[4] = new String[]{"20150331231012030zPpBdoTv10565", "20150915103625159XxXPmivY11079"};
        sUuidMap = strArr;
    }

    public static String getPluginUuid(String uuid) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPluginUuid() hostUuid=" + uuid);
        }
        String pluginUuid = null;
        if (!TextUtils.isEmpty(uuid)) {
            for (int i = 0; i < sUuidMap.length; i++) {
                if (uuid.equals(sUuidMap[i][0])) {
                    pluginUuid = sUuidMap[i][1];
                    break;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPluginUuid() return=" + pluginUuid);
        }
        return pluginUuid;
    }
}
