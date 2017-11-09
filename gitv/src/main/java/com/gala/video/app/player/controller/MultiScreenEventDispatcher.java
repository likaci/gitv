package com.gala.video.app.player.controller;

import com.gala.multiscreen.dmr.model.msg.Notify;

public class MultiScreenEventDispatcher {
    private static final String TAG = "Detail/Controller/MultiScreenEventDispatcher";
    private static MultiScreenEventDispatcher sInstance;
    private IDetailMultiListener mListener;

    private MultiScreenEventDispatcher() {
    }

    public static synchronized MultiScreenEventDispatcher instance() {
        MultiScreenEventDispatcher multiScreenEventDispatcher;
        synchronized (MultiScreenEventDispatcher.class) {
            if (sInstance == null) {
                sInstance = new MultiScreenEventDispatcher();
            }
            multiScreenEventDispatcher = sInstance;
        }
        return multiScreenEventDispatcher;
    }

    public void register(IDetailMultiListener listener) {
        if (listener != null) {
            this.mListener = listener;
        }
    }

    public void unregister() {
        this.mListener = null;
    }

    public static synchronized void release() {
        synchronized (MultiScreenEventDispatcher.class) {
            sInstance = null;
        }
    }

    public Notify onPhoneSync() {
        if (this.mListener != null) {
            return this.mListener.onPhoneSync();
        }
        return null;
    }

    public boolean onSeekChanged(long newPosition) {
        return this.mListener != null && this.mListener.onSeekChanged(newPosition);
    }

    public boolean onResolutionChanged(String newRes) {
        return this.mListener != null && this.mListener.onResolutionChanged(newRes);
    }

    public long getPlayPosition() {
        if (this.mListener != null) {
            return this.mListener.getPlayPosition();
        }
        return 0;
    }

    public boolean onKeyChanged(int keycode) {
        return this.mListener != null && this.mListener.onKeyChanged(keycode);
    }
}
