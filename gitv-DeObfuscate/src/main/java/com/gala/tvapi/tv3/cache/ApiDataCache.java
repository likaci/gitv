package com.gala.tvapi.tv3.cache;

public class ApiDataCache {
    private static RegisterDataCache f1107a = new RegisterDataCache();
    private static TimeDataCache f1108a = new TimeDataCache();

    public static RegisterDataCache getRegisterDataCache() {
        return f1107a;
    }

    public static TimeDataCache getTimeDataCache() {
        return f1108a;
    }
}
