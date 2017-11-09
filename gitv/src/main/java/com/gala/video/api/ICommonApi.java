package com.gala.video.api;

public interface ICommonApi {
    void call(String str, ICommonApiCallback iCommonApiCallback, IApiHeader iApiHeader, boolean z, String str2);

    void call(String str, ICommonApiCallback iCommonApiCallback, boolean z, String str2);

    void callSync(String str, ICommonApiCallback iCommonApiCallback, IApiHeader iApiHeader, boolean z, String str2);

    void callSync(String str, ICommonApiCallback iCommonApiCallback, boolean z, String str2);
}
