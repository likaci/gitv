package com.gala.video.app.epg.home.data.model;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;

public class ChannelModel extends HomeModel {
    private static final String TAG = "ChannelModel";
    private static final long serialVersionUID = 1;
    private Channel mChannel;
    private ChannelCarousel mChannelCarousel;
    private String mIcon;
    private String mId;
    private String mImageUrl;
    private String mTableNo;
    private String mTextContent;

    public ChannelModel(Channel channel) {
        this.mChannel = channel;
        this.mId = channel.id;
        this.mTextContent = channel.name;
        this.mImageUrl = channel.picUrl;
        this.mIcon = channel.icon;
    }

    public ChannelModel(ChannelCarousel channel) {
        this.mChannelCarousel = channel;
        this.mId = String.valueOf(channel.id);
        this.mTextContent = channel.name;
        this.mImageUrl = channel.logo;
        this.mIcon = channel.logo;
        this.mTableNo = String.valueOf(channel.tableNo);
    }

    public ChannelModel(TVChannelCarousel channel) {
        ChannelCarousel carousel = new ChannelCarousel();
        carousel.id = channel.id;
        carousel.name = channel.name;
        carousel.logo = channel.icon;
        carousel.tableNo = channel.sid;
        this.mChannelCarousel = carousel;
        this.mId = String.valueOf(channel.id);
        this.mTextContent = channel.name;
        this.mImageUrl = channel.icon;
        this.mIcon = channel.icon;
        this.mTableNo = String.valueOf(channel.sid);
    }

    public String getId() {
        return this.mId;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public String getTextContent() {
        return this.mTextContent;
    }

    public Channel getImpData() {
        return this.mChannel;
    }

    public String getIcon() {
        return this.mIcon;
    }

    public String getTableNo() {
        return this.mTableNo;
    }
}
