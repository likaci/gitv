package com.gala.tvapi.type;

public enum PropType {
    PLAYLIST(1),
    FUNCTION(2),
    TVTAG(3),
    VIRCHANNEL(4),
    CHANNEL(5);
    
    private int f203a;

    private PropType(int value) {
        this.f203a = value;
    }

    public final int getValue() {
        return this.f203a;
    }
}
