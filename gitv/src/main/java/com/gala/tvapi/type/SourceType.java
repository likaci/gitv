package com.gala.tvapi.type;

public enum SourceType {
    GALA_PPS(1),
    GALA(2),
    PPS(3);
    
    private int f534a;

    private SourceType(int value) {
        this.f534a = value;
    }

    public final int getValue() {
        return this.f534a;
    }
}
