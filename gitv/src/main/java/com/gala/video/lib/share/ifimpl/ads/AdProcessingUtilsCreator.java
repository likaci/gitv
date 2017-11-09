package com.gala.video.lib.share.ifimpl.ads;

import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdProcessingUtils;

public class AdProcessingUtilsCreator {
    public static IAdProcessingUtils create() {
        return new AdProcessingUtils();
    }
}
