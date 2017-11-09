package com.gala.video.lib.share.ifimpl.openplay.service.feature;

import com.gala.video.lib.framework.core.utils.LogUtils;

public class NetworkHolder {
    private static final String TAG = "NetworkHolder";
    private boolean mNetworkValid = true;

    public void setNetworkValid(boolean valid) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setNetworkValid(" + valid + ")");
        }
        this.mNetworkValid = valid;
    }

    public boolean isNetworkValid() {
        return this.mNetworkValid;
    }
}
