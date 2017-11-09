package com.gala.tvapi.type;

public enum ChannelLayoutType {
    HORIZONTAL(1),
    VERTICAL(2),
    INFORMATION(3),
    MUSIC(4),
    PLAY(5);
    
    private int f1116a;

    private ChannelLayoutType(int v) {
        this.f1116a = v;
    }

    public final int getValue() {
        return this.f1116a;
    }
}
