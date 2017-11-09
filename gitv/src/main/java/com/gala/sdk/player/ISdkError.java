package com.gala.sdk.player;

public interface ISdkError {
    public static final int CODE_CONFIG_INIT = 4;
    public static final int CODE_EVENT_LIVE_PROGRAM_FINISHED = 1001;
    public static final int CODE_EVENT_PAY_BEFORE_PREVIEW = 1002;
    public static final int CODE_EVENT_PREVIEW_COMPLETE = 1000;
    public static final int CODE_HTTP = 2;
    public static final int CODE_INTERTRUST_DRM_DEVICE_NOT_SUPPORT = 50004;
    public static final int CODE_INTERTRUST_DRM_DLOPEN = 50001;
    public static final int CODE_INTERTRUST_DRM_DLSYM = 50002;
    public static final int CODE_INTERTRUST_DRM_MODULE_NOT_EXIST = 50003;
    public static final int CODE_JOB_CANCELLED = 5;
    public static final int CODE_NATIVE_BOLCK = 10002;
    public static final int CODE_NATIVE_VIP_INFO = 10001;
    public static final int CODE_NO_ERROR = 0;
    public static final int CODE_PACKAGE_NAME_NOT_ALLOWED = 8;
    public static final int CODE_PARAMETER_INVALID = 6;
    public static final int CODE_REQUEST_TIMEOUT = 7;
    public static final int CODE_SERVER = 3;
    public static final int CODE_STATE_INVALID = 9;
    public static final int CODE_UNKNOWN = 1;
    public static final int MODULE_DEFAULT = 0;
    public static final int MODULE_PLAYER_ANDROID = 102;
    public static final int MODULE_PLAYER_NATIVE = 101;
    public static final int MODULE_PLAYER_VLC = 103;
    public static final int MODULE_SERVER_BOSS = 201;
    public static final int MODULE_SERVER_PASSPORT = 203;
    public static final int MODULE_SERVER_TV = 205;
    public static final int MODULE_SERVER_VR = 204;
    public static final int MODULE_SERVER_VRS = 202;
    public static final int MODULE_SPECIAL_EVENT = 10000;
    public static final int MODULE_THIRDPARTY_DRM = 301;

    String getBacktrace();

    int getCode();

    String getExtra1();

    String getExtra2();

    int getHttpCode();

    String getHttpMessage();

    String getMessage();

    int getModule();

    String getServerCode();

    String getServerMessage();

    String getString();

    String getUniqueId();

    String toUniqueCode();
}
