package com.gala.video.lib.share.ifimpl.web.provider;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebJsonParmsProvider;

public class WebJsonParmsProviderCreator {
    public static IWebJsonParmsProvider create() {
        return new WebJsonParmsProvider();
    }
}
