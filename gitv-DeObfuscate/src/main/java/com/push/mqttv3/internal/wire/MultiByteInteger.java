package com.push.mqttv3.internal.wire;

public class MultiByteInteger {
    private int length;
    private long value;

    public MultiByteInteger(long value) {
        this(value, -1);
    }

    public MultiByteInteger(long value, int length) {
        this.value = value;
        this.length = length;
    }

    public int getEncodedLength() {
        return this.length;
    }

    public long getValue() {
        return this.value;
    }
}
