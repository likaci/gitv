package com.gala.sdk.player;

public class AdRequestInfo {
    private String f634a;
    private String f635b;
    private String f636c;
    private String f637d;
    private String f638e;
    private String f639f;
    private String f640g;
    private String f641h;
    private String f642i;
    private String f643j;
    private String f644k;
    private String f645l;
    private String f646m;

    public AdRequestInfo setAlbumId(String albumId) {
        this.f634a = albumId;
        return this;
    }

    public AdRequestInfo setChannelId(String channelId) {
        this.f635b = channelId;
        return this;
    }

    public AdRequestInfo setTvId(String tvId) {
        this.f636c = tvId;
        return this;
    }

    public AdRequestInfo setVid(String vid) {
        this.f637d = vid;
        return this;
    }

    public AdRequestInfo setVideoViewedDuration(String vd) {
        this.f638e = vd;
        return this;
    }

    public AdRequestInfo setVideoViewedCount(String vn) {
        this.f639f = vn;
        return this;
    }

    public AdRequestInfo setSdkVersion(String sdkVersion) {
        this.f640g = sdkVersion;
        return this;
    }

    public AdRequestInfo setAdType(String adType) {
        this.f641h = adType;
        return this;
    }

    public AdRequestInfo setRequestFutureAd(String fa) {
        this.f642i = fa;
        return this;
    }

    public AdRequestInfo setAdStartTime(String startTime) {
        this.f643j = startTime;
        return this;
    }

    public AdRequestInfo setSequenceId(String sequenceId) {
        this.f644k = sequenceId;
        return this;
    }

    public AdRequestInfo setPlayAction(String playAction) {
        this.f645l = playAction;
        return this;
    }

    public AdRequestInfo setCurrentPlayTime(String playTime) {
        this.f646m = playTime;
        return this;
    }

    public String getAlbumId() {
        return this.f634a;
    }

    public String getChannelId() {
        return this.f635b;
    }

    public String getTvId() {
        return this.f636c;
    }

    public String getVid() {
        return this.f637d;
    }

    public String getVideoViewedDuration() {
        return this.f638e;
    }

    public String getVideoViewedCount() {
        return this.f639f;
    }

    public String getSdkVersion() {
        return this.f640g;
    }

    public String getAdType() {
        return this.f641h;
    }

    public String getRequestFutureAd() {
        return this.f642i;
    }

    public String getAdStartTime() {
        return this.f643j;
    }

    public String getSequenceId() {
        return this.f644k;
    }

    public String getPlayAction() {
        return this.f645l;
    }

    public String getCurrentPlayTime() {
        return this.f646m;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AdRequestInfo@").append(Integer.toHexString(hashCode())).append("{mVrsAlbumId=").append(this.f634a).append(", mVrsChannelId=").append(this.f635b).append(", mVrsTvId=").append(this.f636c).append(", mVrsVid=").append(this.f637d).append(", mVd=").append(this.f638e).append(", mVn=").append(this.f639f).append(", mSdkVersion=").append(this.f640g).append(", mAdType=").append(this.f641h).append(", mFa=").append(this.f642i).append(", mStartTime=").append(this.f643j).append(", mSequenceId=").append(this.f644k).append(", mPlayAction=").append(this.f645l).append(", mPlayTime=").append(this.f646m).append("}");
        return stringBuilder.toString();
    }
}
