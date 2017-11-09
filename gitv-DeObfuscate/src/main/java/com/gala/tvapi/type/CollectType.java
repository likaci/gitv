package com.gala.tvapi.type;

public enum CollectType {
    SERIES(1),
    SINGLE(7),
    SOURCE(2);
    
    private int f1120a;

    private CollectType(int subType) {
        this.f1120a = 1;
        this.f1120a = subType;
    }

    public final int getValue() {
        return this.f1120a;
    }
}
