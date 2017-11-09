package com.gala.video.lib.share.uikit.loader.data;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.result.ApiResultChannelList;
import com.gala.tvapi.type.ChannelType;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.loader.IUikitDataFetcherCallback;
import java.util.ArrayList;
import java.util.List;

public class ChannelListRequst {
    public static final int CHANNEL_4K = 10002;
    public static final int CHANNEL_7DAY = 100004;
    public static final int CHANNEL_CINEMA = 100003;
    public static final int CHANNEL_HOT = 10009;
    public static final int CHANNEL_RECENT_UPDATE = 100004;
    public static final int DAILYNEWS_CHANNEL = 10007;
    public static final String LIVE_CHANNEL = "1000004";
    public static final int SUBJECT_REVIEW = 10008;
    private static final String TAG = "ChannelListRequst";
    public static final String VIP_CHANNEL = "1000002";
    private static final List<String> mFixChannel = new ArrayList();

    static {
        mFixChannel.add(String.valueOf(100003));
        mFixChannel.add(String.valueOf(100004));
        mFixChannel.add(String.valueOf(10009));
        mFixChannel.add(String.valueOf(10008));
        mFixChannel.add(String.valueOf(10007));
        mFixChannel.add(String.valueOf(100004));
    }

    private static CardInfoModel saveChannels(CardInfoModel model, List<Channel> list) {
        if (list == null) {
            LogUtils.e(TAG, "saveChannelInfo()---list=null");
            return null;
        }
        if (GetInterfaceTools.getPlayerConfigProvider().isDisable4KH264()) {
            mFixChannel.add(String.valueOf(10002));
        }
        List<Channel> channelList = new ArrayList(10);
        for (Channel channel : list) {
            if (!mFixChannel.contains(channel.id)) {
                if (channel.id.equals("1000002") || channel.id.equals("1000004")) {
                    channelList.add(channel);
                } else if (channel.getChannelType() != ChannelType.FUNCTION_CHANNEL) {
                    channelList.add(channel);
                }
            }
        }
        UikitSourceDataCache.writeChannelDataList(channelList);
        return CardInfoBuildTool.buildChannelListCard(model, channelList);
    }

    public static void callChannelList(final CardInfoModel model, final IUikitDataFetcherCallback callback) {
        TVApi.channelList.callSync(new IApiCallback<ApiResultChannelList>() {
            public void onSuccess(ApiResultChannelList result) {
                CardInfoModel cardInfoModel = ChannelListRequst.saveChannels(model, result.data);
                if (cardInfoModel != null) {
                    LogUtils.d(ChannelListRequst.TAG, "request channel list success");
                    List<CardInfoModel> cardInfoModelList = new ArrayList(1);
                    cardInfoModelList.add(cardInfoModel);
                    callback.onSuccess(cardInfoModelList, "");
                    return;
                }
                callback.onFailed();
            }

            public void onException(ApiException e) {
                callback.onFailed();
            }
        }, Project.getInstance().getBuild().getVersionString(), "1", "60");
    }
}
