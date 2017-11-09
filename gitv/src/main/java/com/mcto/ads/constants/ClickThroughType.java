package com.mcto.ads.constants;

public enum ClickThroughType {
    DEFAULT("0"),
    WEBVIEW("1"),
    BROWSER("2"),
    VIP("3"),
    DOWNLOAD("4"),
    VIDEO("5"),
    PRELOAD("6"),
    GAMECENTER("7"),
    MOVIECENTER("8"),
    QIXIU("9"),
    INNER_START("10"),
    DIRECT_DOWNLOAD("11"),
    IMAGE("12"),
    CAROUSEL_STATION("13"),
    REGISTRATION("67");
    
    private final String value;

    private ClickThroughType(String value) {
        this.value = value;
    }

    public static ClickThroughType build(String value) {
        if ("1".equals(value)) {
            return WEBVIEW;
        }
        if ("2".equals(value)) {
            return BROWSER;
        }
        if ("3".equals(value)) {
            return VIP;
        }
        if ("4".equals(value)) {
            return DOWNLOAD;
        }
        if ("5".equals(value)) {
            return VIDEO;
        }
        if ("6".equals(value)) {
            return PRELOAD;
        }
        if ("7".equals(value)) {
            return GAMECENTER;
        }
        if ("8".equals(value)) {
            return MOVIECENTER;
        }
        if ("9".equals(value)) {
            return QIXIU;
        }
        if ("10".equals(value)) {
            return INNER_START;
        }
        if ("11".equals(value)) {
            return DIRECT_DOWNLOAD;
        }
        if ("12".equals(value)) {
            return IMAGE;
        }
        if ("13".equals(value)) {
            return CAROUSEL_STATION;
        }
        if ("67".equals(value)) {
            return REGISTRATION;
        }
        return DEFAULT;
    }

    public String value() {
        return this.value;
    }
}
