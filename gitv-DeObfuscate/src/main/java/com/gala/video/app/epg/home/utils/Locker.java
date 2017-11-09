package com.gala.video.app.epg.home.utils;

import com.gala.video.lib.framework.core.utils.LogUtils;

public class Locker {
    private static final String TAG = "home/locker";
    private boolean mHasNotify = false;
    private final Object mLock = new Object();

    public void complete() {
        synchronized (this.mLock) {
            this.mHasNotify = true;
            this.mLock.notify();
        }
    }

    public boolean takeOrWait() {
        try {
            synchronized (this.mLock) {
                if (!this.mHasNotify) {
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            LogUtils.m1571e(TAG, "network check is interrupted");
        }
        return true;
    }

    public boolean takeOrWait(long timeOut) {
        try {
            synchronized (this.mLock) {
                if (!this.mHasNotify) {
                    this.mLock.wait(timeOut);
                }
            }
            return this.mHasNotify;
        } catch (InterruptedException e) {
            try {
                LogUtils.m1571e(TAG, "network check is interrupted");
                return this.mHasNotify;
            } catch (Throwable th) {
                return this.mHasNotify;
            }
        }
    }
}
