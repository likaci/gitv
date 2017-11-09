package com.gala.video.utils.zxing;

public abstract class ReaderException extends Exception {
    protected static final StackTraceElement[] NO_TRACE = new StackTraceElement[0];
    protected static final boolean isStackTrace;
    private static final long serialVersionUID = 1;

    static {
        boolean z;
        if (System.getProperty("surefire.test.class.path") != null) {
            z = true;
        } else {
            z = false;
        }
        isStackTrace = z;
    }

    ReaderException() {
    }

    ReaderException(Throwable cause) {
        super(cause);
    }

    public final Throwable fillInStackTrace() {
        return null;
    }
}
