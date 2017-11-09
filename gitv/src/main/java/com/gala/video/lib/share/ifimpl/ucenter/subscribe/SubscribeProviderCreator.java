package com.gala.video.lib.share.ifimpl.ucenter.subscribe;

import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.ISubscribeProvider;

public class SubscribeProviderCreator {
    public static ISubscribeProvider create() {
        return new SubscribeProvider();
    }
}
