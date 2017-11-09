package com.gala.sdk.player;

public enum SourceType {
    COMMON,
    PUSH,
    OUTSIDE,
    VOD,
    DAILY_NEWS,
    BO_DAN,
    LIVE,
    CAROUSEL,
    STARTUP_AD,
    DETAIL_TRAILERS,
    DETAIL_RELATED;

    public static SourceType getByInt(int index) {
        switch (index) {
            case 0:
                return COMMON;
            case 1:
                return PUSH;
            case 2:
                return OUTSIDE;
            case 3:
                return VOD;
            case 4:
                return DAILY_NEWS;
            case 6:
                return BO_DAN;
            case 8:
                return LIVE;
            case 13:
                return CAROUSEL;
            case 14:
                return STARTUP_AD;
            case 15:
                return DETAIL_TRAILERS;
            default:
                return PUSH;
        }
    }
}
