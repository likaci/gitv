package com.gala.video.api;

import java.util.List;

public interface IApiFilter {
    String onCalling(String str);

    List<String> onHeader(List<String> list);
}
