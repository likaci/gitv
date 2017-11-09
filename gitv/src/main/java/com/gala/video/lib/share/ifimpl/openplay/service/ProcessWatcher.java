package com.gala.video.lib.share.ifimpl.openplay.service;

import android.os.Binder;
import android.util.SparseArray;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;

class ProcessWatcher implements IAccessWatcher {
    private static final String TAG = "ProcessWatcher";
    private final long mDuration;
    private final long mInterval;
    private final Object mLock = new Object();
    private final long mMaxCount;
    private final SparseArray<AccessToken> mPids = new SparseArray();

    public ProcessWatcher(long duration, long maxCount, long interval) {
        this.mDuration = duration;
        this.mMaxCount = maxCount;
        this.mInterval = interval;
    }

    private AccessToken getToken() {
        AccessToken token;
        int pid = Binder.getCallingPid();
        synchronized (this.mLock) {
            token = (AccessToken) this.mPids.get(pid);
        }
        if (token == null) {
            synchronized (this.mLock) {
                AccessToken temp = new AccessToken(this.mDuration, this.mMaxCount, this.mInterval);
                this.mPids.put(pid, temp);
                token = temp;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getToken() " + pid + " return " + token);
        }
        return token;
    }

    public boolean isAllowedAccess() {
        return getToken().isAllowedAccess();
    }

    public void increaseAccessCount() {
        getToken().increaseAccessCount();
    }

    public void replace(int oldPid) {
        int pid = Binder.getCallingPid();
        synchronized (this.mLock) {
            AccessToken token = (AccessToken) this.mPids.get(oldPid);
            if (token != null) {
                this.mPids.remove(oldPid);
                this.mPids.put(pid, token);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "replace(" + oldPid + ", " + pid + ")");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("ProcessWatcher@{size=");
        int size = this.mPids.size();
        sb.append(size);
        sb.append(",");
        for (int i = 0; i < size; i++) {
            AccessToken info = (AccessToken) this.mPids.valueAt(i);
            sb.append("[");
            sb.append(this.mPids.keyAt(i));
            sb.append(",");
            sb.append(info);
            sb.append(AlbumEnterFactory.SIGN_STR);
        }
        sb.append(")");
        return sb.toString();
    }
}
