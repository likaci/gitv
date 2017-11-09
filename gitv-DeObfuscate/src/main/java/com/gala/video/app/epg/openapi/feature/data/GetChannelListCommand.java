package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultChannelList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.ui.albumlist.common.AlbumProviderHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.List;

public class GetChannelListCommand extends ServerCommand<List<Channel>> {
    private static final String TAG = "GetChannelListCommand";
    private int[] mChannelIds = null;

    private class MyListener extends ResultListHolder<Channel> implements IApiCallback<ApiResultChannelList> {
        private MyListener() {
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(GetChannelListCommand.TAG, "onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultChannelList channelList) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(GetChannelListCommand.TAG, "onSuccess(" + channelList + ")" + ", channelIds = " + GetChannelListCommand.this.mChannelIds);
            }
            setNetworkValid(true);
            if (channelList.data != null) {
                AlbumProviderHelper.initAlbumProvider(channelList.data);
                for (com.gala.tvapi.tv2.model.Channel channel : channelList.data) {
                    if (GetChannelListCommand.this.mChannelIds == null || GetChannelListCommand.this.containChannelId(StringUtils.parse(channel.id, -1))) {
                        add(OpenApiUtils.createSdkChannel(channel));
                    }
                }
            }
        }
    }

    public GetChannelListCommand(Context context, int[] channelIds) {
        super(context, TargetType.TARGET_CHANNEL, 20003, DataType.DATA_CHANNEL_LIST);
        setNeedNetwork(true);
        this.mChannelIds = channelIds;
    }

    public GetChannelListCommand(Context context) {
        super(context, TargetType.TARGET_CHANNEL, 20003, DataType.DATA_CHANNEL_LIST);
        setNeedNetwork(true);
        this.mChannelIds = null;
    }

    public Bundle onProcess(Bundle params) {
        MyListener listener = new MyListener();
        TVApi.channelList.callSync(listener, Project.getInstance().getBuild().getVersionString(), "1", "60");
        if (listener.isNetworkValid()) {
            increaseAccessCount();
        }
        return listener.getResult();
    }

    private boolean containChannelId(int channelId) {
        boolean containChannelId = false;
        for (int id : this.mChannelIds) {
            if (channelId == id) {
                containChannelId = true;
                break;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(TAG, "containChannelId() + , channelId = " + channelId + ", contain = " + containChannelId);
        }
        return containChannelId;
    }
}
