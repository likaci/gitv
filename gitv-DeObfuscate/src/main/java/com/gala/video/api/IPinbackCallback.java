package com.gala.video.api;

public interface IPinbackCallback {
    void onException(String str, Exception exception);

    void onSuccess(String str);
}
