package com.gala.multiscreen.dmr.model.type;

import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;

public enum MSType {
    DEFAULT(""),
    CONTROL(MultiScreenParams.DLNA_PHONE_CONTROLL),
    SYNC("sync"),
    SEEK(MultiScreenParams.DLNA_PHONE_CONTROLL_SEEK),
    GETPOSITION("getposition"),
    CHANGE_RES("changeres"),
    PLAYLIST("playlist");
    
    private String type;

    private MSType(String type) {
        setType(type);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static MSType findType(String value) {
        for (MSType type : values()) {
            if (type.getType().equals(value)) {
                return type;
            }
        }
        return DEFAULT;
    }
}
