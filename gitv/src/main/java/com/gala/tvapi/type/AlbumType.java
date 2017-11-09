package com.gala.tvapi.type;

public enum AlbumType {
    ALBUM(1),
    VIDEO(0),
    PEOPLE(99),
    NONE(-1),
    OFFLINE(14),
    PLAYLIST(2);
    
    private int f156a;

    private AlbumType(int i) {
        this.f156a = i;
    }

    public final int getValue() {
        return this.f156a;
    }
}
