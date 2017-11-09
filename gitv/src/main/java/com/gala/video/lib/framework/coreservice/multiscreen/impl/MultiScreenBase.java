package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import com.gala.multiscreen.dmr.MultiScreenHelper;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.model.msg.DlnaMessage;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnKeyChanged;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnNotifyEvent;

class MultiScreenBase {
    private static final String TAG = "MultiScreenBase";
    protected boolean mDlnaDebugEnabled = true;
    protected GalaMSWrapper mGalaMSWrapper;
    protected MultiScreenHelper mHelper = null;
    protected boolean mIsPhoneKey = false;
    protected OnKeyChanged mOnKeyChangedListener = new OnKeyChanged() {
        public void onEvent(int keycode) {
            MultiScreenBase.this.mIsPhoneKey = true;
        }
    };
    protected OnNotifyEvent mOnNotifyListener = new OnNotifyEvent() {
        public void onEvent(RequestKind kind, String message) {
            if (kind == RequestKind.ONLINE) {
                MultiScreenBase.this.mPhoneConnected = true;
            } else if (kind == RequestKind.OFFLINE) {
                MultiScreenBase.this.mPhoneConnected = false;
            }
        }
    };
    protected boolean mPhoneConnected = false;
    protected StandardMSWrapper mStandardMSWrapper;
    protected String mTvVersion = null;

    MultiScreenBase() {
    }

    public void setDlnaLogEnabled(boolean enabled) {
        this.mDlnaDebugEnabled = enabled;
        if (this.mHelper != null) {
            this.mHelper.setDlnaLogEnabled(enabled);
        }
    }

    public void setTvVersion(String version) {
        this.mTvVersion = version;
        if (this.mHelper != null) {
            this.mHelper.setTvVersionString(version);
        }
    }

    public void sendMessage(DlnaMessage msg) {
        if (this.mHelper != null) {
            this.mHelper.sendMessage(msg);
        }
    }

    public void onSeekFinish() {
        if (this.mHelper != null) {
            this.mHelper.onSeekFinish();
        }
    }

    public void setDeviceName(String name) {
        if (this.mHelper != null) {
            this.mHelper.changeName(name);
        }
    }

    public boolean isPhoneKey() {
        return this.mIsPhoneKey;
    }

    public void setIsPhoneKey(boolean v) {
        this.mIsPhoneKey = v;
    }

    public boolean isPhoneConnected() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isPhoneConnected " + this.mPhoneConnected);
        }
        return this.mPhoneConnected;
    }
}
