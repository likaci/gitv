package com.gala.video.app.epg.home.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.mcto.ads.constants.ClickThroughType;

public abstract class AdItemData extends ItemData {
    private static final String TAG = "data/AdItemData";
    protected AdClickType mAdClickType = AdClickType.NONE;
    protected int mAdId;
    protected String mAlbumId = "";
    protected String mCarouselId = "";
    protected String mCarouselName = "";
    protected String mCarouselNo = "";
    protected String mClickThroughInfo = "";
    protected ClickThroughType mClickThroughType = null;
    protected int mDefHeight;
    protected int mDefWidth;
    protected ItemDataType mItemDataType = ItemDataType.NONE;
    protected boolean mNeedAdBadge = true;
    protected String mPlId = "";
    protected String mTvId = "";
    protected WidgetChangeStatus mWidgetChangeStatus;

    public int getAdId() {
        return this.mAdId;
    }

    public void setAdId(int mAdId) {
        this.mAdId = mAdId;
    }

    public int getDefWidth() {
        return this.mDefWidth;
    }

    public void setDefWidth(int mDefWidth) {
        this.mDefWidth = mDefWidth;
    }

    public int getmDefHeight() {
        return this.mDefHeight;
    }

    public void setDefHeight(int mDefHeight) {
        this.mDefHeight = mDefHeight;
    }

    public boolean getNeedAdBadge() {
        return this.mNeedAdBadge;
    }

    public void setNeedAdBadge(boolean mNeedAdBadge) {
        this.mNeedAdBadge = mNeedAdBadge;
    }

    public ClickThroughType getClickThroughType() {
        return this.mClickThroughType;
    }

    public void setClickThroughType(ClickThroughType mClickThroughType) {
        this.mClickThroughType = mClickThroughType;
    }

    public AdClickType getAdClickType() {
        return this.mAdClickType;
    }

    public void setFocusImageAdType(AdClickType mAdClickType) {
        this.mAdClickType = mAdClickType;
    }

    public String getClickThroughInfo() {
        return this.mClickThroughInfo;
    }

    public void setClickThroughInfo(String mClickThroughInfo) {
        this.mClickThroughInfo = mClickThroughInfo;
    }

    public String getAlbumId() {
        return this.mAlbumId;
    }

    public void setAlbumId(String mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public String getTvId() {
        return this.mTvId;
    }

    public void setTvId(String mTvId) {
        this.mTvId = mTvId;
    }

    public String getPlId() {
        return this.mPlId;
    }

    public void setPlId(String mPlId) {
        this.mPlId = mPlId;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
    }

    public String getCarouselId() {
        return this.mCarouselId;
    }

    public void setCarouselId(String mCarouselId) {
        this.mCarouselId = mCarouselId;
    }

    public String getCarouselNo() {
        return this.mCarouselNo;
    }

    public void setCarouselNo(String mCarouselNo) {
        this.mCarouselNo = mCarouselNo;
    }

    public String getCarouselName() {
        return this.mCarouselName;
    }

    public void setCarouselName(String mCarouselName) {
        this.mCarouselName = mCarouselName;
    }

    public void setItemType(ItemDataType itemDataType) {
        this.mItemDataType = itemDataType;
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mWidgetChangeStatus;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus mWidgetChangeStatus) {
        this.mWidgetChangeStatus = mWidgetChangeStatus;
    }

    public String toString() {
        return "AdItemData{mAdId=" + this.mAdId + ", mDefWidth=" + this.mDefWidth + ", mDefHeight=" + this.mDefHeight + ", mNeedAdBadge='" + this.mNeedAdBadge + '\'' + ", mClickThroughType=" + this.mClickThroughType + ", mAdClickType=" + this.mAdClickType + ", mClickThroughInfo='" + this.mClickThroughInfo + '\'' + ", mAlbumId='" + this.mAlbumId + '\'' + ", mTvId='" + this.mTvId + '\'' + ", mPlId='" + this.mPlId + '\'' + ", mCarouselId='" + this.mCarouselId + '\'' + ", mCarouselNo='" + this.mCarouselNo + '\'' + ", mCarouselName='" + this.mCarouselName + '\'' + ", mWidgetChangeStatus=" + this.mWidgetChangeStatus + ", mItemDataType=" + this.mItemDataType + '}';
    }
}
