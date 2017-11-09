package com.gala.video.lib.share.uikit.data.provider;

import com.gala.tvapi.vrs.model.ChannelLabel;

public class NetCheckDataProvider {
    private static final NetCheckDataProvider sInstance = new NetCheckDataProvider();
    private ChannelLabel data = null;

    private NetCheckDataProvider() {
    }

    public static NetCheckDataProvider getInstance() {
        return sInstance;
    }

    public void setData(ChannelLabel label) {
        this.data = label;
    }

    public ChannelLabel getData() {
        return this.data;
    }
}
