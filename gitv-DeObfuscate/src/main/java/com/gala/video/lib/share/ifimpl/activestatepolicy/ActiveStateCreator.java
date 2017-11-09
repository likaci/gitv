package com.gala.video.lib.share.ifimpl.activestatepolicy;

import com.gala.video.lib.share.ifmanager.bussnessIF.activestatepolicy.IActiveStateDispatcher;

public class ActiveStateCreator {
    public static IActiveStateDispatcher create() {
        return new ActiveStateDispatcher();
    }
}
