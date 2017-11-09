package com.gala.video.api;

import java.util.List;

public interface IApiUrlBuilder {
    String build(String... strArr);

    List<String> header();
}
