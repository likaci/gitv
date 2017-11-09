package com.gala.video.lib.share.ifimpl.openplay.service;

import com.gala.video.lib.framework.core.utils.LogUtils;

public class GlobalWatcher implements IAccessWatcher {
    private static final String TAG = "GlobalWatcher";
    private final AccessToken mToken;

    public GlobalWatcher(long duration, long maxCount, long interval) {
        this.mToken = new AccessToken(duration, maxCount, interval);
    }

    public boolean isAllowedAccess() {
        return this.mToken.isAllowedAccess();
    }

    public void increaseAccessCount() {
        this.mToken.increaseAccessCount();
    }

    public void replace(int oldPid) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "replace(" + oldPid + ")");
        }
    }

    public String toString() {
        return "GlobalWatcher@{token=" + this.mToken + ")";
    }
}
