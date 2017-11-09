package com.gala.report.core.upload.config;

public class LogRecordConfigUtils {
    private static GlobalConfig sConfig = new GlobalConfig();

    public static void setGlobalConfig(GlobalConfig config) {
        sConfig = config;
    }

    public static GlobalConfig getGlobalConfig() {
        return sConfig;
    }
}
