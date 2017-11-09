package com.gala.video.api.http;

import java.util.List;

public interface IHttpEngine {
    void call(String str, List<String> list, IHttpCallback iHttpCallback, boolean z, String str2);

    void callSync(String str, List<String> list, IHttpCallback iHttpCallback, boolean z, String str2);

    void setDelayDuration(long j);
}
