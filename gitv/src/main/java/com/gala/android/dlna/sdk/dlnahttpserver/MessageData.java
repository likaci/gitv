package com.gala.android.dlna.sdk.dlnahttpserver;

public class MessageData {
    private byte data;
    private long time;
    private String uuid;

    public MessageData() {
        this.uuid = null;
        this.time = 0;
        this.data = (byte) 0;
    }

    public MessageData(String uuid, long time, byte data) {
        this.uuid = uuid;
        this.time = time;
        this.data = data;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte getData() {
        return this.data;
    }

    public void setData(byte data) {
        this.data = data;
    }
}
