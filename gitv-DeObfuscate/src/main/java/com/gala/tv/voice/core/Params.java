package com.gala.tv.voice.core;

public class Params {

    public static class Extras {
        public static final String EXTRA_CHANNEL_NAME = "com.gala.tv.sdk.extra.EXTRA_CHANNEL_NAME";
        public static final String EXTRA_COLUMN = "com.gala.tv.sdk.extra.EXTRA_COLUMN";
        public static final String EXTRA_CUSTOMER_PACKAGE_NAME = "com.gala.tv.sdk.extra.EXTRA_CUSTOMER_PACKAGE_NAME";
        public static final String EXTRA_CUSTOMER_VERSION_CODE = "com.gala.tv.sdk.extra.EXTRA_CUSTOMER_VERSION_CODE";
        public static final String EXTRA_CUSTOMER_VERSION_NAME = "com.gala.tv.sdk.extra.EXTRA_CUSTOMER_VERSION_NAME";
        public static final String EXTRA_EPISODE_INDEX = "com.gala.tv.sdk.extra.EXTRA_EPISODE_INDEX";
        public static final String EXTRA_KEYWORD = "com.gala.tv.sdk.extra.EXTRA_KEYWORD";
        public static final String EXTRA_OPERATION_TARGET = "com.gala.tv.sdk.extra.EXTRA_OPERATION_TARGET";
        public static final String EXTRA_OPERATION_TYPE = "com.gala.tv.sdk.extra.EXTRA_OPERATION_TYPE";
        public static final String EXTRA_RESULT_CODE = "com.gala.tv.sdk.extra.EXTRA_RESULT_CODE";
        public static final String EXTRA_RESULT_DATA_SIZE = "com.gala.tv.sdk.extra.EXTRA_RESULT_DATA_SIZE";
        public static final String EXTRA_RESULT_DATA_TYPE = "com.gala.tv.sdk.extra.EXTRA_RESULT_DATA_TYPE";
        public static final String EXTRA_RESULT_DATA_VALUE = "com.gala.tv.sdk.extra.EXTRA_RESULT_DATA_VALUE";
        public static final String EXTRA_ROW = "com.gala.tv.sdk.extra.EXTRA_ROW";
        public static final String SERVICE_ACTION_VOICE = "com.gala.tv.sdk.ACTION_VOICE_SERVICE";
    }

    public static class OperationType {
        public static final int OP_DISPATCH = 20001;
        public static final int OP_GET = 20002;
        public static final int OP_UNKNOWN = 20000;
    }

    public static class ResultCode {
        public static final int ERROR_INVALID_PARAMETERS = 1;
        public static final int SUCCESS = 0;
        public static final int UNKNOW = -1;
    }

    public static class ResultDataType {
        public static int RESULT_TYPE_BOOLEAN = 4;
        public static int RESULT_TYPE_PARCELABLE_ARRAY = 2;
        public static int RESULT_TYPE_PARCELABLE_ARRAY_LIST = 1;
        public static int RESULT_TYPE_PARCELABLE_ITEM = 0;
        public static int RESULT_TYPE_STRING_ARRAY_LIST = 3;
        public static int RESULT_TYPE_UNKNOWN = -1;
    }

    public static class TargetType {
        public static final int TARGET_VOICE_EVENT = 10001;
    }

    private Params() {
    }
}
