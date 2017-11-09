package com.gala.video.lib.share.common.model.player;

public class NewsDetailPlayParamBuilder extends AbsPlayParamBuilder {
    public String mChannelName;
    public NewsParams mParams;

    public NewsDetailPlayParamBuilder setChannelName(String channelName) {
        this.mChannelName = channelName;
        return this;
    }

    public NewsDetailPlayParamBuilder setNewParams(NewsParams newsParams) {
        this.mParams = newsParams;
        return this;
    }
}
