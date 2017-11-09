package com.gala.video.lib.framework.core.exception;

public class BaseException extends RuntimeException {
    public static final int CATEGORY_API = 4;
    public static final int CATEGORY_CLIENT = 2;
    public static final int CATEGORY_ERROR = 5;
    public static final int CATEGORY_PLAYER = 3;
    public static final int CATEGORY_UNKNOWN = 1;
    public static final int TYPE_API_CODE = 772;
    public static final int TYPE_API_CONNECTION = 769;
    public static final int TYPE_API_DATA_EMPTY = 771;
    public static final int TYPE_API_DATA_FORMAT = 770;
    public static final int TYPE_API_RESULT_SIZE = 773;
    public static final int TYPE_API_USER_LOGIN = 774;
    public static final int TYPE_CLIENT_SPACE_NOT_ENOUGH = 513;
    public static final int TYPE_ERROR_CONNECT_IN_UI_THREAD = 1281;
    public static final int TYPE_UNKNOWN = 256;
    private static final long serialVersionUID = -5717773628878516451L;
    protected int category = 1;
    protected int type = 256;

    public BaseException(int category) {
        this.category = category;
    }

    public BaseException(Throwable e) {
        super(e);
    }

    public BaseException(Throwable e, int type) {
        super(e);
        this.category = type;
    }
}
