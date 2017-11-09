package com.gala.video.lib.share.uikit.data.data.Model;

public enum ErrorEvent {
    C_SUCCESS(0),
    C_ERROR_MAC(1),
    C_ERROR_E000054(2),
    C_ERROR_E000012(3),
    C_ERROR_E000001(11),
    C_ERROR_E_OTHER(4),
    C_ERROR_JSON(5),
    C_ERROR_HTTP(6),
    C_ERROR_SERVER(7),
    C_ERROR_INTERNET(8),
    C_ERROR_NONET(9),
    C_ERROR_DATAISNULL(10);
    
    public static final String API_CODE_FAIL_AUTH = "E000054";
    public static final String API_CODE_FAIL_DATA_EXCEPTION = "E000012";
    public static final String API_CODE_FAIL_JSON_EXCEPTION = "-100";
    public static final String API_CODE_FAIL_SERVICE = "E000001";
    public static final String API_CODE_GET_MAC_FAILD = "-1010";
    public static final String HTTP_CODE_FAIL_EXCEPTION = "-50";
    public static final String HTTP_CODE_SUCCESS = "200";
    private int mCode;

    private ErrorEvent(int code) {
        this.mCode = code;
    }

    public int getCode() {
        return this.mCode;
    }

    public boolean isSameAs(int code) {
        return getCode() == code;
    }
}
