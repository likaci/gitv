package com.gala.tvapi.type;

public enum ContentType {
    FEATURE_FILM(1),
    SPECIAL(2),
    PREVUE(3),
    TRAILER(4),
    TITBIT(5),
    PROPAGANDA(6),
    CLIP(7),
    OTHER(25);
    
    private int f1122a;

    private ContentType(int v) {
        this.f1122a = v;
    }

    public final int getValue() {
        return this.f1122a;
    }
}
