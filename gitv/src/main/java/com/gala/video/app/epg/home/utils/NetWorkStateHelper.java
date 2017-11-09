package com.gala.video.app.epg.home.utils;

import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;

public class NetWorkStateHelper {
    private static final String TAG = "home/NetWorkStateHelper";
    private boolean mHasNotify = false;
    private Object mLock = new Object();
    private int mNetState = 0;

    public int checkNetWorkSync() {
        NetWorkManager.getInstance().checkNetWork(new StateCallback() {
            public void getStateResult(int state) {
                NetWorkStateHelper.this.mNetState = state;
                LogUtils.d(NetWorkStateHelper.TAG, "check network result state = " + state);
                synchronized (NetWorkStateHelper.this.mLock) {
                    NetWorkStateHelper.this.mHasNotify = true;
                    NetWorkStateHelper.this.mLock.notify();
                }
            }
        });
        try {
            synchronized (this.mLock) {
                if (!this.mHasNotify) {
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            LogUtils.e(TAG, "network check is interrupted");
        }
        LogUtils.e(TAG, "network check is finished");
        return this.mNetState;
    }

    public static boolean isNetWorkConnected() {
        int netState = NetWorkManager.getInstance().getNetState();
        LogUtils.d(TAG, "isNetWorkConnected = " + netState);
        if (netState == 1 || netState == 2) {
            return true;
        }
        return false;
    }

    public static boolean isNoNetWorkOnBootUp() {
        String apiCode = DeviceCheckModel.getInstance().getApiCode();
        return "HTTP_ERR_-50".equals(apiCode) || "-100".equals(apiCode);
    }
}
