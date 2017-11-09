package com.gala.tvapi.type;

public enum AlbumListLayoutType {
    HORIZONTAL(1),
    VERTICAL(2),
    MIXED(3),
    OTHER(4);
    
    private int f1111a;

    private AlbumListLayoutType(int v) {
        this.f1111a = v;
    }

    public final int getValue() {
        return this.f1111a;
    }
}
