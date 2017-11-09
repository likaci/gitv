package com.qiyi.tv.client.impl;

public class Params {

    public static class AccountSource {
        public static final int HUA_WEI = 1;
    }

    public static class AppCategory {
        public static final int COLLECTION = 2;
        public static final int DETAIL = 1;
        public static final int FOCUS = 3;
    }

    public static class DataType {
        public static final int DATA_ACCOUNT_STATUS = 30006;
        public static final int DATA_ACTIVATION_STATE = 30013;
        public static final int DATA_APP_INFO = 30012;
        public static final int DATA_APP_INFO_LIST = 30011;
        public static final int DATA_CHANNEL_LIST = 30001;
        public static final int DATA_EMPTY = 30000;
        public static final int DATA_MEDIA = 30008;
        public static final int DATA_MEDIA_LIST = 30002;
        public static final int DATA_RECOMMENDATION = 30003;
        public static final int DATA_RECOMMENDATION_FOR_TAB = 30010;
        public static final int DATA_SEARCH_HOT = 30005;
        public static final int DATA_SEARCH_SUGGESTION = 30004;
        public static final int DATA_URL = 30007;
        public static final int DATA_VIP_INFO = 30014;
    }

    public static class Extras {
        public static final String ACTION_FAVORITE_CHANGED_ACTION = "com.qiyi.tv.client.ACTION_FAVORITE_CHANGED_ACTION";
        public static final String ACTION_HISTORY_CHANGED_ACTION = "com.qiyi.tv.client.ACTION_HISTORY_CHANGED_ACTION";
        public static final String ACTION_PLAY_STATE = "com.qiyi.tv.client.ACTION_PLAY_STATE";
        public static final String ACTION_REQUEST_APPINFO_ACTION = "com.qiyi.tv.client.ACTION_REQUEST_APPINFO_ACTION";
        public static final String ACTION_RESPONSE_APPINFO_ACTION = "com.qiyi.tv.client.ACTION_RESPONSE_APPINFO_ACTION";
        public static final String ACTION_SERVICE_PREFIX = "com.qiyi.tv.client.ACTION_SERVICE_XHKO";
        public static final String EXTRA_ACCOUNT_AUTHCOOKIE = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_AUTHCOOKIE";
        public static final String EXTRA_ACCOUNT_EXPIRE = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_EXPIRE";
        public static final String EXTRA_ACCOUNT_FROM = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_FROM";
        public static final String EXTRA_ACCOUNT_GENDER = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_GENDER";
        public static final String EXTRA_ACCOUNT_ICONURL = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_ICONURL";
        public static final String EXTRA_ACCOUNT_NAME = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_NAME";
        public static final String EXTRA_ACCOUNT_NICKNAME = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_NICKNAME";
        public static final String EXTRA_ACCOUNT_REFRESH_TOKEN = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_REFRESH_TOKEN";
        public static final String EXTRA_ACCOUNT_STATUS = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_STATUS";
        public static final String EXTRA_ACCOUNT_TOKEN = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_TOKEN";
        public static final String EXTRA_ACCOUNT_TOKEN_SECRET = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_TOKEN_SECRET";
        public static final String EXTRA_ACCOUNT_UID = "com.qiyi.tv.sdk.extra.EXTRA_ACCOUNT_UID";
        public static final String EXTRA_ACTIVATE_CODE = "com.qiyi.tv.sdk.extra.EXTRA_ACTIVATE_CODE";
        public static final String EXTRA_ACTIVATION_CODE = "com.qiyi.tv.sdk.extra.EXTRA_ACTIVATION_CODE";
        public static final String EXTRA_ACTIVATION_STATE = "com.qiyi.tv.sdk.extra.EXTRA_ACTIVATION_STATE";
        public static final String EXTRA_ALBUM = "com.qiyi.tv.sdk.extra.EXTRA_ALBUM";
        public static final String EXTRA_API_KEY = "com.qiyi.tv.sdk.extra.EXTRA_API_KEY";
        public static final String EXTRA_APP_CATEGORY = "com.qiyi.tv.sdk.extra.EXTRA_APP_CATEGORY";
        public static final String EXTRA_APP_INFO = "com.qiyi.tv.sdk.extra.EXTRA_APP_INFO";
        public static final String EXTRA_BOOLEAN = "com.qiyi.tv.sdk.extra.EXTRA_BOOLEAN";
        public static final String EXTRA_CHANNEL = "com.qiyi.tv.sdk.extra.EXTRA_CHANNEL";
        public static final String EXTRA_CHANNELID = "com.qiyi.tv.sdk.extra.EXTRA_CHANNELID";
        public static final String EXTRA_CLASS_TAG = "com.qiyi.tv.sdk.extra.EXTRA_CLASS_TAG";
        public static final String EXTRA_COMMAND_CONTINUE = "com.qiyi.tv.sdk.extra.EXTRA_COMMAND_CONTINUE";
        public static final String EXTRA_COUNT = "com.qiyi.tv.sdk.extra.EXTRA_COUNT";
        public static final String EXTRA_CURRENT_TV_PACKAGENAME = "com.qiyi.tv.sdk.extra.EXTRA_CURRENT_TV_PACKAGENAME";
        public static final String EXTRA_CURRENT_UUID = "com.qiyi.tv.sdk.extra.EXTRA_CURRENT_UUID";
        public static final String EXTRA_CUSTOMER_PACKAGE_NAME = "com.qiyi.tv.sdk.extra.EXTRA_CUSTOMER_PACKAGE_NAME";
        public static final String EXTRA_CUSTOMER_SIGNATURE = "com.qiyi.tv.sdk.extra.EXTRA_CUSTOMER_SIGNATURE";
        public static final String EXTRA_CUSTOMER_UUID = "com.qiyi.tv.sdk.extra.EXTRA_CUSTOMER_UUID";
        public static final String EXTRA_CUSTOMER_VERSION_CODE = "com.qiyi.tv.sdk.extra.EXTRA_CUSTOMER_VERSION_CODE";
        public static final String EXTRA_CUSTOMER_VERSION_NAME = "com.qiyi.tv.sdk.extra.EXTRA_CUSTOMER_VERSION_NAME";
        public static final String EXTRA_DAILYLABEL = "com.qiyi.tv.sdk.extra.EXTRA_DAILYLABEL";
        public static final String EXTRA_FAVORITE_CHANGED_ACTION = "com.qiyi.tv.sdk.extra.EXTRA_FAVORITE_CHANGED_ACTION";
        public static final String EXTRA_FILTER_TAGS = "com.qiyi.tv.sdk.extra.EXTRA_FILTER_TAGS";
        public static final String EXTRA_FROM_THIRD_USER = "com.qiyi.tv.sdk.extra.EXTRA_FROM_THIRD_USER";
        public static final String EXTRA_HISTORY_CHANGED_ACTION = "com.qiyi.tv.sdk.extra.EXTRA_HISTORY_CHANGED_ACTION";
        public static final String EXTRA_HOME_TAB = "com.qiyi.tv.sdk.extra.EXTRA_HOME_TAB";
        public static final String EXTRA_INTENT = "com.qiyi.tv.sdk.extra.EXTRA_INTENT";
        public static final String EXTRA_INTENT_FLAG = "com.qiyi.tv.sdk.extra.EXTRA_INTENT_FLAG";
        public static final String EXTRA_KEYWORD = "com.qiyi.tv.sdk.extra.EXTRA_KEYWORD";
        public static final String EXTRA_MAX_COUNT = "com.qiyi.tv.sdk.extra.EXTRA_MAX_COUNT";
        public static final String EXTRA_MEDIA_PLAY_PARAMS = "com.qiyi.tv.sdk.extra.EXTRA_MEDIA_PLAY_PARAMS";
        public static final String EXTRA_ONLY_LONG_VIDEO = "com.qiyi.tv.sdk.extra.EXTRA_ONLY_LONG_VIDEO";
        public static final String EXTRA_OPERATION_DATA_TYPE = "com.qiyi.tv.sdk.extra.EXTRA_OPERATION_DATA_TYPE";
        public static final String EXTRA_OPERATION_TARGET = "com.qiyi.tv.sdk.extra.EXTRA_OPERATION_TARGET";
        public static final String EXTRA_OPERATION_TYPE = "com.qiyi.tv.sdk.extra.EXTRA_OPERATION_TYPE";
        public static final String EXTRA_PAGE_MAX_SIZE = "com.qiyi.tv.sdk.extra.EXTRA_PAGE_MAX_SIZE";
        public static final String EXTRA_PAGE_NO = "com.qiyi.tv.sdk.extra.EXTRA_PAGE_NO";
        public static final String EXTRA_PAGE_SIZE = "com.qiyi.tv.sdk.extra.EXTRA_PAGE_SIZE";
        public static final String EXTRA_PICTURE_SIZE = "com.qiyi.tv.sdk.extra.EXTRA_PICTURE_SIZE";
        public static final String EXTRA_PICTURE_TYPE = "com.qiyi.tv.sdk.extra.EXTRA_PICTURE_TYPE";
        public static final String EXTRA_PICTURE_URL = "com.qiyi.tv.sdk.extra.EXTRA_PICTURE_URL";
        public static final String EXTRA_PLAYLIST = "com.qiyi.tv.sdk.extra.EXTRA_PLAYLIST";
        public static final String EXTRA_PLAY_STATE = "com.qiyi.tv.sdk.extra.EXTRA_PLAY_STATE";
        public static final String EXTRA_PLUGIN_PROVIDER = "com.qiyi.tv.sdk.extra.EXTRA_PLUGIN_PROVIDER";
        public static final String EXTRA_POSITION = "com.qiyi.tv.sdk.extra.EXTRA_POSITION";
        public static final String EXTRA_QRCODE_URL = "com.qiyi.tv.sdk.extra.EXTRA_QRCODE_URL";
        public static final String EXTRA_RESOURCE_ID = "com.qiyi.tv.sdk.extra.EXTRA_RESOURCE_ID";
        public static final String EXTRA_RESOURCE_PICTURE_URL = "com.qiyi.tv.sdk.extra.EXTRA_RESOURCE_PICTURE_URL";
        public static final String EXTRA_RESULT_CODE = "com.qiyi.tv.sdk.extra.EXTRA_RESULT_CODE";
        public static final String EXTRA_RESULT_DATA_SIZE = "com.qiyi.tv.sdk.extra.EXTRA_RESULT_DATA_SIZE";
        public static final String EXTRA_RESULT_DATA_TYPE = "com.qiyi.tv.sdk.extra.EXTRA_RESULT_DATA_TYPE";
        public static final String EXTRA_RESULT_DATA_VALUE = "com.qiyi.tv.sdk.extra.EXTRA_RESULT_DATA_VALUE";
        public static final String EXTRA_SCREEN_SCALE = "com.qiyi.tv.sdk.extra.EXTRA_SCREEN_SCALE";
        public static final String EXTRA_SKIP_HEADER_TAILER = "com.qiyi.tv.sdk.extra.EXTRA_SKIP_HEADER_TAILER";
        public static final String EXTRA_SORT = "com.qiyi.tv.sdk.extra.EXTRA_SORT";
        public static final String EXTRA_STREAM_TYPE = "com.qiyi.tv.sdk.extra.EXTRA_STREAM_TYPE";
        public static final String EXTRA_STRING = "com.qiyi.tv.sdk.extra.EXTRA_STRING";
        public static final String EXTRA_TITLE = "com.qiyi.tv.sdk.extra.EXTRA_TITLE";
        public static final String EXTRA_VIDEO = "com.qiyi.tv.sdk.extra.EXTRA_VIDEO";
        public static final String EXTRA_VIP_INFO = "com.qiyi.tv.sdk.extra.EXTRA_VIP_INFO";
    }

