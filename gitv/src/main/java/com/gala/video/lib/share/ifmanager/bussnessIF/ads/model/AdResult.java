package com.gala.video.lib.share.ifmanager.bussnessIF.ads.model;

import com.gala.video.lib.framework.core.utils.StringUtils;

public class AdResult {
    public String ad;

    public boolean isEmpty() {
        return StringUtils.isEmpty(this.ad);
    }
}
