package com.gala.speedrunner.model;

public class PlayResult {
    public Access access_vrs;
    public int auth_succ;
    public CacheStatus cache_status;
    public Dns dns;
    public int dns_succ;
    public int get_key_succ;
    public int step;
    public int url_valid;

    public void setCacheStatus(CacheStatus cache) {
        this.cache_status = cache;
    }

    public CacheStatus getCache_Status() {
        return this.cache_status;
    }

    public void setDns(Dns dns) {
        this.dns = dns;
    }

    public Dns getDns() {
        return this.dns;
    }

    public Access getAccessInfo() {
        return this.access_vrs;
    }
}
