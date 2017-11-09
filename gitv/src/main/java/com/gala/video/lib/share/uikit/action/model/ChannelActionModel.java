package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class ChannelActionModel extends BaseActionModel<Channel> {
    private String from;
    private Channel mChannel;
    private int mChnId;
    private String mPingback;
    private String mTabName;
    private String mTitle;

    public ChannelActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(Channel channel) {
        this.mTitle = "";
        this.mTabName = "";
        this.mChannel = channel;
        this.mChnId = StringUtils.parse(channel.id, 0);
        return this;
    }

    public CarouselPlayParamBuilder getCarouselPlayParamBuilder() {
        CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
        carouselPlayParamBuilder.setChannel(null);
        carouselPlayParamBuilder.setFrom(this.from);
        carouselPlayParamBuilder.setTabSource(this.mTabName);
        return carouselPlayParamBuilder;
    }

    public void setPingback(String pingback) {
        this.mPingback = pingback;
        this.from = this.mPingback + "_" + this.mTitle;
        this.mTabName = "tab_" + this.mPingback;
    }

    public void setChannel(Channel channel) {
        this.mChannel = channel;
    }

    public void setChnId(int chnId) {
        this.mChnId = chnId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public Channel getChannel() {
        return this.mChannel;
    }

    public String getFrom() {
        return this.from;
    }

    public int getChnId() {
        return this.mChnId;
    }

    public String getTabName() {
        return this.mTabName;
    }
}
