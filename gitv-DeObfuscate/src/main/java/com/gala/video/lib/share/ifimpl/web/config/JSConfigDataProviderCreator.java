package com.gala.video.lib.share.ifimpl.web.config;

import com.gala.video.lib.share.ifmanager.bussnessIF.web.IJSConfigDataProvider;

public class JSConfigDataProviderCreator {
    public static IJSConfigDataProvider create() {
        return new JSConfigDataProvider();
    }
}
