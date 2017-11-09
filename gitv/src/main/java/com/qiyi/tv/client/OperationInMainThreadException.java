package com.qiyi.tv.client;

public class OperationInMainThreadException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public OperationInMainThreadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
