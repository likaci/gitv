package com.gala.video.lib.framework.core.network.download;

public class GalaDownloadException extends Exception {
    public static final int ERROR_DOWNLOAD_FAILED = 100;
    public static final int ERROR_ILLEGAL_ARGUMENT = 2;
    public static final int ERROR_MALFORMED_URL = 3;
    public static final int ERROR_MD5_CHECK = 6;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_NOT_SUPPORT_SYNC = 1;
    public static final int ERROR_NO_SPACE = 4;
    public static final int ERROR_WRITE_DISK = 5;
    private int mErrorCode;

    public GalaDownloadException(int errorCode, String msg) {
        super(msg);
        this.mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}
