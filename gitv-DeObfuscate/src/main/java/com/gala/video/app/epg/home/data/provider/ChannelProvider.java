package com.gala.video.app.epg.home.data.provider;

import android.util.Log;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.app.epg.home.data.model.ChannelModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import java.io.IOException;
import java.util.List;

public class ChannelProvider {
    private static final String TAG = "ChannelProvider";
    private static final ChannelProvider sChannelProvider = new ChannelProvider();
    private List<ChannelModel> mChannelList;

    private ChannelProvider() {
    }

    public static ChannelProvider getInstance() {
        return sChannelProvider;
    }

    public List<ChannelModel> getChannelList() {
        if (ListUtils.isEmpty(this.mChannelList)) {
            try {
                this.mChannelList = (List) SerializableUtils.read(HomeDataConfig.HOME_CHANNEL_LIST_DIR);
            } catch (Exception e) {
                Log.e(TAG, "read channel list failed. " + e);
            }
        }
        return this.mChannelList;
    }

    private ChannelModel getChannelModelById(int channelId) {
        if (ListUtils.isEmpty(this.mChannelList)) {
            this.mChannelList = getChannelList();
        }
        if (!ListUtils.isEmpty(this.mChannelList)) {
            for (ChannelModel homeData : this.mChannelList) {
                if ((channelId + "").equals(homeData.getId())) {
                    return homeData;
                }
            }
        }
        return null;
    }

    public Channel getChannelById(int channelId) {
        ChannelModel channelModel = getChannelModelById(channelId);
        return channelModel == null ? null : channelModel.getImpData();
    }

    public void setChannelList(List<ChannelModel> channelList) {
        this.mChannelList = channelList;
        try {
            SerializableUtils.write(channelList, HomeDataConfig.HOME_CHANNEL_LIST_DIR);
        } catch (IOException e) {
            Log.e(TAG, "write channel list failed. " + e);
        }
    }
}
