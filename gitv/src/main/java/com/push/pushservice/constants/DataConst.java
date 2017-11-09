package com.push.pushservice.constants;

public class DataConst {
    public static final String ACTION_MESSAGE = "com.push.pushservice.action.MESSAGE";
    public static final String ACTION_RECEIVE = "com.push.pushservice.action.RECEIVE";
    public static final String APP_INFO_APP_ID = "app_id";
    public static final String APP_INFO_APP_LIST = "app_list";
    public static final String APP_INFO_APP_VER = "app_ver";
    public static final String APP_INFO_DEVICE_ID = "device_id";
    public static final String APP_INFO_HOST_LIST = "host_list";
    public static final String APP_INFO_PKG_NAME = "pkg_name";
    public static final String APP_INFO_REGISTER = "register";
    public static final String EXTRA_APP_ID = "appid";
    public static final int EXTRA_BIND = 19001;
    public static final String EXTRA_ERROR_CODE = "error_code";
    public static final String EXTRA_ERROR_MSG = "error_msg";
    public static final String EXTRA_ERROR_TYPE = "error_type";
    public static final int EXTRA_MESSAGE_CALLBACK = 1903;
    public static final String EXTRA_PUSH_APPID = "appid";
    public static final String EXTRA_PUSH_MESSAGE = "message";
    public static final String EXTRA_PUSH_MESSAGE_ID = "msg_id";
    public static final int EXTRA_UNBIND = 1902;
    public static final String HTTPS_TAG = "https:";
    public static final String MESSAGE_PINGBACK_ADDRESS = "http://msg.ptqy.gitv.tv/v5/zblts/pushsdk";
    public static final String PINGBACK_ADDRESS = "http://msg.ptqy.gitv.tv/v5/ypt/push";
    public static final short PLATFORM_ID = (short) 24;
    public static final int PUSH_TYPE = 1;
    public static final short SDK_VERSION = (short) 2;
    private static final String YINHE_KEPLER_HOST = "http://kepler.ptqy.gitv.tv";
    public static final String YINHE_SERVER_GET_PUSH_TYPE_URL = "http://kepler.ptqy.gitv.tv/apis/device/upload.action";

    public enum LocalArea {
        LA_MAINLAND(1),
        LA_TAIWAN(2);
        
        private int mValue;

        private LocalArea(int value) {
            this.mValue = 1;
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }
}
