package com.gala.tvapi.vrs.core;

import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.video.api.ApiResult;

public interface IVrsServer<T extends ApiResult> {
    void call(IVrsCallback<T> iVrsCallback, TVApiHeader tVApiHeader, String... strArr);

    void call(IVrsCallback<T> iVrsCallback, String... strArr);

    void callSync(IVrsCallback<T> iVrsCallback, TVApiHeader tVApiHeader, String... strArr);

    void callSync(IVrsCallback<T> iVrsCallback, String... strArr);
}
