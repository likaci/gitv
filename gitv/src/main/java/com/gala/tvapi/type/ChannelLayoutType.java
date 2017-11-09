package com.gala.tvapi.type;

public enum ChannelLayoutType {
    HORIZONTAL(1),
    VERTICAL(2),
    INFORMATION(3),
    MUSIC(4),
    PLAY(5);
    
    private int f202a;

    private ChannelLayoutType(int v) {
        this.f202a = v;
    }

    public final int getValue() {
        return this.f202a;
    }
}
