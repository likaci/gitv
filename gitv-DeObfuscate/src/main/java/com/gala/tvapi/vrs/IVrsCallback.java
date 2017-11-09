package com.gala.tvapi.vrs;

import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;

public interface IVrsCallback<T extends ApiResult> {
    void onException(ApiException apiException);

    void onSuccess(T t);
}
