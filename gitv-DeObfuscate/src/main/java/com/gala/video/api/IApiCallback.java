package com.gala.video.api;

public interface IApiCallback<T> {
    void onException(ApiException apiException);

    void onSuccess(T t);
}
