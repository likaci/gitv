package com.gala.video.lib.share.ifimpl.ads;

import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdApi;

public class AdApiCreator {
    public static IAdApi create() {
        return new AdApi();
    }
}
