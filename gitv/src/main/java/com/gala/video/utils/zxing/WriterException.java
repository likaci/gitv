package com.gala.video.utils.zxing;

public final class WriterException extends Exception {
    private static final long serialVersionUID = 1;

    public WriterException(String message) {
        super(message);
    }

    public WriterException(Throwable cause) {
        super(cause);
    }
}
