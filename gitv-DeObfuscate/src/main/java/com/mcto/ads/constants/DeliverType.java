package com.mcto.ads.constants;

import android.util.Log;

public enum DeliverType {
    DELIVER_UNSUPPORTED(0),
    DELIVER_PMP(1),
    DELIVER_TRUEVIEW(2),
    DELIVER_VIP_AD(3),
    DELIVER_ORIGINAL_ROLL(4);
    
    private final int value;

    private DeliverType(int value) {
        this.value = value;
    }

    public static DeliverType build(int value) {
        switch (value) {
            case 0:
                return DELIVER_UNSUPPORTED;
            case 1:
                return DELIVER_PMP;
            case 2:
                return DELIVER_TRUEVIEW;
            case 3:
                return DELIVER_VIP_AD;
            case 4:
                return DELIVER_ORIGINAL_ROLL;
            default:
                Log.e("a71_ads_client", "DeliverType build(): invalid deliver type: " + value);
                return DELIVER_UNSUPPORTED;
        }
    }

    public int value() {
        return this.value;
    }
}
