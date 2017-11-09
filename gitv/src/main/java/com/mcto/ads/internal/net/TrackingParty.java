package com.mcto.ads.internal.net;

public enum TrackingParty {
    DEFAULT("-1"),
    THIRD("0"),
    CUPID("1"),
    ADX("2");
    
    private final String value;

    private TrackingParty(String value) {
        this.value = value;
    }

    public static TrackingParty build(String value) {
        if ("0".equals(value)) {
            return THIRD;
        }
        if ("1".equals(value)) {
            return CUPID;
        }
        if ("2".equals(value)) {
            return ADX;
        }
        return DEFAULT;
    }

    public String value() {
        return this.value;
    }
}
