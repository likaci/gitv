package com.push.mqttv3.internal;

public interface ExceptionListener {
    void notifyCommsException(Exception exception);
}
