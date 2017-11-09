package com.gala.sdk.player;

public class Build {
    private static final int BUILD_TYPE = 0;
    public static final int BUILD_TYPE_APK = 1;
    public static final int BUILD_TYPE_JAR = 0;
    private static final String COMMIT_VERSION = "1";
    private static final String HEADER_VERSION = "2.1.0";

    public static int getBuildType() {
        return 0;
    }

    public static String getVersion() {
        return "2.1.01";
    }
}
