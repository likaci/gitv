package com.gala.video.app.player.controller;

import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class MyPlayerStateListenerProxy implements OnPlayerStateChangedListener {
    private static final String TAG = "MyPlayerStateListenerProxy";
    private OnPlayerStateChangedListener mListener;

    public MyPlayerStateListenerProxy(OnPlayerStateChangedListener listener) {
        this.mListener = listener;
    }

    public void onScreenModeSwitched(ScreenMode arg0) {
        if (this.mListener != null) {
            this.mListener.onScreenModeSwitched(arg0);
        }
    }

    public void onAdStarted() {
        if (this.mListener != null) {
            this.mListener.onAdStarted();
        }
    }

    public boolean onError(IVideo arg0, ISdkError arg1) {
        if (this.mListener != null) {
            return this.mListener.onError(arg0, arg1);
        }
        return false;
    }

    public void onPlaybackFinished() {
        if (this.mListener != null) {
            this.mListener.onPlaybackFinished();
        }
    }

    public void onVideoStarted(IVideo arg0) {
        if (this.mListener != null) {
            this.mListener.onVideoStarted(arg0);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onVideoStarted: pingback delay send duration set to 0 !");
        }
    }

    public void onVideoSwitched(IVideo arg0, int type) {
        if (this.mListener != null) {
            this.mListener.onVideoSwitched(arg0, type);
        }
    }

    public void onAdEnd() {
        if (this.mListener != null) {
            this.mListener.onAdStarted();
        }
    }

    public void onPrepared() {
        if (this.mListener != null) {
            this.mListener.onPrepared();
        }
    }
}
