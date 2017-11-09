package com.gala.video.lib.share.ifimpl.openplay.service;

import com.gala.video.lib.framework.core.utils.LogUtils;

class AccessToken {
    private static final String TAG = "AccessToken";
    private long mAccessCount;
    private long mAccessTime;
    private final long mDuration;
    private final long mInterval;
    private final long mMaxCount;

    public AccessToken(long duration, long maxCount, long interval) {
        this.mDuration = duration;
        this.mMaxCount = maxCount;
        this.mInterval = interval;
    }

    public synchronized boolean isAllowedAccess() {
        boolean allowed;
        long now = System.currentTimeMillis();
        long duration = now - this.mAccessTime;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isAllowedAccess() now = " + now + ", mMaxCount = " + this.mMaxCount);
        }
        if (duration > this.mDuration) {
            this.mAccessCount = 0;
            allowed = true;
        } else if (duration >= this.mInterval || duration < 0) {
            allowed = this.mAccessCount < this.mMaxCount;
        } else {
            allowed = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isAllowedAccess() count=" + this.mAccessCount + ", time=" + this.mAccessTime + ", return " + allowed);
        }
        return allowed;
    }

    public synchronized void increaseAccessCount() {
        this.mAccessTime = System.currentTimeMillis();
        this.mAccessCount++;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "increaseAccessCount() count=" + this.mAccessCount);
        }
    }

    public String toString() {
        return "AccessToken@{count=" + this.mAccessCount + ", time=" + this.mAccessTime + ", duration=" + this.mDuration + ", maxCount=" + this.mMaxCount + ", interval=" + this.mInterval + ")";
    }
}
