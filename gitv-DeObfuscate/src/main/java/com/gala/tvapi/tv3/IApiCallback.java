package com.gala.tvapi.tv3;

public interface IApiCallback<T> {
    void onException(ApiException apiException);

    void onSuccess(T t);
}
