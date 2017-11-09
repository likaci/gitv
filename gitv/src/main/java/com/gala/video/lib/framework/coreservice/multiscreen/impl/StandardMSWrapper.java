package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import com.gala.multiscreen.dmr.IStandardMSCallback;
import com.gala.multiscreen.dmr.model.MSMessage.PlayKind;
import com.gala.multiscreen.dmr.model.MSMessage.PushKind;
import com.gala.multiscreen.dmr.model.MSMessage.SeekTimeKind;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSStandListener;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSStandardOperate;

class StandardMSWrapper implements IStandardMSCallback, IMSStandardOperate {
    private IMSStandListener mStandCallback;

    StandardMSWrapper() {
    }

    public void setMSStandardListener(IMSStandListener callback) {
        this.mStandCallback = callback;
    }

    private boolean isEnable() {
        return this.mStandCallback != null;
    }

    public void onNotify(PlayKind kind, String message) {
        if (isEnable()) {
            this.mStandCallback.onNotify(kind, message);
        }
    }

    public void onSeek(SeekTimeKind kind, long time) {
        if (isEnable()) {
            this.mStandCallback.onSeek(kind, time);
        }
    }

    public void setAVTransportURI(PushKind kind, String url, boolean isNext) {
        if (isEnable()) {
            this.mStandCallback.setAVTransportURI(kind, url, isNext);
        }
    }
}
