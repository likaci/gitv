package com.mcto.ads.internal.thirdparty;

public enum TrackingProvider {
    ADMASTER("adMaster"),
    MIAOZHEN("miaozhen"),
    NIELSEN("nielsen"),
    CTR("ctr"),
    DEFAULT("");
    
    private final String value;

    private TrackingProvider(String value) {
        this.value = value;
    }
}
