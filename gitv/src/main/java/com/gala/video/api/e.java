package com.gala.video.api;

import com.gala.video.api.http.HttpEngineFactory;
import java.util.Random;

public final class e<T extends ApiResult> extends a implements c<T> {
    protected e() {
        Random random = new Random(5203);
        HttpEngineFactory.newCommonEngine("TVApi", 2);
    }

    protected e(IApiFilter iApiFilter) {
        Random random = new Random(5203);
        HttpEngineFactory.newCommonEngine("TVApi", 2);
        a(iApiFilter);
    }
}
