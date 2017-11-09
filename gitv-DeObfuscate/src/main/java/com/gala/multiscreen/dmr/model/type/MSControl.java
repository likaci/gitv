package com.gala.multiscreen.dmr.model.type;

import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;

public enum MSControl {
    DEFAULT(""),
    PUSHVIDEO(MultiScreenParams.DLNA_PHONE_CONTROLL_PHONE_TV_PUSHVIDEO),
    ACTION("action"),
    PHONE_SYNC("phone_sync");
    
    private String value;

    private MSControl(String value) {
        this.value = "";
        setValue(value);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static MSControl findControl(String control) {
        for (MSControl mControl : values()) {
            if (mControl.getValue().equals(control)) {
                return mControl;
            }
        }
        return DEFAULT;
    }
}
