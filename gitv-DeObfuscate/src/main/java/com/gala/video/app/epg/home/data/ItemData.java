package com.gala.video.app.epg.home.data;

import android.graphics.drawable.Drawable;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import java.util.List;

public class ItemData extends DataSource {
    private int appId = 0;
    private String appPackageName;
    public String carouselChannelId = "";
    public boolean circleHasTitle = true;
    private String dataIndex = "";
    public String desL1RBString;
    public boolean is3D = false;
    public boolean isCarouselChannel = false;
    public boolean isCharge = false;
    public boolean isCoupons = false;
    public boolean isDolby = false;
    public boolean isDuBo = false;
    public boolean isDuJia = false;
    public boolean isPlaying = false;
    public boolean isRanked = false;
    public boolean isShowRBDes1;
    public boolean isShowScore;
    public boolean isSubject = false;
    public boolean isToBeOnline = false;
    public boolean isVip = false;
    public boolean isVipTab = false;
    private int mAppItemType;
    private Channel mChannel;
    private int mChnId;
    private String mDownloadUrl;
    private int mFocusedIconId;
    public String mGif;
    public int mGifHigh;
    public int mGifWidth;
    private int mHigh;
    private Album mHistoryInfoAlbum;
    private Drawable mIconDrawable;
    private int mIconRes;
    private String mIconUrl;
    private ItemDataType mItemDataType = ItemDataType.NONE;
    public ChannelLabel mLabel;
    private String mLiveId;
    private List<DailyLabelModel> mNewParams;
    private int mNewParamsPos = 0;
    private String mOnlineTime;
    public String mPostImageUrl;
    private String mRecommendAppKey;
    public WidgetChangeStatus mStatus;
    public String mTVImageUrl;
    private int mTabNo;
    private int mWidth;
    public String pageUrl = "";
    public String pingbackTabSrc = "";
    public String plId;
    public int rankedNum;
    public String score;

    public Album getHistoryInfoAlbum() {
        return this.mHistoryInfoAlbum;
    }

    public void setHistoryInfoAlbum(Album historyInfoAlbum) {
        this.mHistoryInfoAlbum = historyInfoAlbum;
    }

    public int getTabNo() {
        return this.mTabNo;
    }

    public void setTabNo(int mTabNo) {
        this.mTabNo = mTabNo;
    }

    public String getLiveId() {
        return this.mLiveId;
    }

    public void setLiveId(String mLiveId) {
        this.mLiveId = mLiveId;
    }

    public void setIconResId(int icon) {
        this.mIconRes = icon;
    }

    public int getIconResId() {
        return this.mIconRes;
    }

    public void setIconFocusedResId(int focusedIconId) {
        this.mFocusedIconId = focusedIconId;
    }

    public int getIconFocusedResId() {
        return this.mFocusedIconId;
    }

    public void setIconDrawable(Drawable drawable) {
        this.mIconDrawable = drawable;
    }

    public Drawable getIconDrawable() {
        return this.mIconDrawable;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getHeight() {
        return this.mHigh;
    }

    public void setHigh(int mHigh) {
        this.mHigh = mHigh;
    }

    public String toString() {
        return "ItemData [mTitle=" + this.mTitle + ", mChannelLabel=" + this.mLabel + ", mItemDataType=" + this.mItemDataType + ", mImage=" + this.mImage + ", isCarouselChannel=" + this.isCarouselChannel + ", carouselChannelId=" + this.carouselChannelId + ", circleHasTitle=" + this.circleHasTitle + ", isVipTab=" + this.isVipTab + ", mStatus=" + this.mStatus + ", mWidth=" + this.mWidth + ", mHigh=" + this.mHigh + ", mChnId=" + this.mChnId + ", isVip=" + this.isVip + ", isCharge=" + this.isCharge + ", isCoupons=" + this.isCoupons + ", isDuJia=" + this.isDuJia + ", isDuBo=" + this.isDuBo + ", isSubject=" + this.isSubject + ", is3D=" + this.is3D + ", isDolby=" + this.isDolby + ", isRanked=" + this.isRanked + ", isPlaying=" + this.isPlaying + ", isToBeOnline=" + this.isToBeOnline + ", rankedNum=" + this.rankedNum + ", isShowScore=" + this.isShowScore + ", score=" + this.score + ", isShowRBDes1=" + this.isShowRBDes1 + ", desL1RBString=" + this.desL1RBString + ", mPostImageUrl=" + this.mPostImageUrl + ", mTVImageUrl=" + this.mTVImageUrl + ", pingbackTabSrc=" + this.pingbackTabSrc + ", pageUrl=" + this.pageUrl + ", mNewParams=" + this.mNewParams + ", mNewParamsPos=" + this.mNewParamsPos + ", mIconUrl=" + this.mIconUrl + ", mIconRes=" + this.mIconRes + ", mIconDrawable=" + this.mIconDrawable + ", appId=" + this.appId + ", appPackageName=" + this.appPackageName + ", mAppItemType=" + this.mAppItemType + ", mOnlineTime=" + this.mOnlineTime + ", mTabNo=" + this.mTabNo + ", mLiveId=" + this.mLiveId + ", mChannel=" + this.mChannel + ", mDownloadUrl=" + this.mDownloadUrl + ", mHistoryInfoAlbum=" + this.mHistoryInfoAlbum;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
    }

    public void setItemType(ItemDataType mItemDataType) {
        this.mItemDataType = mItemDataType;
    }

    public int getChnId() {
        return this.mChnId;
    }

    public void setChnId(int chnId) {
        this.mChnId = chnId;
    }

    public List<DailyLabelModel> getNewParams() {
        return this.mNewParams;
    }

    public void setNewParams(List<DailyLabelModel> mNewParams) {
        this.mNewParams = mNewParams;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public void setIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public int getNewParamsPos() {
        return this.mNewParamsPos;
    }

    public void setNewParamsPos(int mNewParamsPos) {
        this.mNewParamsPos = mNewParamsPos;
    }

    public int getAppId() {
        return this.appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppPackageName() {
        return this.appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public void setAppItemType(int type) {
        this.mAppItemType = type;
    }

    public int getAppItemType() {
        return this.mAppItemType;
    }

    public String getOnlineTime() {
        return this.mOnlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.mOnlineTime = onlineTime;
    }

    public Channel getChannel() {
        return this.mChannel;
    }

    public void setChannel(Channel mChannel) {
        this.mChannel = mChannel;
    }

    public void setAppDownLoadUrl(String url) {
        this.mDownloadUrl = url;
    }

    public String getAppDownLoadUrl() {
        return this.mDownloadUrl;
    }

    public String getDataIndex() {
        return this.dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public String getRecommendAppKey() {
        return this.mRecommendAppKey;
    }

    public void setRecommendAppKey(String recommendAppKey) {
        this.mRecommendAppKey = recommendAppKey;
    }
}
