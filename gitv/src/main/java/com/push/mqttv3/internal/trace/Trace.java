package com.push.mqttv3.internal.trace;

import com.push.mqttv3.internal.ExceptionHelper;

public class Trace {
    public static final byte FINE = (byte) 1;
    public static final byte FINER = (byte) 2;
    public static final byte FINEST = (byte) 3;
    private static short count = (short) 0;
    private static TraceDestination destination;
    private boolean on;
    private String resource;
    private short source;

    public static synchronized Trace getTrace(String resource) {
        Trace trace;
        synchronized (Trace.class) {
            if (destination == null && ExceptionHelper.isClassAvailable("java.io.File")) {
                try {
                    destination = (TraceDestination) Class.forName("TraceFileDestination").newInstance();
                } catch (Exception e) {
                }
            }
            trace = new Trace(count, resource);
            count = (short) (count + 1);
        }
        return trace;
    }

    private Trace(short source, String resource) {
        this.source = source;
        this.resource = resource;
        boolean z = destination != null && destination.isEnabled(this.resource);
        this.on = z;
    }

    public boolean isOn() {
        return this.on;
    }

    public void traceEntry(byte level, int id) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 1, level, id, null, null));
        }
    }

    public void traceExit(byte level, int id) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 2, level, id, null, null));
        }
    }

    public void traceBreak(byte level, int id) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 3, level, id, null, null));
        }
    }

    public void traceCatch(byte level, int id, Throwable throwable) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 4, level, id, throwable, null));
        }
    }

    public void trace(byte level, int id) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 5, level, id, null, null));
        }
    }

    public void trace(byte level, int id, Object[] inserts) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 5, level, id, null, inserts));
        }
    }

    public void trace(byte level, int id, Object[] inserts, Throwable throwable) {
        if (this.on) {
            destination.write(new TracePoint(this.source, (byte) 5, level, id, throwable, inserts));
        }
    }
}
