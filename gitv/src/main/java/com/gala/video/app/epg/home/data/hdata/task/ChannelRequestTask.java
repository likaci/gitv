package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.result.ApiResultChannelList;
import com.gala.tvapi.type.ChannelType;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.model.ChannelModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.ChannelProvider;
import com.gala.video.app.epg.ui.albumlist.common.AlbumProviderHelper;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChannelRequestTask extends BaseRequestTask {
    public static final int CHANNEL_4K = 10002;
    public static final int CHANNEL_7DAY = 100004;
    public static final int CHANNEL_CINEMA = 100003;
    public static final int CHANNEL_HOT = 10009;
    public static final int CHANNEL_RECENT_UPDATE = 100004;
    public static final int DAILYNEWS_CHANNEL = 10007;
    public static final String LIVE_CHANNEL = "1000004";
    private static final String PAGE_NO = "1";
    private static final String PAGE_SIZE = "60";
    public static final int SUBJECT_REVIEW = 10008;
    private static final String TAG = "home/ChannelTask";
    public static final String VIP_CHANNEL = "1000002";
    private static final List<String> mFixChannel = new ArrayList();
    private TaskInput mInput;

    static {
        mFixChannel.add(String.valueOf(100003));
        mFixChannel.add(String.valueOf(100004));
        mFixChannel.add(String.valueOf(10009));
        mFixChannel.add(String.valueOf(10008));
        mFixChannel.add(String.valueOf(10007));
        mFixChannel.add(String.valueOf(100004));
    }

    public ChannelRequestTask(TaskInput input) {
        this.mInput = input;
    }

    public void invoke() {
        LogUtils.d(TAG, "invoke channel task input : " + this.mInput);
        String version = Project.getInstance().getBuild().getVersionString();
        TVApi.channelList.callSync(new IApiCallback<ApiResultChannelList>() {
            public void onSuccess(ApiResultChannelList result) {
                ChannelRequestTask.this.saveChannels(result.data);
                LogUtils.e(ChannelRequestTask.TAG, "request channel list success");
            }

            public void onException(ApiException e) {
                String str;
                LogRecordUtils.setEventID(PingBackUtils.createEventId());
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
                String str2 = "pfec";
                if (e == null) {
                    str = "";
                } else {
                    str = e.getCode();
                }
                addItem.addItem(str2, str).addItem(Keys.ERRURL, "").addItem(Keys.APINAME, "channelList").addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
                if (e != null) {
                    LogUtils.e(ChannelRequestTask.TAG, "request channel list failed code = " + e.getCode());
                }
            }
        }, version, "1", PAGE_SIZE);
    }

    public synchronized void saveChannels(List<Channel> list) {
        if (list == null) {
            LogUtils.e(TAG, "saveChannelInfo()---list=null");
        } else {
            if (GetInterfaceTools.getPlayerConfigProvider().isDisable4KH264()) {
                mFixChannel.add(String.valueOf(10002));
            }
            AlbumProviderHelper.initAlbumProvider(list);
            List<ChannelModel> dataModels = new ArrayList();
            for (Channel channel : list) {
                ChannelModel data = new ChannelModel(channel);
                if (!mFixChannel.contains(channel.id)) {
                    if (channel.id.equals("1000002") || channel.id.equals("1000004")) {
                        dataModels.add(data);
                    } else if (channel.getChannelType() != ChannelType.FUNCTION_CHANNEL) {
                        dataModels.add(data);
                    }
                }
            }
            ChannelProvider.getInstance().setChannelList(dataModels);
            try {
                SerializableUtils.write(dataModels, HomeDataConfig.HOME_CHANNEL_LIST_DIR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onOneTaskFinished() {
        LogUtils.d(TAG, "channel task request finished");
        HomeDataObservable.getInstance().post(HomeDataType.CHANNEL, WidgetChangeStatus.DataChange, null);
    }
}
