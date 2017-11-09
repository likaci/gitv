package com.gala.tvapi.vrs.core;

import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.video.api.ApiResult;

public interface IPushVideoServer<T extends ApiResult> {
    void call(IVrsCallback<T> iVrsCallback, PlatformType platformType, String... strArr);

    void callSync(IVrsCallback<T> iVrsCallback, PlatformType platformType, String... strArr);
}
