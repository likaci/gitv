package com.gala.video.app.epg.home.ads.model;

import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.CupidAdModel;

public class StartAdModel extends CupidAdModel {
    private static final String TAG = "ads/model/StartAdModel";
    private boolean isSkippable = false;
    private String mDuration = "";
    private String mDynamicUrl = "";
    private boolean mNeedAdBadge = true;
    private String mQrDescription = "";
    private String mQrHeightScale = "";
    private String mQrTitle = "";
    private String mRenderType = "image";
    private String mVideoDownloadedUrl = "";

    public boolean isNeedAdBadge() {
        return this.mNeedAdBadge;
    }

    public void setNeedAdBadge(boolean needAdBadge) {
        this.mNeedAdBadge = needAdBadge;
    }

    public String getRenderType() {
        return this.mRenderType;
    }

    public void setRenderType(String renderType) {
        this.mRenderType = renderType;
    }

    public String getQrTitle() {
        return this.mQrTitle;
    }

    public void setQrTitle(String qrTitle) {
        this.mQrTitle = qrTitle;
    }

    public boolean isSkippable() {
        return this.isSkippable;
    }

    public void setSkippable(boolean skippable) {
        this.isSkippable = skippable;
    }

    public String getDynamicUrl() {
        return this.mDynamicUrl;
    }

    public void setDynamicUrl(String dynamicUrl) {
        this.mDynamicUrl = dynamicUrl;
    }

    public String getVideoDownloadedUrl() {
        return this.mVideoDownloadedUrl;
    }

    public void setVideoDownloadedUrl(String videoDownloadedUrl) {
        this.mVideoDownloadedUrl = videoDownloadedUrl;
    }

    public String getDuration() {
        return this.mDuration;
    }

    public void setDuration(String duration) {
        this.mDuration = duration;
    }

    public String getQrDescription() {
        return this.mQrDescription;
    }

    public void setQrDescription(String qrDescription) {
        this.mQrDescription = qrDescription;
    }

    public String getQrHeightScale() {
        return this.mQrHeightScale;
    }

    public void setQrHeightScale(String qrHeightScale) {
        this.mQrHeightScale = qrHeightScale;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StartAdModel{");
        sb.append("mNeedAdBadge=").append(this.mNeedAdBadge);
        sb.append(", mRenderType='").append(this.mRenderType).append('\'');
        sb.append(", mQrTitle='").append(this.mQrTitle).append('\'');
        sb.append(", isSkippable=").append(this.isSkippable);
        sb.append(", mDynamicUrl='").append(this.mDynamicUrl).append('\'');
        sb.append(", mVideoDownloadedUrl='").append(this.mVideoDownloadedUrl).append('\'');
        sb.append(", mDuration='").append(this.mDuration).append('\'');
        sb.append(", mQrDescription='").append(this.mQrDescription).append('\'');
        sb.append(", mQrHeightScale='").append(this.mQrHeightScale).append('\'');
        sb.append('}');
        return super.toString() + sb.toString();
    }
}
