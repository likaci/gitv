package com.gala.tvapi.type;

public enum AlbumListLayoutType {
    HORIZONTAL(1),
    VERTICAL(2),
    MIXED(3),
    OTHER(4);
    
    private int f529a;

    private AlbumListLayoutType(int v) {
        this.f529a = v;
    }

    public final int getValue() {
        return this.f529a;
    }
}
