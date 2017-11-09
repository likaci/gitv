package com.mcto.ads.constants;

public enum ClickArea {
    AD_CLICK_AREA_BUTTON("button"),
    AD_CLICK_AREA_EXT_BUTTON("ext_button"),
    AD_CLICK_AREA_GRAPHIC("graphic"),
    AD_CLICK_AREA_PORTRAIT("portrait"),
    AD_CLICK_AREA_ACCOUNT("account"),
    AD_CLICK_AREA_COMMENT("comment"),
    AD_CLICK_AREA_NEGTIVE("negtive"),
    AD_CLICK_AREA_PLAYER("player");
    
    private final String value;

    private ClickArea(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
