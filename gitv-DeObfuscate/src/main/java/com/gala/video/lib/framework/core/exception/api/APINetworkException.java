package com.gala.video.lib.framework.core.exception.api;

import com.gala.video.lib.framework.core.exception.ApiException;

public class APINetworkException extends ApiException {
    private static final long serialVersionUID = 8891956433747791555L;

    public APINetworkException() {
        setType();
    }

    public APINetworkException(Exception e) {
        super(e);
        setType();
    }

    private void setType() {
        this.type = 769;
    }
}
