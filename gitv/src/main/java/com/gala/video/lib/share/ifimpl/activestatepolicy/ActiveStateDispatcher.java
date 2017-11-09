package com.gala.video.lib.share.ifimpl.activestatepolicy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.activestatepolicy.IActiveStateChangeListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.activestatepolicy.IActiveStateDispatcher.Wrapper;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ActiveStateDispatcher extends Wrapper {
    private static final int ACTIVE_STATE_INTERVAL = 240000;
    private static final int MSG_INACTIVE = 1;
    private static final String TAG = "ActiveStateDispatcher";
    private boolean mIsActive = true;
    private Set<IActiveStateChangeListener> mListeners = new CopyOnWriteArraySet();
    private TimeHandler mTimeHandler = new TimeHandler(Looper.getMainLooper());

    private class TimeHandler extends Handler {
        TimeHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                LogUtils.d(ActiveStateDispatcher.TAG, "turn into inactive state");
                ActiveStateDispatcher.this.turnToInActive();
                ActiveStateDispatcher.this.mIsActive = false;
            }
        }
    }

    public void resister(IActiveStateChangeListener listener) {
        this.mListeners.add(listener);
    }

    public void unResister(IActiveStateChangeListener listener) {
        this.mListeners.remove(listener);
    }

    public void notifyKeyEvent() {
        LogUtils.d(TAG, "notifyKeyEvent()");
        this.mTimeHandler.removeCallbacksAndMessages(null);
        this.mTimeHandler.sendEmptyMessageDelayed(1, 240000);
        if (!this.mIsActive) {
            this.mIsActive = true;
            turnToActive();
        }
    }

    public void turnToActive() {
        for (IActiveStateChangeListener listener : this.mListeners) {
            listener.turnToActive();
        }
    }

    public void turnToInActive() {
        for (IActiveStateChangeListener listener : this.mListeners) {
            listener.turnToInActive();
        }
    }
}
