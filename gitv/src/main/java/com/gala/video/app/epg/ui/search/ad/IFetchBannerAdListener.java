package com.gala.video.app.epg.ui.search.ad;

import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import java.util.List;

public interface IFetchBannerAdListener {
    void onFailed(ApiException apiException);

    void onSendPingback(PingBackParams pingBackParams);

    void onSuccess(List<BannerImageAdModel> list);
}
