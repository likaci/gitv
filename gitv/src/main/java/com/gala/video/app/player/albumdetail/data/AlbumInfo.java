package com.gala.video.app.player.albumdetail.data;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Cast;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.tvapi.type.AlbumType;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlbumInfo {
    private String TAG = "AlbumInfo";
    private final Object lock = new Object();
    private Album mAlbum = new Album();
    private String mAlbumDetailPic;
    private String mAlbumOriginPrice = "";
    private String mAlbumPrice = "";
    private String mCouponCount = "";
    private int mEpisodeMaxOrder;
    private int mEpisodeTotalCount;
    private List<AlbumInfo> mEpisodeVideos = new CopyOnWriteArrayList();
    private boolean mIsAlbumSinglePay = false;
    private boolean mIsFavored;
    private boolean mIsVipAuthorized;

    public enum VideoKind {
        VIDEO_SINGLE,
        VIDEO_SOURCE,
        VIDEO_EPISODE,
        ALBUM_EPISODE,
        VideoKind,
        ALBUM_SOURCE
    }

    public int getEpisodeMaxOrder() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getEpisodeMaxOrder " + this.mEpisodeMaxOrder);
        }
        return this.mEpisodeMaxOrder;
    }

    public void setEpisodeMaxOrder(int order) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "maxOrder is " + this.mEpisodeMaxOrder + "order = " + order);
        }
        if (this.mEpisodeMaxOrder < order) {
            this.mEpisodeMaxOrder = order;
        }
    }

    public void setEpisodes(List<Episode> episodes, int totalCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setEpisodes: episodes.size= " + episodes.size() + " mAlbumDetailPic " + this.mAlbumDetailPic + "  )" + ListUtils.getCount((List) episodes) + " mEpisodeVideos " + ListUtils.getCount(this.mEpisodeVideos) + " totalCount " + totalCount);
        }
        if (ListUtils.getCount(this.mEpisodeVideos) <= ListUtils.getCount((List) episodes)) {
            this.mEpisodeTotalCount = totalCount;
            if (ListUtils.getCount((List) episodes) == 0) {
                this.mEpisodeVideos.clear();
                return;
            }
            List<AlbumInfo> mTempEpisodeVideos = new ArrayList();
            for (Episode ep : episodes) {
                Album album = new Album(ep);
                if (this.mAlbum != null) {
                    album.qpId = getAlbumId();
                    album.sourceCode = getSourceCode();
                    album.chnId = getChannelId();
                    album.tvCount = getTvCount();
                    album.tvsets = getTvSets();
                    album.tvPic = album.pic;
                    album.pic = this.mAlbumDetailPic;
                }
                mTempEpisodeVideos.add(new AlbumInfo(album));
            }
            synchronized (this.lock) {
                this.mEpisodeVideos.clear();
                this.mEpisodeVideos.addAll(mTempEpisodeVideos);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setEpisodes not opreate, maybe cache to current");
        }
    }

    public void clearEpisodeVideos() {
        this.mEpisodeVideos.clear();
        this.mEpisodeTotalCount = 0;
        this.mEpisodeMaxOrder = 0;
    }

    public List<AlbumInfo> getEpisodeVideos() {
        ArrayList<AlbumInfo> list;
        synchronized (this.lock) {
            list = new ArrayList(this.mEpisodeVideos);
        }
        return list;
    }

    public int getTvSets() {
        return this.mAlbum.tvsets;
    }

    public int getEpisodesTotalCount() {
        return this.mEpisodeTotalCount;
    }

    public int getTvCount() {
        return this.mAlbum.tvCount;
    }

    public void setAlbumOriginPrice(String arg0) {
        this.mAlbumOriginPrice = arg0;
    }

    public void setAlbumPrice(String arg0) {
        this.mAlbumPrice = arg0;
    }

    public String getAlbumOriginPrice() {
        return this.mAlbumOriginPrice;
    }

    public String getAlbumPrice() {
        return this.mAlbumPrice;
    }

    public void setAlbumSinglePay(boolean arg0) {
        this.mIsAlbumSinglePay = arg0;
    }

    public boolean isAlbumSinglePay() {
        return (!this.mAlbum.isSinglePay() || this.mAlbum.isVipForAccount() || this.mAlbum.isCoupon()) ? false : true;
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }

    public AlbumInfo(Album album) {
        this.mAlbum = album;
    }

    public String getAlbumId() {
        return this.mAlbum.qpId;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public boolean isAlbumVip() {
        return this.mAlbum.isVipForAccount() && !this.mAlbum.isCoupon();
    }

    public int getSuperId() {
        return this.mAlbum.superId;
    }

    public boolean isAlbumCoupon() {
        return this.mAlbum.isCoupon();
    }

    public boolean isTvSeries() {
        return this.mAlbum.isSeries() && !this.mAlbum.isSourceType();
    }

    public void setFavored(boolean isFavored) {
        this.mIsFavored = isFavored;
    }

    public boolean isFavored() {
        return this.mIsFavored;
    }

    public int getChannelId() {
        return this.mAlbum.chnId;
    }

    public String getSourceCode() {
        return this.mAlbum.sourceCode;
    }

    public boolean isSourceType() {
        return this.mAlbum.isSourceType();
    }

    public String getStreamVer() {
        return this.mAlbum.stream;
    }

    public String getTvId() {
        return this.mAlbum.tvQid;
    }

    public void setTvId(String tvQid) {
        this.mAlbum.tvQid = tvQid;
    }

    public String getVid() {
        return this.mAlbum.vid;
    }

    public boolean is3d() {
        return this.mAlbum.is3D();
    }

    public String getAlbumDesc() {
        return this.mAlbum.desc;
    }

    public boolean isVipAuthorized() {
        return this.mIsVipAuthorized;
    }

    public void setVipAuthorized(boolean isVipAuthorized) {
        this.mIsVipAuthorized = isVipAuthorized;
    }

    public void setCouponCount(String arg0) {
        this.mCouponCount = arg0;
    }

    public String getCouponCount() {
        return this.mCouponCount;
    }

    public String getAlbumSubName() {
        return this.mAlbum == null ? null : this.mAlbum.getAlbumSubName();
    }

    public String getScore() {
        return this.mAlbum.getScore();
    }

    public boolean isSeries() {
        return this.mAlbum.isSeries();
    }

    public String getPlayCount() {
        return this.mAlbum.pCount;
    }

    public String getDirector() {
        return (this.mAlbum == null || this.mAlbum.cast == null) ? null : this.mAlbum.cast.director;
    }

    public String getMainActor() {
        return (this.mAlbum == null || this.mAlbum.cast == null) ? null : this.mAlbum.cast.mainActor;
    }

    public AlbumType getAlbumType() {
        return this.mAlbum.getType();
    }

    public String getAlbumPic() {
        return this.mAlbum.pic;
    }

    public String getAlbumDetailPic() {
        return this.mAlbumDetailPic;
    }

    public void setAlbumDetailPic(String albumPic) {
        this.mAlbumDetailPic = albumPic;
        this.mAlbum.pic = albumPic;
    }

    public int getPlayOrder() {
        int playOrder = -1;
        if (isTvSeries()) {
            playOrder = this.mAlbum.order;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getPlayOrder() return " + playOrder + ", series=" + isTvSeries());
        }
        return playOrder;
    }

    public void setPlayOrder(int order) {
        if (isTvSeries()) {
            this.mAlbum.order = order;
        }
    }

    public Cast getCast() {
        return this.mAlbum.cast;
    }

    public String getSuperName() {
        return this.mAlbum.superName;
    }

    public VideoKind getKind() {
        return getKind(this.mAlbum);
    }

    public VideoKind getKind(Album album) {
        if (album == null) {
            return VideoKind.VIDEO_SINGLE;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getVideoAlbumKind() isSeries" + album.isSeries() + "album = " + getAlbumType() + ", album.type=" + album.type + "album.isSourceType()" + album.isSourceType());
        }
        if (album.type == AlbumType.VIDEO.getValue()) {
            if (!album.isSeries() && !album.isSourceType()) {
                return VideoKind.VIDEO_SINGLE;
            }
            if (!album.isSeries() || album.isSourceType()) {
                return VideoKind.VIDEO_SOURCE;
            }
            return VideoKind.VIDEO_EPISODE;
        } else if (album.type != AlbumType.ALBUM.getValue()) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "getVideoAlbumKind  unhanlded  albumType" + getAlbumType());
            }
            return VideoKind.VIDEO_SINGLE;
        } else if (album.isSourceType()) {
            return VideoKind.ALBUM_SOURCE;
        } else {
            return VideoKind.ALBUM_EPISODE;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AlbumInfo@").append(this.TAG).append("{");
        builder.append("hashCode=").append(hashCode());
        builder.append("qpId=").append(this.mAlbum.qpId);
        builder.append(", tvQid=").append(this.mAlbum.tvQid);
        builder.append(", getType()=").append(this.mAlbum.getType());
        builder.append(", tvName=").append(this.mAlbum.tvName);
        builder.append(", isSeries()=").append(this.mAlbum.isSeries());
        builder.append(", sourceCode=").append(this.mAlbum.sourceCode);
        builder.append(", playOrder=").append(this.mAlbum.order);
        builder.append(", getAlbumSubName()=").append(this.mAlbum.getAlbumSubName());
        builder.append(", isPurchase()=").append(this.mAlbum.isPurchase());
        builder.append(", isExclusive()=").append(this.mAlbum.isExclusivePlay());
        builder.append(", pic=").append(this.mAlbum.pic);
        builder.append(", tvPic=").append(this.mAlbum.tvPic);
        builder.append(", issueTime=").append(this.mAlbum.time);
        builder.append(", playCount=").append(this.mAlbum.pCount);
        builder.append(", playLength=").append(this.mAlbum.len);
        builder.append(", definition list=").append(this.mAlbum.stream);
        builder.append(", default definition=").append(this.mAlbum.vid);
        builder.append(", channelId=").append(this.mAlbum.chnId);
        builder.append(", channelName=").append(this.mAlbum.chnName);
        builder.append(", liveChannelId=").append(this.mAlbum.live_channelId);
        builder.append(", startTime=").append(this.mAlbum.startTime);
        builder.append(", endTime=").append(this.mAlbum.endTime);
        builder.append(", tvCount=").append(this.mAlbum.tvCount);
        builder.append(", tvSets=").append(this.mAlbum.tvsets);
        builder.append(", playTime=").append(this.mAlbum.playTime <= 0 ? this.mAlbum.playTime : this.mAlbum.playTime * 1000);
        builder.append(", url=").append(this.mAlbum.url);
        builder.append(", isSinglePay=").append(this.mAlbum.isSinglePay());
        builder.append(", episodes=").append(this.mEpisodeVideos == null ? "-1" : Integer.valueOf(this.mEpisodeVideos.size()));
        builder.append(", episodeMaxOrder=").append(this.mEpisodeMaxOrder);
        builder.append(", isVipForAccount=").append(this.mAlbum.isVipForAccount());
        builder.append(", isCoupon=").append(this.mAlbum.isCoupon());
        builder.append(", getContentType=").append(this.mAlbum.getContentType());
        builder.append(", drmType=").append(this.mAlbum.getDrmType());
        builder.append(", dynamicRange=").append(this.mAlbum.dynamicRanges);
        builder.append(", desc=").append(this.mAlbum.desc);
        builder.append(", time=").append(this.mAlbum.time);
        builder.append("}");
        return builder.toString();
    }

    public String toStringBrief() {
        StringBuilder builder = new StringBuilder();
        builder.append("AlbumInfo@").append(this.TAG).append("{");
        builder.append("qpId=").append(this.mAlbum.qpId);
        builder.append(", tvQid=").append(this.mAlbum.tvQid);
        builder.append(", getType()=").append(this.mAlbum.getType());
        builder.append(", tvName=").append(this.mAlbum.tvName);
        builder.append(", isSeries()=").append(this.mAlbum.isSeries());
        builder.append(", sourceCode=").append(this.mAlbum.sourceCode);
        builder.append(", playOrder=").append(this.mAlbum.order);
        builder.append(", liveChannelId=").append(this.mAlbum.live_channelId);
        builder.append(", playTime=").append(this.mAlbum.playTime);
        builder.append(", getContentType=").append(this.mAlbum.getContentType());
        builder.append(", drmType=").append(this.mAlbum.getDrmType());
        builder.append(", dynamicRange=").append(this.mAlbum.dynamicRanges);
        builder.append("}");
        return builder.toString();
    }
}
