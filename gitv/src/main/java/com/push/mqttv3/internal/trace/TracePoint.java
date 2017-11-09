package com.push.mqttv3.internal.trace;

public class TracePoint {
    public static final byte BREAK = (byte) 3;
    public static final byte CATCH = (byte) 4;
    public static final byte ENTRY = (byte) 1;
    public static final byte EXIT = (byte) 2;
    public static final byte OTHER = (byte) 5;
    public short id;
    public Object[] inserts;
    public byte level;
    public short source;
    public String[] stacktrace;
    public String threadName;
    public Throwable throwable;
    public long timestamp;
    public byte type;

    public TracePoint(short source, byte type, byte level, int id, Throwable throwable, Object[] inserts) {
        this.source = source;
        this.threadName = Thread.currentThread().getName();
        this.timestamp = System.currentTimeMillis();
        this.type = type;
        this.level = level;
        this.id = (short) id;
        this.throwable = throwable;
        this.inserts = inserts;
    }
}
