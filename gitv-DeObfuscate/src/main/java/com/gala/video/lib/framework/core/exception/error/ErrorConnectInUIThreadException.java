package com.gala.video.lib.framework.core.exception.error;

import com.gala.video.lib.framework.core.exception.ApiException;
import com.gala.video.lib.framework.core.exception.BaseException;

public class ErrorConnectInUIThreadException extends ApiException {
    private static final long serialVersionUID = 8891956433747791555L;

    public ErrorConnectInUIThreadException() {
        setType();
    }

    public ErrorConnectInUIThreadException(Exception e) {
        super(e);
        setType();
    }

    private void setType() {
        this.type = BaseException.TYPE_ERROR_CONNECT_IN_UI_THREAD;
    }
}
