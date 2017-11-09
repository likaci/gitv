package com.push.mqttv3.internal.trace;

public interface TraceDestination {
    boolean isEnabled(String str);

    void write(TracePoint tracePoint);
}
