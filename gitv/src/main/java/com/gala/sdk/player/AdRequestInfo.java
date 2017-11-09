package com.gala.sdk.player;

public class AdRequestInfo {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;

    public AdRequestInfo setAlbumId(String albumId) {
        this.a = albumId;
        return this;
    }

    public AdRequestInfo setChannelId(String channelId) {
        this.b = channelId;
        return this;
    }

    public AdRequestInfo setTvId(String tvId) {
        this.c = tvId;
        return this;
    }

    public AdRequestInfo setVid(String vid) {
        this.d = vid;
        return this;
    }

    public AdRequestInfo setVideoViewedDuration(String vd) {
        this.e = vd;
        return this;
    }

    public AdRequestInfo setVideoViewedCount(String vn) {
        this.f = vn;
        return this;
    }

    public AdRequestInfo setSdkVersion(String sdkVersion) {
        this.g = sdkVersion;
        return this;
    }

    public AdRequestInfo setAdType(String adType) {
        this.h = adType;
        return this;
    }

    public AdRequestInfo setRequestFutureAd(String fa) {
        this.i = fa;
        return this;
    }

    public AdRequestInfo setAdStartTime(String startTime) {
        this.j = startTime;
        return this;
    }

    public AdRequestInfo setSequenceId(String sequenceId) {
        this.k = sequenceId;
        return this;
    }

    public AdRequestInfo setPlayAction(String playAction) {
        this.l = playAction;
        return this;
    }

    public AdRequestInfo setCurrentPlayTime(String playTime) {
        this.m = playTime;
        return this;
    }

    public String getAlbumId() {
        return this.a;
    }

    public String getChannelId() {
        return this.b;
    }

    public String getTvId() {
        return this.c;
    }

    public String getVid() {
        return this.d;
    }

    public String getVideoViewedDuration() {
        return this.e;
    }

    public String getVideoViewedCount() {
        return this.f;
    }

    public String getSdkVersion() {
        return this.g;
    }

    public String getAdType() {
        return this.h;
    }

    public String getRequestFutureAd() {
        return this.i;
    }

    public String getAdStartTime() {
        return this.j;
    }

    public String getSequenceId() {
        return this.k;
    }

    public String getPlayAction() {
        return this.l;
    }

    public String getCurrentPlayTime() {
        return this.m;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AdRequestInfo@").append(Integer.toHexString(hashCode())).append("{mVrsAlbumId=").append(this.a).append(", mVrsChannelId=").append(this.b).append(", mVrsTvId=").append(this.c).append(", mVrsVid=").append(this.d).append(", mVd=").append(this.e).append(", mVn=").append(this.f).append(", mSdkVersion=").append(this.g).append(", mAdType=").append(this.h).append(", mFa=").append(this.i).append(", mStartTime=").append(this.j).append(", mSequenceId=").append(this.k).append(", mPlayAction=").append(this.l).append(", mPlayTime=").append(this.m).append("}");
        return stringBuilder.toString();
    }
}
