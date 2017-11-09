package com.push.mqttv3.internal.wire;

public class UnsignedShort {
    private int value;

    public UnsignedShort(int value) {
        this.value = value;
    }

    public int intValue() {
        return this.value;
    }
}
