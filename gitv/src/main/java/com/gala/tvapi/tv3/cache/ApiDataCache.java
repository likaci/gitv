package com.gala.tvapi.tv3.cache;

public class ApiDataCache {
    private static RegisterDataCache a = new RegisterDataCache();
    private static TimeDataCache f469a = new TimeDataCache();

    public static RegisterDataCache getRegisterDataCache() {
        return a;
    }

    public static TimeDataCache getTimeDataCache() {
        return f469a;
    }
}
