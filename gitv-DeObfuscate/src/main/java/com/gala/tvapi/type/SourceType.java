package com.gala.tvapi.type;

public enum SourceType {
    GALA_PPS(1),
    GALA(2),
    PPS(3);
    
    private int f1151a;

    private SourceType(int value) {
        this.f1151a = value;
    }

    public final int getValue() {
        return this.f1151a;
    }
}
