package com.gala.video.api;

public class ApiFactory {
    public static final <T extends ApiResult> C0480c<T> build(IApiUrlBuilder iApiUrlBuilder, Class<T> cls) {
        return new C0484e();
    }

    public static final <T extends ApiResult> C0480c<T> build(IApiUrlBuilder iApiUrlBuilder, Class<T> cls, IApiFilter filter) {
        return new C0484e(filter);
    }

    public static final IPingbackApi getPingbackApi() {
        return C0483d.m1519a();
    }

    public static final ICommonApi getCommonApi() {
        return C0479b.m1517a();
    }
}
