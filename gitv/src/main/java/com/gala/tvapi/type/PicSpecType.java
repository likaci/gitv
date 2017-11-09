package com.gala.tvapi.type;

public enum PicSpecType {
    LANDSCAPE(1),
    PORTRAIT(2),
    MIXING(3),
    OTHER(4);
    
    private int f200a;

    private PicSpecType(int v) {
        this.f200a = v;
    }

    public final int getValue() {
        return this.f200a;
    }
}
