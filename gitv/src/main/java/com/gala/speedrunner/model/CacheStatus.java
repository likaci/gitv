package com.gala.speedrunner.model;

public class CacheStatus {
    public int avg_speed;
    public int code;
    public String host;
    public Ping ping;
    public int state;
    public String url;

    public void setPing(Ping ping) {
        this.ping = ping;
    }

    public Ping getPing() {
        return this.ping;
    }
}
