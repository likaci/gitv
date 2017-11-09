package com.gala.sdk.utils.performance;

import com.gala.sdk.utils.MyLogUtils;
import org.cybergarage.soap.SOAP;

public class WatchDog {
    private long f743a;
    private String f744a;
    private long f745b;
    private String f746b;

    public WatchDog(String name) {
        this.f744a = name;
    }

    public void start(String lable) {
        this.f746b = lable;
        this.f743a = System.currentTimeMillis();
        MyLogUtils.m462d("Player/Lib/Utils/WatchDog", "[" + this.f744a + "]start() " + this.f746b + SOAP.DELIM + this.f743a);
    }

    public void stop() {
        this.f745b = System.currentTimeMillis();
        MyLogUtils.m462d("Player/Lib/Utils/WatchDog", "[" + this.f744a + "]stop()   " + this.f746b + SOAP.DELIM + this.f745b + ", consumed:" + (this.f745b - this.f743a));
        this.f746b = null;
    }
}
