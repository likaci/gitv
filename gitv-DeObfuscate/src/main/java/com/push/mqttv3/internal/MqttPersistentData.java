package com.push.mqttv3.internal;

import com.push.mqttv3.MqttPersistable;

public class MqttPersistentData implements MqttPersistable {
    private int hLength = 0;
    private int hOffset = 0;
    private byte[] header = null;
    private String key = null;
    private int pLength = 0;
    private int pOffset = 0;
    private byte[] payload = null;

    public MqttPersistentData(String key, byte[] header, int hOffset, int hLength, byte[] payload, int pOffset, int pLength) {
        this.key = key;
        this.header = header;
        this.hOffset = hOffset;
        this.hLength = hLength;
        this.payload = payload;
        this.pOffset = pOffset;
        this.pLength = pLength;
    }

    public String getKey() {
        return this.key;
    }

    public byte[] getHeaderBytes() {
        return this.header;
    }

    public int getHeaderLength() {
        return this.hLength;
    }

    public int getHeaderOffset() {
        return this.hOffset;
    }

    public byte[] getPayloadBytes() {
        return this.payload;
    }

    public int getPayloadLength() {
        if (this.payload == null) {
            return 0;
        }
        return this.pLength;
    }

    public int getPayloadOffset() {
        return this.pOffset;
    }
}