    public static class FavoriteChangedAction {
        public static final int ADD_FAVORITE_ACTION = 1;
        public static final int CLEAR_FAVORITE_ACTION = 3;
        public static final int REMOVE_FAVORITE_ACTION = 2;
    }

    public static class HistoryChangedAction {
        public static final int ADD_HISTORY_ACTION = 1;
        public static final int CLEAR_HISTORY_ACTION = 3;
        public static final int REMOVE_HISTORY_ACTION = 2;
    }

    public static class OperationType {
        public static final int OP_ADD = 20100;
        public static final int OP_CLEAR = 20102;
        public static final int OP_CLEAR_ANONYMOUS_FAVORITE = 20208;
        public static final int OP_CLEAR_ANONYMOUS_HISTORY = 20206;
        public static final int OP_CLEAR_FAVORITE = 20207;
        public static final int OP_CLEAR_HISTORY = 20205;
        public static final int OP_DELETE_ANONYMOUS_FAVORITE = 20214;
        public static final int OP_DELETE_ANONYMOUS_HISTORY = 20212;
        public static final int OP_DELETE_FAVORITE = 20213;
        public static final int OP_DELETE_HISTORY = 20211;
        public static final int OP_GET = 20003;
        public static final int OP_LOGIN = 20201;
        public static final int OP_LOGOUT = 20202;
        public static final int OP_MERGE_FAVORITE = 20204;
        public static final int OP_MERGE_HISTORY = 20203;
        public static final int OP_OPEN = 20001;
        public static final int OP_OPEN_ACTIVATION_PAGE = 20210;
        public static final int OP_PLAY = 20004;
        public static final int OP_PULL = 20209;
        public static final int OP_REMOVE = 20101;
        public static final int OP_SET = 20002;
        public static final int OP_UNKNOWN = 20000;
    }

