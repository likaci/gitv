package com.gala.tvapi.tv3.cache;

public class TimeDataCache extends ApiCache {
    public void putServiceTime(long time) {
        ApiCache.a("service_time", time);
    }

    public long getServiceTime() {
        return ApiCache.a("service_time");
    }

    public void putDeviceTime(long time) {
        ApiCache.a("device_time", time);
    }

    public long getDeviceTime() {
        return ApiCache.a("device_time");
    }
}
