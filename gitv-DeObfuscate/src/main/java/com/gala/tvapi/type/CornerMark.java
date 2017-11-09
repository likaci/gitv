package com.gala.tvapi.type;

public enum CornerMark {
    CORNERMARK_NO(0),
    CORNERMARK_720P(1),
    CORNERMARK_H265(2),
    CORNERMARK_1080P(3),
    CORNERMARK_DOLBY(4),
    CORNERMARK_3D(5),
    CORNERMARK_4K(6),
    CORNERMARK_EXCLUSIVEPLAY(7),
    CORNERMARK_INDIVIDEMAND(8),
    CORNERMARK_VIP(9),
    CORNERMARK_LIVE(10);
    
    private int f1124a;

    private CornerMark(int i) {
        this.f1124a = 0;
        this.f1124a = i;
    }

    public final int getValue() {
        return this.f1124a;
    }
}
