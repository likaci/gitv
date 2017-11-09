package com.mcto.ads.constants;

public enum AdCard {
    AD_CARD_NATIVE_VIDEO("0"),
    AD_CARD_MOBILE_FLOW("1"),
    AD_CARD_NATIVE_MUTIL_IMAGE("3"),
    AD_CARD_NATIVE_IMAGE("4"),
    AD_CARD_TV_BANNER("5"),
    AD_CARD_HEADLINE_NATIVE_IMAGE("6");
    
    private final String value;

    private AdCard(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
