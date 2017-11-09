package com.gala.video.api.http;

import java.util.List;

public interface IHttpCallback {
    String onCalling(String str);

    void onException(Exception exception, String str, String str2, List<Integer> list);

    List<String> onHeader(List<String> list);

    void onSuccess(String str, String str2, List<Integer> list);
}
