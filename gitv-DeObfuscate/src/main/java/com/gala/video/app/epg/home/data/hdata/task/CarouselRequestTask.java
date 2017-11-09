package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.result.ApiResultTVChannelListCarousel;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.model.ChannelModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.CarouselChannelProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.Precondition;
import java.util.ArrayList;
import java.util.List;

public class CarouselRequestTask extends BaseRequestTask {
    private static final String TAG = "home/CarouselRequestTask";
    private static String mTime = "0";
    private List<ChannelModel> mDataModels = new ArrayList();

    class C06291 implements IApiCallback<ApiResultTVChannelListCarousel> {
        C06291() {
        }

        public void onSuccess(ApiResultTVChannelListCarousel apiResultTVChannelListCarousel) {
            if (apiResultTVChannelListCarousel != null && !Precondition.isEmpty(apiResultTVChannelListCarousel.data)) {
                for (TVChannelCarousel channel : apiResultTVChannelListCarousel.data) {
                    CarouselRequestTask.this.mDataModels.add(new ChannelModel(channel));
                }
                CarouselRequestTask.mTime = String.valueOf(apiResultTVChannelListCarousel.f1051t);
                CarouselChannelProvider.getInstance().setChannelList(CarouselRequestTask.this.mDataModels);
                CarouselChannelProvider.getInstance().writeChannelToCache();
            }
        }

        public void onException(ApiException e) {
            CarouselRequestTask.mTime = "0";
            HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", e.getCode()).addItem(Keys.ERRURL, e.getUrl()).addItem(Keys.APINAME, "channelCarousel").addItem(Keys.ERRDETAIL, e.getMessage()).addItem("activity", "HomeActivity").addItem(Keys.f2035T, "0").setOthersNull().post();
        }
    }

    public void invoke() {
        if (Project.getInstance().getControl().isOpenCarousel()) {
            this.mDataModels.clear();
            fetchData();
        }
    }

    private void fetchData() {
        if (Precondition.isEmpty(CarouselChannelProvider.getInstance().getChannelList())) {
            mTime = "0";
        }
        TVApi.channelCarousel.callSync(new C06291(), "2", "0", mTime, TVApiBase.getTVApiProperty().getVersion(), "1.0");
    }

    public void onOneTaskFinished() {
        if (Precondition.isEmpty(CarouselChannelProvider.getInstance().getChannelList())) {
            mTime = "0";
            HomeDataObservable.getInstance().post(HomeDataType.CAROUSEL_CHANNEL, WidgetChangeStatus.NoData, null);
            return;
        }
        HomeDataObservable.getInstance().post(HomeDataType.CAROUSEL_CHANNEL, WidgetChangeStatus.DataChange, null);
    }
}
