package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.VipInfo;

public class HistoryInfo {
    private long mAddTime = 0;
    private Album mAlbum = new Album();
    private String mCookie = "";
    private int mIsCommitted = 0;
    private long mUploadTime = 0;

    public static class Builder {
        private long mAddTime = 0;
        private Album mAlbum = new Album();
        private String mCookie = "";
        private int mIsCommitted = 0;
        private long mUploadTime = 0;

        public Builder(String cookie) {
            this.mCookie = cookie;
            this.mAlbum.vipInfo = new VipInfo();
        }

        public Builder album(Album album) {
            if (album != null) {
                this.mAlbum.qpId = album.qpId;
                this.mAlbum.tvQid = album.tvQid;
                this.mAlbum.playTime = album.playTime;
                this.mAlbum.name = album.name;
                this.mAlbum.tvName = album.tvName;
                this.mAlbum.pic = album.pic;
                this.mAlbum.tvPic = album.tvPic;
                this.mAlbum.is3D = album.is3D;
                this.mAlbum.isPurchase = album.isPurchase;
                this.mAlbum.stream = album.stream;
                this.mAlbum.exclusive = album.exclusive;
                this.mAlbum.isSeries = album.isSeries;
                this.mAlbum.indiviDemand = album.indiviDemand;
                this.mAlbum.chnId = album.chnId;
                this.mAlbum.vid = album.vid;
                this.mAlbum.order = album.order;
                this.mAlbum.type = 0;
                this.mAlbum.tvsets = album.tvsets;
                this.mAlbum.sourceCode = album.sourceCode;
                this.mAlbum.len = album.len;
                this.mAlbum.time = album.time;
                this.mAlbum.vipInfo = album.vipInfo;
                this.mAlbum.tvCount = album.tvCount;
                this.mAlbum.payMarkType = album.payMarkType;
                this.mAlbum.drm = album.drm;
                this.mAlbum.shortName = album.shortName;
                this.mAlbum.contentType = album.contentType;
                this.mAlbum.dynamicRanges = album.dynamicRanges;
            }
            return this;
        }

        public Builder albumId(String albumid) {
            this.mAlbum.qpId = albumid;
            return this;
        }

        public Builder tvId(String tvid) {
            this.mAlbum.tvQid = tvid;
            return this;
        }

        public Builder addedTime(long addedTime) {
            this.mAddTime = addedTime;
            this.mAlbum.addTime = String.valueOf(this.mAddTime);
            return this;
        }

        public Builder playTime(int playTime) {
            this.mAlbum.playTime = playTime;
            return this;
        }

        public Builder name(String name) {
            this.mAlbum.name = name;
            return this;
        }

        public Builder tvName(String tvName) {
            this.mAlbum.tvName = tvName;
            return this;
        }

        public Builder pic(String pic) {
            this.mAlbum.pic = pic;
            return this;
        }

        public Builder tvPic(String tvPic) {
            this.mAlbum.tvPic = tvPic;
            return this;
        }

        public Builder is3D(int is3D) {
            this.mAlbum.is3D = is3D;
            return this;
        }

        public Builder isPurchase(int isPurchase) {
            this.mAlbum.isPurchase = isPurchase;
            return this;
        }

        public Builder stream(String stream) {
            this.mAlbum.stream = stream;
            return this;
        }

        public Builder exclusive(int exclusive) {
            this.mAlbum.exclusive = exclusive;
            return this;
        }

        public Builder isSeries(int isSeries) {
            this.mAlbum.isSeries = isSeries;
            return this;
        }

        public Builder indivDemand(int indivDemand) {
            this.mAlbum.indiviDemand = indivDemand;
            return this;
        }

        public Builder channelId(int channelId) {
            this.mAlbum.chnId = channelId;
            return this;
        }

        public Builder vid(String vid) {
            this.mAlbum.vid = vid;
            return this;
        }

        public Builder playOrder(int order) {
            this.mAlbum.order = order;
            return this;
        }

        public Builder type(int type) {
            this.mAlbum.type = type;
            return this;
        }

        public Builder tvSets(int tvsets) {
            this.mAlbum.tvsets = tvsets;
            return this;
        }

        public Builder sourceCode(String sourceCode) {
            this.mAlbum.sourceCode = sourceCode;
            return this;
        }

        public Builder uploadTime(long uploadTime) {
            this.mUploadTime = uploadTime;
            return this;
        }

        public Builder cookie(String cookie) {
            this.mCookie = cookie;
            return this;
        }

        public Builder duration(String duration) {
            this.mAlbum.len = duration;
            return this;
        }

        public Builder time(String time) {
            this.mAlbum.time = time;
            return this;
        }

        public Builder isCommitted(int isCommitted) {
            this.mIsCommitted = isCommitted;
            return this;
        }

        public Builder isVip(int isVip) {
            this.mAlbum.vipInfo.isVip = isVip;
            return this;
        }

