package com.gala.video.api;

import com.gala.video.api.http.HttpEngineFactory;
import java.util.Random;

public final class C0484e<T extends ApiResult> extends C0474a implements C0480c<T> {
    protected C0484e() {
        Random random = new Random(5203);
        HttpEngineFactory.newCommonEngine("TVApi", 2);
    }

    protected C0484e(IApiFilter iApiFilter) {
        Random random = new Random(5203);
        HttpEngineFactory.newCommonEngine("TVApi", 2);
        m1516a(iApiFilter);
    }
}
