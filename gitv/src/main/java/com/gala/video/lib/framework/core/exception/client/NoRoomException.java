package com.gala.video.lib.framework.core.exception.client;

import com.gala.video.lib.framework.core.exception.ClientException;

public class NoRoomException extends ClientException {
    private static final long serialVersionUID = 1;

    public NoRoomException() {
        setType();
    }

    public NoRoomException(Exception e) {
        super(e);
        setType();
    }

    private void setType() {
        this.type = 513;
    }
}
