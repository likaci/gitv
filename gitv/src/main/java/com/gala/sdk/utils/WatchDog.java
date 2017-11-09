package com.gala.sdk.utils;

import org.cybergarage.soap.SOAP;

public class WatchDog {
    private long a;
    private final String f364a;
    private long b;
    private String f365b;

    public WatchDog(String name) {
        this.f364a = name;
    }

    public synchronized void start(String lable) {
        this.f365b = lable;
        this.a = System.currentTimeMillis();
        if (Log.DEBUG) {
            Log.d("PlayerUtils/WatchDog", "[" + this.f364a + "]start() " + this.f365b + SOAP.DELIM + this.a);
        }
    }

    public synchronized void stop() {
        this.b = System.currentTimeMillis();
        long j = this.b - this.a;
        if (Log.DEBUG) {
            Log.d("PlayerUtils/WatchDog", "[" + this.f364a + "]stop()   " + this.f365b + SOAP.DELIM + this.b + ", consumed:" + j);
        }
        this.f365b = null;
    }
}
