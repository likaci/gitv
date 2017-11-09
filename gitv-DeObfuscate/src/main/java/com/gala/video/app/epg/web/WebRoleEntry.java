package com.gala.video.app.epg.web;

import android.content.Context;
import com.gala.tvapi.tv2.model.VipGuideInfo;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebRoleEntry.Wrapper;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gitv.tvappstore.utils.SysUtils;

public class WebRoleEntry extends Wrapper {
    private static final String NAME = "open_web_time";
    private static final String NOW_TIME = "_now_time";
    private static final String TAG = "Web/WebRoleUtils";
    private static int currentCount = 0;
    private static boolean isOpenedRoleAlbum = false;
    private static boolean isOpenedRoleVIP = false;

    private boolean isreset() {
        return timeGap() >= 24;
    }

    private void reset() {
        if (isreset()) {
            isOpenedRoleVIP = false;
            isOpenedRoleAlbum = false;
            if (currentCount != 0) {
                currentCount = 0;
            }
        }
    }

    private int getMaxCount() {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (result == null) {
            LogUtils.m1574i(TAG, "DynamicResult is null");
            return 0;
        }
        VipGuideInfo vipGuideInfo = result.getVipGuideInfo();
        if (vipGuideInfo == null) {
            LogUtils.m1574i(TAG, "VipGuideInfo is null");
            return 3;
        }
        int maxCount = vipGuideInfo.count;
        if (maxCount != 0) {
            return maxCount;
        }
        LogUtils.m1574i(TAG, "maxCount is null");
        return maxCount;
    }

    private synchronized void increase() {
        currentCount++;
    }

    private synchronized boolean isAbleToOpen() {
        boolean z;
        if (currentCount < getMaxCount()) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    private long getSysTime() {
        return SysUtils.getSysTime().longValue();
    }

    private void saveTime() {
        new AppPreference(AppRuntimeEnv.get().getApplicationContext(), NAME).save(NOW_TIME, getSysTime());
    }

    private long getLastSaveTime() {
        return new AppPreference(AppRuntimeEnv.get().getApplicationContext(), NAME).getLong(NOW_TIME, getSysTime());
    }

    private long timeGap() {
        long gap = getSysTime() - getLastSaveTime();
        LogUtils.m1574i(TAG, "timeGap = " + gap);
        return ((gap / 1000) / 60) / 60;
    }

    public void showRoleInVip(Context context) {
        reset();
        if (isActivationStatus() && isAbleToOpen() && !isOpenedRoleVIP) {
            GetInterfaceTools.getWebEntry().gotoRoleWebActivity(context);
            saveTime();
            increase();
            isOpenedRoleVIP = true;
        }
    }

    public void showRoleInAlbum(Context context) {
        reset();
        if (isActivationStatus() && isAbleToOpen() && !isOpenedRoleAlbum) {
            GetInterfaceTools.getWebEntry().gotoRoleWebActivity(context);
            saveTime();
            increase();
            isOpenedRoleAlbum = true;
        }
    }

    private static boolean isActivationStatus() {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (result == null) {
            LogUtils.m1574i(TAG, "DynamicResult is null");
            return false;
        }
        VipGuideInfo vipGuideInfo = result.getVipGuideInfo();
        if (vipGuideInfo == null) {
            LogUtils.m1574i(TAG, "VipGuideInfo is null");
            return false;
        } else if (vipGuideInfo.role_type == null) {
            LogUtils.m1568d(TAG, "vipGuideInfo.role_type == null");
            return false;
        } else {
            int currentRole;
            if (GetInterfaceTools.getIGalaAccountManager().getIsLitchiVipForH5()) {
                currentRole = 7;
            } else {
                currentRole = GetInterfaceTools.getIGalaAccountManager().getUserTypeForH5();
            }
            LogUtils.m1568d(TAG, "role = " + currentRole);
            int size = vipGuideInfo.role_type.size();
            for (int i = 0; i < size; i++) {
                if (currentRole == ((Integer) vipGuideInfo.role_type.get(i)).intValue()) {
                    LogUtils.m1568d(TAG, "vipGuideInfo.role_type.get(i) = " + vipGuideInfo.role_type.get(i));
                    return true;
                }
                LogUtils.m1568d(TAG, "vipGuideInfo.role_type.get(i) = " + vipGuideInfo.role_type.get(i));
            }
            return false;
        }
    }
}
