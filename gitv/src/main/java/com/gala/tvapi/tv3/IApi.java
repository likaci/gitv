package com.gala.tvapi.tv3;

public interface IApi<T extends ApiResult> {
    void callAsync(IApiCallback<T> iApiCallback, String... strArr);

    void callSync(IApiCallback<T> iApiCallback, String... strArr);
}
