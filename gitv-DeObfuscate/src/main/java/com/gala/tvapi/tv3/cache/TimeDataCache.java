package com.gala.tvapi.tv3.cache;

public class TimeDataCache extends ApiCache {
    public void putServiceTime(long time) {
        ApiCache.m731a("service_time", time);
    }

    public long getServiceTime() {
        return ApiCache.m729a("service_time");
    }

    public void putDeviceTime(long time) {
        ApiCache.m731a("device_time", time);
    }

    public long getDeviceTime() {
        return ApiCache.m729a("device_time");
    }
}
