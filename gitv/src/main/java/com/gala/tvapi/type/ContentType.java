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
    
    private int f199a;

    private ContentType(int v) {
        this.f199a = v;
    }

    public final int getValue() {
        return this.f199a;
    }
}
