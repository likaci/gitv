package com.gala.video.api;

public interface ICommonApiCallback {
    void onException(Exception exception, String str);

    void onSuccess(String str);
}
