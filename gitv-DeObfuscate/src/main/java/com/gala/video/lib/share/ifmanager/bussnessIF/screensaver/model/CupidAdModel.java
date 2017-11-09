package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.mcto.ads.constants.ClickThroughType;

public class CupidAdModel {
    private static final String TAG = "ads/model/CupidAdModel";
    public AdClickType mAdClickType = AdClickType.NONE;
    public int mAdId;
    public String mAdImageUrl = "";
    public String mAlbumId = "";
    public String mCarouselId = "";
    public String mCarouselName = "";
    public String mCarouselNo = "";
    public String mClickThroughInfo = "";
    public ClickThroughType mClickThroughType = null;
    public String mDefault = "";
    public String mH5Url = "";
    public String mJumpingShowImageUrl = "";
    public String mPlId = "";
    public String mTvId = "";

    public int getAdId() {
        return this.mAdId;
    }

    public void setAdId(int adId) {
        this.mAdId = adId;
    }

    public ClickThroughType getClickThroughType() {
        return this.mClickThroughType;
    }

    public void setClickThroughType(ClickThroughType clickThroughType) {
        this.mClickThroughType = clickThroughType;
    }

    public AdClickType getAdClickType() {
        return this.mAdClickType;
    }

    public void setAdClickType(AdClickType adClickType) {
        this.mAdClickType = adClickType;
    }

    public String getAdImageUrl() {
        return this.mAdImageUrl;
    }

    public void setAdImageUrl(String adImageUrl) {
        this.mAdImageUrl = adImageUrl;
    }

    public String getClickThroughInfo() {
        return this.mClickThroughInfo;
    }

    public void setClickThroughInfo(String clickThroughInfo) {
        this.mClickThroughInfo = clickThroughInfo;
    }

    public String getDefault() {
        return this.mDefault;
    }

    public void setDefault(String aDefault) {
        this.mDefault = aDefault;
    }

    public String getH5Url() {
        return this.mH5Url;
    }

    public void setH5Url(String h5Url) {
        this.mH5Url = h5Url;
    }

    public String getJumpingShowImageUrl() {
        return this.mJumpingShowImageUrl;
    }

    public void setJumpingShowImageUrl(String jumpingShowImageUrl) {
        this.mJumpingShowImageUrl = jumpingShowImageUrl;
    }

    public String getAlbumId() {
        return this.mAlbumId;
    }

    public void setAlbumId(String albumId) {
        this.mAlbumId = albumId;
    }

    public String getTvId() {
        return this.mTvId;
    }

    public void setTvId(String tvId) {
        this.mTvId = tvId;
    }

    public String getPlId() {
        return this.mPlId;
    }

    public void setPlId(String plId) {
        this.mPlId = plId;
    }

    public String getCarouselId() {
        return this.mCarouselId;
    }

    public void setCarouselId(String carouselId) {
        this.mCarouselId = carouselId;
    }

    public String getCarouselNo() {
        return this.mCarouselNo;
    }

    public void setCarouselNo(String carouselNo) {
        this.mCarouselNo = carouselNo;
    }

    public String getCarouselName() {
        return this.mCarouselName;
    }

    public void setCarouselName(String carouselName) {
        this.mCarouselName = carouselName;
    }

    public boolean isEnableJumping() {
        if (this.mAdClickType == null) {
            return false;
        }
        switch (this.mAdClickType) {
            case NONE:
                return false;
            case H5:
                if (StringUtils.isEmpty(this.mH5Url)) {
                    return false;
                }
                return true;
            case DEFAULT:
                if (StringUtils.isEmpty(this.mDefault)) {
                    return false;
                }
                return true;
            case IMAGE:
                if (StringUtils.isEmpty(this.mJumpingShowImageUrl)) {
                    return false;
                }
                return true;
            case PLAY_LIST:
                if (StringUtils.isEmpty(this.mPlId)) {
                    return false;
                }
                return true;
            case VIDEO:
                if (StringUtils.isEmpty(this.mTvId) || StringUtils.isEmpty(this.mAlbumId)) {
                    return false;
                }
                return true;
            case CAROUSEL:
                if (StringUtils.isEmpty(this.mCarouselId) || StringUtils.isEmpty(this.mCarouselName) || StringUtils.isEmpty(this.mCarouselNo)) {
                    return false;
                }
                return true;
            default:
                LogUtils.m1577w(TAG, "isEnableJumping, ad click type : " + this.mAdClickType);
                return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CupidAdModel{");
        sb.append("mAdId=").append(this.mAdId);
        sb.append(", mClickThroughType=").append(this.mClickThroughType);
        sb.append(", mAdClickType=").append(this.mAdClickType);
        sb.append(", mAdImageUrl='").append(this.mAdImageUrl).append('\'');
        sb.append(", mClickThroughInfo='").append(this.mClickThroughInfo).append('\'');
        sb.append(", mH5Url='").append(this.mH5Url).append('\'');
        sb.append(", mJumpingShowImageUrl='").append(this.mJumpingShowImageUrl).append('\'');
        sb.append(", mAlbumId='").append(this.mAlbumId).append('\'');
        sb.append(", mTvId='").append(this.mTvId).append('\'');
        sb.append(", mPlId='").append(this.mPlId).append('\'');
        sb.append(", mCarouselId='").append(this.mCarouselId).append('\'');
        sb.append(", mCarouselNo='").append(this.mCarouselNo).append('\'');
        sb.append(", mCarouselName='").append(this.mCarouselName).append('\'');
        sb.append(", mDefault='").append(this.mDefault).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
