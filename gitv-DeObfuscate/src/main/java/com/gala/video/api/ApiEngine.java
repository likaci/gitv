package com.gala.video.api;

public class ApiEngine {
    private static final ApiEngine f1840a = new ApiEngine();
    private String f1841a = "";
    private String f1842b = "";

    public static ApiEngine get() {
        return f1840a;
    }

    private ApiEngine() {
    }

    public void setClientVersion(String v) {
        this.f1841a = v;
    }

    public void setUserAgent(String userAgent) {
        this.f1842b = userAgent;
    }

    public String getUserAgent() {
        return this.f1842b + "_ITV_" + this.f1841a;
    }
}
