package com.gala.tvapi.tv2;

import com.gala.tvapi.TVApiHeader;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;

public interface ITVApiServer<T extends ApiResult> {
    void call(IApiCallback<T> iApiCallback, TVApiHeader tVApiHeader, String... strArr);

    void call(IApiCallback<T> iApiCallback, String... strArr);

    void callSync(IApiCallback<T> iApiCallback, TVApiHeader tVApiHeader, String... strArr);

    void callSync(IApiCallback<T> iApiCallback, String... strArr);
}
