package com.gala.video.lib.framework.core.exception;

public class ClientException extends BaseException {
    private static final long serialVersionUID = 1;

    public ClientException() {
        super(2);
    }

    public ClientException(Exception e) {
        super(e, 2);
    }
}
