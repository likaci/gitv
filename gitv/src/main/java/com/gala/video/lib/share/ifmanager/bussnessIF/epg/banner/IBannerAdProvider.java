package com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.mcto.ads.AdsClient;
import java.util.List;

public interface IBannerAdProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IBannerAdProvider {
        public Object getInterface() {
            return this;
        }

        public static IBannerAdProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IBannerAdProvider)) {
                return null;
            }
            return (IBannerAdProvider) wrapper;
        }
    }

    List<BannerImageAdModel> fetchBannerAdData();

    String getAdNetworkInfo();

    int getResultId();

    BannerAdResultModel parseAd(AdsClient adsClient, String str);
}
