package com.gala.tvapi.type;

public enum ChannelType {
    REAL_CHANNEL(0),
    TOPIC_CHANNEL(1),
    VIRTUAL_CHANNEL(2),
    FUNCTION_CHANNEL(3);
    
    private int f1118a;

    private ChannelType(int v) {
        this.f1118a = v;
    }

    public final int getValue() {
        return this.f1118a;
    }
}
