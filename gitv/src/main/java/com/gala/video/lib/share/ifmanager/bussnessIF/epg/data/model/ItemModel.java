package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class ItemModel extends HomeModel {
    private static final long serialVersionUID = 1;
    private String dataIndex = "";
    private boolean isToBeOnline;
    private boolean isVipTab;
    private Channel mChannel;
    private ChannelLabel mChannelLabel;
    private int mChnId;
    private String mDes = "";
    private String mDesL1RBString = "";
    private int mFocusedIconId;
    public int mGifHigh;
    public String mGifPic;
    public int mGifWidth;
    private int mHigh;
    private String mIcon = "";
    private boolean mIsDisableNoLogin = false;
    public boolean mIsPlaying;
    private boolean mIsTitle = true;
    private boolean mIsVip;
    private ItemDataType mItemDataType;
    private String mItemPic = "";
    private String mLiveId = "";
    private int mNormalIconId;
    private String mOnlineTime = "";
    private String mPic = "";
    private String mPlId = "";
    private String mQpId = "";
    private int mRank;
    private String mRecommendAppKey;
    public TabModel mTabModel;
    public String mTabSrc = "";
    private int mTableNo;
    private String mTitle = "";
    private String mTvId = "";
    private String mTvPic = "";
    private String mTvTag = "";
    private String mUrl = "";
    private WidgetChangeStatus mWidgetChangeStatus = WidgetChangeStatus.NoChange;
    private int mWidgetType;
    private int mWidth;
    public String playListId = "";

    public enum ItemUIStyle {
        PORTRAIT("portrait"),
        LANDSCAPE("landscape"),
        NONE("none");
        
        private String value;

        private ItemUIStyle(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public String getRecommendAppKey() {
        return this.mRecommendAppKey;
    }

    public void setRecommendAppKey(String recommendAppKey) {
        this.mRecommendAppKey = recommendAppKey;
    }

    public int getNormalIconId() {
        return this.mNormalIconId;
    }

    public void setNormalIconId(int normalIconId) {
        this.mNormalIconId = normalIconId;
    }

    public int getFocusedIconId() {
        return this.mFocusedIconId;
    }

    public void setFocusedIconId(int focusedIconId) {
        this.mFocusedIconId = focusedIconId;
    }

    public void setDesL1RBString(String rightDes) {
        this.mDesL1RBString = rightDes;
    }

    public String getDesL1RBString() {
        return this.mDesL1RBString;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.mIsPlaying = isPlaying;
    }

    public int getWidgetType() {
        return this.mWidgetType;
    }

    public void setWidgetType(int widgetType) {
        this.mWidgetType = widgetType;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
    }

    public void setItemType(ItemDataType itemDataType) {
        this.mItemDataType = itemDataType;
    }

    public ChannelLabel getData() {
        return this.mChannelLabel;
    }

    public void setData(ChannelLabel data) {
        this.mChannelLabel = data;
    }

    public String getQpId() {
        return this.mQpId;
    }

    public void setQpId(String qpId) {
        this.mQpId = qpId;
    }

    public String getTvId() {
        return this.mTvId;
    }

    public void setTvId(String tvId) {
        this.mTvId = tvId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getItemPic() {
        return this.mItemPic;
    }

    public void setItemPic(String pic) {
        this.mItemPic = pic;
    }

    public String getDes() {
        return this.mDes;
    }

    public void setDes(String des) {
        this.mDes = des;
    }

    public String getOnlineTime() {
        return this.mOnlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.mOnlineTime = onlineTime;
    }

    public String getIcon() {
        return this.mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public int getChannelId() {
        return this.mChnId;
    }

    public void setChannelId(int chnId) {
        this.mChnId = chnId;
    }

    public String getTvTag() {
        return this.mTvTag;
    }

    public void setTvTag(String tvTag) {
        this.mTvTag = tvTag;
    }

    public String getPlId() {
        return this.mPlId;
    }

    public void setPlId(String plId) {
        this.mPlId = plId;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHigh() {
        return this.mHigh;
    }

    public void setHigh(int high) {
        this.mHigh = high;
    }

    public String getLiveId() {
        return this.mLiveId;
    }

    public void setLiveId(String liveId) {
        this.mLiveId = liveId;
    }

    public boolean isVip() {
        return this.mIsVip;
    }

    public void setIsVip(boolean isVip) {
        this.mIsVip = isVip;
    }

    public String getTvPic() {
        return this.mTvPic;
    }

    public void setTvPic(String tvPic) {
        this.mTvPic = tvPic;
    }

    public String getPic() {
        return this.mPic;
    }

    public void setPic(String pic) {
        this.mPic = pic;
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mWidgetChangeStatus;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus widgetChangeStatus) {
        this.mWidgetChangeStatus = widgetChangeStatus;
    }

    public int getTableNo() {
        return this.mTableNo;
    }

    public void setTableNo(int tableNo) {
        this.mTableNo = tableNo;
    }

    public int getRank() {
        return this.mRank;
    }

    public void setRank(int mRank) {
        this.mRank = mRank;
    }

    public boolean isTitle() {
        return this.mIsTitle;
    }

    public void setIsTitle(boolean mIsTitle) {
        this.mIsTitle = mIsTitle;
    }

    public boolean isVipTab() {
        return this.isVipTab;
    }

    public void setIsVipTab(boolean isVipTab) {
        this.isVipTab = isVipTab;
    }

    public boolean isToBeOnline() {
        return this.isToBeOnline;
    }

    public void setToBeOnline(boolean isToBeOnline) {
        this.isToBeOnline = isToBeOnline;
    }

    public Channel getChannel() {
        return this.mChannel;
    }

    public void setChannel(Channel mChannel) {
        this.mChannel = mChannel;
    }

    public String getDataIndex() {
        return this.dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public void setDisableNoLogin(boolean isDisable) {
        this.mIsDisableNoLogin = isDisable;
    }

    public boolean isDisableNoLogin() {
        return this.mIsDisableNoLogin;
    }

    public void setTabSrc(String tabSrc) {
        this.mTabSrc = tabSrc;
    }

    public String getTabSrc() {
        return this.mTabSrc;
    }

    public String toString() {
        return "ItemModel [mWidgetType=" + this.mWidgetType + ", mItemDataType=" + this.mItemDataType + ", mChannelLabel=" + this.mChannelLabel + ", isVipTab=" + this.isVipTab + ", mQpId=" + this.mQpId + ", mTvId=" + this.mTvId + ", mTitle=" + this.mTitle + ", mItemPic=" + this.mItemPic + ", mTvPic=" + this.mTvPic + ", mPic=" + this.mPic + ", mDes=" + this.mDes + ", mOnlineTime=" + this.mOnlineTime + ", mIcon=" + this.mIcon + ", mTvTag=" + this.mTvTag + ", mPlId=" + this.mPlId + ", mUrl=" + this.mUrl + ", mWidth=" + this.mWidth + ", mHigh=" + this.mHigh + ", mLiveId=" + this.mLiveId + ", mChnId=" + this.mChnId + ", mTableNo=" + this.mTableNo + ", mIsVip=" + this.mIsVip + ", mRank=" + this.mRank + ", mIsTitle=" + this.mIsTitle + ", isToBeOnline=" + this.isToBeOnline + ", mWidgetChangeStatus=" + this.mWidgetChangeStatus + ", mChannel=" + this.mChannel + ", mTabSrc=" + this.mTabSrc + ", mTabModel=" + this.mTabModel + ", dataIndex=" + this.dataIndex + ", mIsPlaying=" + this.mIsPlaying + AlbumEnterFactory.SIGN_STR;
    }
}
