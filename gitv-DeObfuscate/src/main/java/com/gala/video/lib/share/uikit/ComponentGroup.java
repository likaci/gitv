package com.gala.video.lib.share.uikit;

import com.gala.video.lib.share.uikit.utils.LogUtils;

public abstract class ComponentGroup extends Component {
    private static final int STATE_DESTROY = 2;
    private static final int STATE_NONE = -1;
    private static final int STATE_START = 0;
    private static final int STATE_STOP = 1;
    private int mState = -1;

    protected abstract void onDestroy();

    protected abstract void onHide();

    protected abstract void onShow();

    protected abstract void onStart();

    protected abstract void onStop();

    public void start() {
        if (this.mState != 0) {
            this.mState = 0;
            onStart();
        }
        show();
    }

    public void show() {
        LogUtils.m1593i("ComponentGroup", "ComponentGroup show isStart() = ", Boolean.valueOf(isStart()));
        if (isStart()) {
            onShow();
        }
    }

    public void stop() {
        if (this.mState == 0) {
            this.mState = 1;
            onStop();
        }
        hide();
    }

    public void hide() {
        onHide();
    }

    public void destroy() {
        if (this.mState != 2) {
            this.mState = 2;
            stop();
            onDestroy();
        }
    }

    public int getState() {
        return this.mState;
    }

    public boolean isStart() {
        return this.mState == 0;
    }

    public boolean isDestroy() {
        return this.mState == 2;
    }
}