        public Builder isCoupon(int isCoupon) {
            this.mAlbum.vipInfo.isCoupon = isCoupon;
            return this;
        }

        public Builder isTvod(int isTvod) {
            this.mAlbum.vipInfo.isTvod = isTvod;
            return this;
        }

        public Builder isPackage(int isPkg) {
            this.mAlbum.vipInfo.isPkg = isPkg;
            return this;
        }

        public Builder epIsVip(int epIsVip) {
            this.mAlbum.vipInfo.epIsVip = epIsVip;
            return this;
        }

        public Builder epIsCoupon(int epIsCoupon) {
            this.mAlbum.vipInfo.epIsCoupon = epIsCoupon;
            return this;
        }

        public Builder epIsTvod(int isTvod) {
            this.mAlbum.vipInfo.epIsTvod = isTvod;
            return this;
        }

        public Builder epIsPackage(int epIsPackage) {
            this.mAlbum.vipInfo.epIsPkg = epIsPackage;
            return this;
        }

        public Builder payMark(int payMark) {
            this.mAlbum.payMarkType = TVApiTool.getPayMarkType(payMark);
            return this;
        }

        public Builder tvCount(int count) {
            this.mAlbum.tvCount = count;
            return this;
        }

        public Builder drm(String drm) {
            this.mAlbum.drm = drm;
            return this;
        }

        public Builder shortName(String shortName) {
            this.mAlbum.shortName = shortName;
            return this;
        }

        public Builder contentType(int contentType) {
            this.mAlbum.contentType = contentType;
            return this;
        }

        public Builder hdr(String hdr) {
            this.mAlbum.dynamicRanges = hdr;
            return this;
        }

        public HistoryInfo build() {
            return new HistoryInfo(this);
        }
    }

    public HistoryInfo(Builder builder) {
        this.mAlbum = builder.mAlbum;
        this.mCookie = builder.mCookie;
        this.mAddTime = builder.mAddTime;
        this.mUploadTime = builder.mUploadTime;
        this.mIsCommitted = builder.mIsCommitted;
    }

    public long getAddTime() {
        return this.mAddTime;
    }

    public void changeToUserCookie(String cookie) {
        this.mCookie = cookie;
    }

    public long getUploadTime() {
        return this.mUploadTime;
    }

    public String getCookie() {
        return this.mCookie;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public int getPlayTime() {
        return this.mAlbum.playTime;
    }

    public String getTvId() {
        return this.mAlbum.tvQid;
    }

    public String getQpId() {
        return this.mAlbum.qpId;
    }

    public int getPlayOrder() {
        return this.mAlbum.order;
    }

    public int isCommitted() {
        return this.mIsCommitted;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("albumid=").append(this.mAlbum.qpId).append(", tvid=").append(this.mAlbum.tvQid).append(", order=").append(this.mAlbum.order).append(", time=").append(this.mAlbum.time).append(", name=").append(this.mAlbum.name).append(", playtime=").append(this.mAlbum.playTime).append(", uploadtime=").append(this.mUploadTime).append(", addedtime=").append(this.mAddTime).append(", duration=").append(this.mAlbum.len).append(", sourcecode=").append(this.mAlbum.sourceCode).append(", score=").append(this.mAlbum.score).append(", tvsets=").append(this.mAlbum.tvsets).append(", vid=").append(this.mAlbum.vid).append(", channelid=").append(this.mAlbum.chnId).append(", type=").append(this.mAlbum.type).append(",tvname=").append(this.mAlbum.tvName).append(", is3d=").append(this.mAlbum.is3D).append(", ispurchase=").append(this.mAlbum.isPurchase).append(", isseries=").append(this.mAlbum.isSeries).append(", pic=").append(this.mAlbum.pic).append(", tvpic=").append(this.mAlbum.tvPic).append(", exclusivee=").append(this.mAlbum.exclusive);
        if (this.mAlbum.vipInfo != null) {
            sb.append(", isvip=").append(this.mAlbum.vipInfo.isVip).append(", isCoupon=").append(this.mAlbum.vipInfo.isCoupon).append(", isTvod=").append(this.mAlbum.vipInfo.isTvod).append(", isPackage=").append(this.mAlbum.vipInfo.isPkg).append(", episvip=").append(this.mAlbum.vipInfo.epIsVip).append(", episcoupon=").append(this.mAlbum.vipInfo.epIsCoupon).append(", epistvod=").append(this.mAlbum.vipInfo.epIsTvod).append(", epispackage=").append(this.mAlbum.vipInfo.epIsPkg);
        }
        sb.append(", paymark=").append(this.mAlbum.payMarkType).append(", tvcount=").append(this.mAlbum.tvCount).append(",drm=").append(this.mAlbum.drm).append(",shortName=").append(this.mAlbum.shortName).append(",contentType=").append(this.mAlbum.contentType);
        sb.append(", cookie=").append(this.mCookie).append(", hdr=").append(this.mAlbum.dynamicRanges).append(")");
        return sb.toString();
    }
}
