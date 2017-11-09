package com.gala.video.lib.framework.core.exception;

public class ApiException extends BaseException {
    private static final long serialVersionUID = 1;

    public ApiException() {
        super(4);
    }

    public ApiException(Exception e) {
        super(e, 4);
    }
}
