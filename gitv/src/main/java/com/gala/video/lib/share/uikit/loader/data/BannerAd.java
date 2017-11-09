package com.gala.video.lib.share.uikit.loader.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.mcto.ads.constants.ClickThroughType;
import java.io.Serializable;

public class BannerAd implements Serializable {
    public AdClickType adClickType = AdClickType.NONE;
    public int adId;
    public String albumId = "";
    public String carouselId = "";
    public String carouselName = "";
    public String carouselNo = "";
    public String clickThroughInfo = "";
    public ClickThroughType clickThroughType = null;
    public String imageUrl = "";
    public String mAdZoneId;
    public boolean needAdBadge;
    public String plId = "";
    public String title = "";
    public String tvId = "";
}
