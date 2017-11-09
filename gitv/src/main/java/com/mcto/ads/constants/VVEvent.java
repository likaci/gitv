package com.mcto.ads.constants;

public enum VVEvent {
    START(0),
    PAUSE(1),
    RESUME(2),
    COMPLETE(3);
    
    private final int value;

    private VVEvent(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
