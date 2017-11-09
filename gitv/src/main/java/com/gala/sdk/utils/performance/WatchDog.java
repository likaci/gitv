package com.gala.sdk.utils.performance;

import com.gala.sdk.utils.MyLogUtils;
import org.cybergarage.soap.SOAP;

public class WatchDog {
    private long a;
    private String f376a;
    private long b;
    private String f377b;

    public WatchDog(String name) {
        this.f376a = name;
    }

    public void start(String lable) {
        this.f377b = lable;
        this.a = System.currentTimeMillis();
        MyLogUtils.d("Player/Lib/Utils/WatchDog", "[" + this.f376a + "]start() " + this.f377b + SOAP.DELIM + this.a);
    }

    public void stop() {
        this.b = System.currentTimeMillis();
        MyLogUtils.d("Player/Lib/Utils/WatchDog", "[" + this.f376a + "]stop()   " + this.f377b + SOAP.DELIM + this.b + ", consumed:" + (this.b - this.a));
        this.f377b = null;
    }
}
