package com.gala.tvapi.type;

public enum CollectType {
    SERIES(1),
    SINGLE(7),
    SOURCE(2);
    
    private int f530a;

    private CollectType(int subType) {
        this.f530a = 1;
        this.f530a = subType;
    }

    public final int getValue() {
        return this.f530a;
    }
}
