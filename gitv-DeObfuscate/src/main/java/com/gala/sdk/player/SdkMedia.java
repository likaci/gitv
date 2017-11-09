package com.gala.sdk.player;

import java.util.Map;

public class SdkMedia implements IMedia {
    private static final long serialVersionUID = -5510097036662219792L;
    private String mAlbumId;
    private int mDrmType;
    private Map<String, Object> mExtra;
    private boolean mIsLive;
    private boolean mIsVip;
    private String mLiveChannelId;
    private String mLiveProgramId;
    private int mLiveType;
    private long mPlayLength;
    private int mStartPosition;
    private String mTvId;
    private int mVideoType;

    public static IMedia createVodInstance(String albumId, String tvId, boolean isVip, int startPosition) {
        IMedia sdkMedia = new SdkMedia();
        sdkMedia.setAlbumId(albumId);
        sdkMedia.setTvId(tvId);
        sdkMedia.setIsVip(isVip);
        sdkMedia.setStartPosition(startPosition);
        sdkMedia.setIsLive(false);
        return sdkMedia;
    }

    public static IMedia createLiveInstance(String liveChannelId, String liveProgramId, int liveType, boolean isVip) {
        IMedia sdkMedia = new SdkMedia();
        sdkMedia.setLiveChannelId(liveChannelId);
        sdkMedia.setLiveProgramId(liveProgramId);
        sdkMedia.setLiveType(liveType);
        sdkMedia.setIsVip(isVip);
        sdkMedia.setIsLive(true);
        return sdkMedia;
    }

    public String getAlbumId() {
        return this.mAlbumId;
    }

    public String getTvId() {
        return this.mTvId;
    }

    public boolean isVip() {
        return this.mIsVip;
    }

    public int getStartPosition() {
        return this.mStartPosition;
    }

    public Map<String, Object> getExtra() {
        return this.mExtra;
    }

    public String getLiveChannelId() {
        return this.mLiveChannelId;
    }

    public String getLiveProgramId() {
        return this.mLiveProgramId;
    }

    public int getMediaType() {
        return this.mVideoType;
    }

    public int getLiveType() {
        return this.mLiveType;
    }

    public int getDrmType() {
        return this.mDrmType;
    }

    public void setAlbumId(String id) {
        this.mAlbumId = id;
    }

    public void setTvId(String id) {
        this.mTvId = id;
    }

    public void setStartPosition(int pos) {
        this.mStartPosition = pos;
    }

    public void setLiveChannelId(String id) {
        this.mLiveChannelId = id;
    }

    public void setLiveProgramId(String id) {
        this.mLiveProgramId = id;
    }

    public void setLiveType(int liveType) {
        this.mLiveType = liveType;
    }

    public void setIsVip(boolean isVip) {
        this.mIsVip = isVip;
    }

    public void setMediaType(int videoType) {
        this.mVideoType = videoType;
    }

    public boolean isLive() {
        return this.mIsLive;
    }

    public void setDrmType(int type) {
        this.mDrmType = type;
    }

    public void setIsLive(boolean isLive) {
        this.mIsLive = isLive;
    }

    public void setExtra(Map<String, Object> extra) {
        this.mExtra = extra;
    }

    public long getPlayLength() {
        return this.mPlayLength;
    }

    public void setPlayLength(long playLength) {
        this.mPlayLength = playLength;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IMedia@{albumId=").append(this.mAlbumId).append(", tvId=").append(this.mTvId).append(", liveChannelId=").append(this.mLiveChannelId).append(", liveProgramId=").append(this.mLiveProgramId).append(", isVip=").append(this.mIsVip).append(", isLive=").append(this.mIsLive).append(", liveType=").append(this.mLiveType).append(", drmType=").append(this.mDrmType).append(", startPosition=").append(this.mStartPosition).append(", videoType=").append(this.mVideoType).append("}");
        return stringBuilder.toString();
    }

    public boolean isOffline() {
        return false;
    }
}
