package com.mcto.ads.constants;

public enum AdEvent {
    AD_EVENT_IMPRESSION("0"),
    AD_EVENT_START("1"),
    AD_EVENT_PAUSE("2"),
    AD_EVENT_RESUME("3"),
    AD_EVENT_STOP("4"),
    AD_EVENT_CLICK("5");
    
    private final String value;

    private AdEvent(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
