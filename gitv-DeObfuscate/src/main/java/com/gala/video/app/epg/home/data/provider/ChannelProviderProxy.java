package com.gala.video.app.epg.home.data.provider;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.IChannelProviderProxy.Wrapper;

public class ChannelProviderProxy extends Wrapper {
    public Channel getChannelById(int channelId) {
        return ChannelProvider.getInstance().getChannelById(channelId);
    }
}
