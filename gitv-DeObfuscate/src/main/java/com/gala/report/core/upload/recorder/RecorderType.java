package com.gala.report.core.upload.recorder;

import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifmanager.InterfaceKey;

public enum RecorderType {
    _CRASH("crash"),
    _FEEDBACK_AUTO("feedback_auto"),
    _FEEDBACK(InterfaceKey.EPG_FB),
    _ERROR(MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR);
    
    private final String mShortName;

    private RecorderType(String shortName) {
        this.mShortName = shortName;
    }

    public String toString() {
        return this.mShortName;
    }
}
