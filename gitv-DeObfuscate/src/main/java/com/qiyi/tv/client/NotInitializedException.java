package com.qiyi.tv.client;

public class NotInitializedException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public NotInitializedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