    public static class PlayState {
        public static final int STATE_COMPLETED = 6;
        public static final int STATE_ERROR = 8;
        public static final int STATE_PAUSED = 5;
        public static final int STATE_PREPARED = 4;
        public static final int STATE_PREPARING = 3;
        public static final int STATE_STARTED = 1;
        public static final int STATE_STOPPED = 2;
        public static final int STATE_STOPPING = 7;
    }

    public static class RecommendationType {
        public static final int COMMON = 2;
        public static final int EXTRUDE = 0;
        public static final int SUBJECT = 1;
    }

    public static class ResultDataType {
        public static int RESULT_TYPE_PARCELABLE_ARRAY = 2;
        public static int RESULT_TYPE_PARCELABLE_ARRAY_LIST = 1;
        public static int RESULT_TYPE_PARCELABLE_ITEM = 0;
        public static int RESULT_TYPE_STRING_ARRAY_LIST = 3;
        public static int RESULT_TYPE_UNKNOWN = -1;
    }

    public static class TargetType {
        public static final int TARGET_ACCOUNT = 10002;
        public static final int TARGET_ACTIVATE_PAGE = 10209;
        public static final int TARGET_APPLIST = 10013;
        public static final int TARGET_APP_STORE = 10009;
        public static final int TARGET_APP_STORE_APPDETAIL = 10602;
        public static final int TARGET_APP_STORE_APPINFO = 10601;
        public static final int TARGET_APP_STORE_SEARCH = 10018;
        public static final int TARGET_AUTH = 10301;
        public static final int TARGET_BUY_VIP = 10019;
        public static final int TARGET_CAROUSEL = 10017;
        public static final int TARGET_CHANNEL = 10200;
        public static final int TARGET_CHANNEL_HOT = 10103;
        public static final int TARGET_CHANNEL_NEWEST = 10102;
        public static final int TARGET_CINEMA = 10207;
        public static final int TARGET_DAILY_NEWS = 10006;
        public static final int TARGET_FAVORITE = 10003;
        public static final int TARGET_FOOT_HISTORY = 10011;
        public static final int TARGET_HISTORY = 10001;
        public static final int TARGET_HOME_TAB = 10801;
        public static final int TARGET_HUAWEI_RECOMMED = 10208;
        public static final int TARGET_MEDIA = 10201;
        public static final int TARGET_MEDIA_DETAIL = 10206;
        public static final int TARGET_MULTISCREEN = 10501;
        public static final int TARGET_NETWORK_SPEED = 10005;
        public static final int TARGET_PALYER_SETTING = 10015;
        public static final int TARGET_PICTURE = 10401;
        public static final int TARGET_QR_CODE = 10402;
        public static final int TARGET_RECOMMEND = 10203;
        public static final int TARGET_RESOURCE_MEDIA = 10205;
        public static final int TARGET_SCREENSCALE = 10502;
        public static final int TARGET_SEARCH = 10008;
        public static final int TARGET_SEARCH_RESULT = 10100;
        public static final int TARGET_SKIP_HEADER_TAILER = 10504;
        public static final int TARGET_STREAM_TYPE = 10503;
        public static final int TARGET_TOPIC = 10202;
        public static final int TARGET_TV_QR_CODE = 10403;
        public static final int TARGET_UNKNOWN = 10000;
        public static final int TARGET_VIP = 10014;
        public static final int TARGET_VIP_RIGHTS = 10701;
        public static final int TARGET_VRS_MEDIA = 10204;
    }

    private Params() {
    }
}
