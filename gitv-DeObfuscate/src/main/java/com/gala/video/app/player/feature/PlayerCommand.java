package com.gala.video.app.player.feature;

public class PlayerCommand {
    public static final String CMD = "PLAYER_COMMAND";
    public static final String CMD_UPDATE_AUTHORIZATION = "UPDATE_AUTHORIZATION";
    public static final String CMD_UPDATE_DEVICE_CHECK = "UPDATE_DEVICE_CHECK";
    public static final String CREATE_PLAYER_FACTORY = "CREATE_PLAYER_FACTORY";
    public static final String ENABLE_HCDN_CLEAN_AVAILABLE = "ENABLE_HCDN_CLEAN_AVAILABLE";
    public static final String ENABLE_HCDN_PRE_DEPLOY = "ENABLE_HCDN_PRE_PUSH";
    public static final String EXTRA = "EXTRA";
    public static final String GET_PLAYER_MODULE_VERSION = "GET_PLAYER_MODULE_VERSION";
    public static final String GET_PUMA_LOG = "GET_PUMA_LOG";
    public static final String IS_SUPPORT_DOLBY = "IS_SUPPORT_DOLBY";
    public static final String IS_SUPPORT_H211 = "IS_SUPPORT_H211";
    public static final String KEY_APIKEY = "APIKEY";
    public static final String KEY_AUTHID = "AUTHID";
    public static final String KEY_AUTHORIZATION = "AUTHORIZATION";
    public static final String LOAD_FAILED_COUNT = "LOAD_FAILED_COUNT";
    public static final String LOAD_PLUGIN_ASYNC = "LOAD_PLUGIN_ASYNC";
    public static final String PARCELABLE_RESULT = "PARCELABLE_RESULT";
    public static final String PLAYER_TYPE = "Player_type";
    public static final int RESULT_FAIL = -1;
    public static final int RESULT_OK = 0;
    public static final int RESULT_UNKNOWN = 1;

    public interface IConnectListener {
        void onStateChange(ServiceConnectState serviceConnectState);
    }

    public enum ServiceConnectState {
        IDLE,
        INITIALIZED,
        CONNECTING,
        CONNECTED
    }
}
