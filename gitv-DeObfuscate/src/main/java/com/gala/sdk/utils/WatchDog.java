package com.gala.sdk.utils;

import org.cybergarage.soap.SOAP;

public class WatchDog {
    private long f717a;
    private final String f718a;
    private long f719b;
    private String f720b;

    public WatchDog(String name) {
        this.f718a = name;
    }

    public synchronized void start(String lable) {
        this.f720b = lable;
        this.f717a = System.currentTimeMillis();
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/WatchDog", "[" + this.f718a + "]start() " + this.f720b + SOAP.DELIM + this.f717a);
        }
    }

    public synchronized void stop() {
        this.f719b = System.currentTimeMillis();
        long j = this.f719b - this.f717a;
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/WatchDog", "[" + this.f718a + "]stop()   " + this.f720b + SOAP.DELIM + this.f719b + ", consumed:" + j);
        }
        this.f720b = null;
    }
}
