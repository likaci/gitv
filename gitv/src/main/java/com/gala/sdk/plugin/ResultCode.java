package com.gala.sdk.plugin;

public class ResultCode {
    public static final int CODE_FAILED = 0;
    public static final int CODE_SUCCESS = 1;
    public static final int PERIOD_LOAD_ASSETS = 1;
    public static final int PERIOD_LOAD_ASSETS_SD = 3;
    public static final int PERIOD_LOAD_DOWNLOAD = 2;
    public static final int PERIOD_LOAD_LOCAL = 0;

    public static class ERROR_TYPE {
        public static final String ERROR_LOAD_ASSETS = "loadAssets";
        public static final String ERROR_LOAD_ASSETS_SD = "loadAssetsSd";
        public static final String ERROR_LOAD_DOWNLOAD = "loadDownload";
        public static final String ERROR_LOAD_LOCAL = "loadLocal";
    }
}
