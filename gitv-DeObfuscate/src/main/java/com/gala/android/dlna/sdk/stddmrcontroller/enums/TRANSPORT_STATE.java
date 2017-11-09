package com.gala.android.dlna.sdk.stddmrcontroller.enums;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;

public enum TRANSPORT_STATE {
    STOPPED,
    PLAYING,
    TRANSITIONING,
    PAUSED_PLAYBACK,
    PAUSED_RECORDING,
    RECORDING,
    NO_MEDIA_PRESENT;

    public static boolean isState(String str) {
        if (Util.isEmpty(str)) {
            return false;
        }
        try {
            if (valueOf(str) != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
