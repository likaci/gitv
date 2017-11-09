package com.gala.video.api;

public class ApiFactory {
    public static final <T extends ApiResult> c<T> build(IApiUrlBuilder iApiUrlBuilder, Class<T> cls) {
        return new e();
    }

    public static final <T extends ApiResult> c<T> build(IApiUrlBuilder iApiUrlBuilder, Class<T> cls, IApiFilter filter) {
        return new e(filter);
    }

    public static final IPingbackApi getPingbackApi() {
        return d.a();
    }

    public static final ICommonApi getCommonApi() {
        return b.a();
    }
}
