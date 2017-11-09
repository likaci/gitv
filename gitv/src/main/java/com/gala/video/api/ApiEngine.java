package com.gala.video.api;

public class ApiEngine {
    private static final ApiEngine a = new ApiEngine();
    private String f505a = "";
    private String b = "";

    public static ApiEngine get() {
        return a;
    }

    private ApiEngine() {
    }

    public void setClientVersion(String v) {
        this.f505a = v;
    }

    public void setUserAgent(String userAgent) {
        this.b = userAgent;
    }

    public String getUserAgent() {
        return this.b + "_ITV_" + this.f505a;
    }
}
