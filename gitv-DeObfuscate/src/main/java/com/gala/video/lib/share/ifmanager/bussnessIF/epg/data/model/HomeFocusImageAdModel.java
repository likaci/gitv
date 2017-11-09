package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.mcto.ads.constants.ClickThroughType;

public class HomeFocusImageAdModel extends HomeModel {
    private static final String TAG = "HomeFocusImageAdModel";
    private static final long serialVersionUID = 1;
    private AdClickType adClickType = AdClickType.NONE;
    private int adId;
    private int adIndex;
    private String albumId = "";
    private String carouselId = "";
    private String carouselName = "";
    private String carouselNo = "";
    private String clickThroughInfo = "";
    private ClickThroughType clickThroughType = null;
    private int defHeight = AdsConstants.HOME_FOCUS_IMAGE_AD_HEIGHT_DEFAULT;
    private int defWidth = AdsConstants.HOME_FOCUS_IMAGE_AD_WIDTH_DEFAULT;
    private int height;
    private String imageUrl = "";
    private ItemDataType mItemDataType = ItemDataType.NONE;
    private WidgetChangeStatus mWidgetChangeStatus = WidgetChangeStatus.NoChange;
    private int mWidgetType = 0;
    private String needAdBadge = "";
    private String plId = "";
    private String title = "";
    private String tvId = "";
    private int width;

    public int getAdId() {
        return this.adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDefWidth() {
        return this.defWidth;
    }

    public int getDefHeight() {
        return this.defHeight;
    }

    public String getNeedAdBadge() {
        return this.needAdBadge;
    }

    public void setNeedAdBadge(String needAdBadge) {
        this.needAdBadge = needAdBadge;
    }

    public ClickThroughType getClickThroughType() {
        return this.clickThroughType;
    }

    public void setClickThroughType(ClickThroughType clickThroughType) {
        this.clickThroughType = clickThroughType;
    }

    public AdClickType getAdClickType() {
        return this.adClickType;
    }

    public void setAdClickType(AdClickType adClickType) {
        this.adClickType = adClickType;
    }

    public String getClickThroughInfo() {
        return this.clickThroughInfo;
    }

    public void setClickThroughInfo(String clickThroughInfo) {
        this.clickThroughInfo = clickThroughInfo;
    }

    public String getPlId() {
        return this.plId;
    }

    public void setPlId(String plId) {
        this.plId = plId;
    }

    public String getTvId() {
        return this.tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getCarouselId() {
        return this.carouselId;
    }

    public void setCarouselId(String carouselId) {
        this.carouselId = carouselId;
    }

    public String getCarouselNo() {
        return this.carouselNo;
    }

    public void setCarouselNo(String carouselNo) {
        this.carouselNo = carouselNo;
    }

    public String getCarouselName() {
        return this.carouselName;
    }

    public void setCarouselName(String carouselName) {
        this.carouselName = carouselName;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
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

    public int getWidgetType() {
        return this.mWidgetType;
    }

    public void setWidgetType(int widgetType) {
        this.mWidgetType = widgetType;
    }

    public int getAdIndex() {
        return this.adIndex;
    }

    public void setAdIndex(int adIndex) {
        this.adIndex = adIndex;
    }

    public String toString() {
        return "HomeFocusImageAdModel{adId=" + this.adId + ", title='" + this.title + '\'' + ", imageUrl='" + this.imageUrl + '\'' + ", width=" + this.width + ", height=" + this.height + ", defWidth=" + this.defWidth + ", defHeight=" + this.defHeight + ", needAdBadge='" + this.needAdBadge + '\'' + ", clickThroughType=" + this.clickThroughType + ", focusImageAdType=" + this.adClickType + ", clickThroughInfo='" + this.clickThroughInfo + '\'' + ", albumId=" + this.albumId + ", tvId=" + this.tvId + ", plId=" + this.plId + ", mWidgetChangeStatus=" + this.mWidgetChangeStatus + ", mItemDataType=" + this.mItemDataType + ", mWidgetType=" + this.mWidgetType + '}';
    }
}
