package com.gala.video.app.epg.home.data.provider;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.app.epg.home.data.model.ChannelModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.utils.Precondition;
import java.io.IOException;
import java.util.List;

public class CarouselChannelProvider {
    private static final String TAG = "CarouselChannelProvider";
    private static final CarouselChannelProvider sChannelProvider = new CarouselChannelProvider();
    private List<ChannelModel> mChannelList;

    private CarouselChannelProvider() {
    }

    public static CarouselChannelProvider getInstance() {
        return sChannelProvider;
    }

    public List<ChannelModel> getChannelList() {
        if (Precondition.isEmpty(this.mChannelList)) {
            try {
                this.mChannelList = (List) SerializableUtils.read(HomeDataConfig.HOME_CAROUSEL_CHANNEL_LIST_DIR);
            } catch (Exception e) {
                LogUtils.m1571e(TAG, "read carousel channel failed " + e);
            }
        }
        return this.mChannelList;
    }

    public ChannelModel getChannelModelById(int channelId, List<ChannelModel> list) {
        if (ListUtils.isEmpty((List) list)) {
            return null;
        }
        for (ChannelModel homeData : list) {
            if ((channelId + "").equals(homeData.getId())) {
                return homeData;
            }
        }
        return null;
    }

    public Channel getChannelById(int channelId) {
        ChannelModel channelModel = getChannelModelById(channelId, getChannelList());
        return channelModel == null ? null : channelModel.getImpData();
    }

    public void setChannelList(List<ChannelModel> channelList) {
        this.mChannelList = channelList;
    }

    public void writeChannelToCache() {
        try {
            SerializableUtils.write(this.mChannelList, HomeDataConfig.HOME_CAROUSEL_CHANNEL_LIST_DIR);
        } catch (IOException e) {
            LogUtils.m1571e(TAG, "write carousel channel failed");
        }
    }
}
