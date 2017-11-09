package com.gala.video.lib.share.uikit.action.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.loader.data.BannerAd;
import java.io.Serializable;

public class CommonAdData implements Serializable {
    private int adId;
    private String albumId;
    private String carouselId;
    private String carouselName;
    private String carouselNo;
    private String clickThroughInfo;
    private AdClickType focusImageAdType;
    private ItemDataType itemDataType;
    private String mTitle;
    private String plId;
    private String tvId;

    public static CommonAdData convertAdModel(HomeFocusImageAdModel model) {
        CommonAdData adItemData = new CommonAdData();
        adItemData.setAdId(model.getAdId());
        adItemData.setClickThroughInfo(model.getClickThroughInfo());
        adItemData.setPlId(model.getPlId());
        adItemData.setAlbumId(model.getAlbumId());
        adItemData.setTvId(model.getTvId());
        adItemData.setCarouselId(model.getCarouselId());
        adItemData.setCarouselNo(model.getCarouselNo());
        adItemData.setCarouselName(model.getCarouselName());
        adItemData.setFocusImageAdType(model.getAdClickType());
        adItemData.setItemDataType(model.getItemType());
        adItemData.setTitle(model.getTitle());
        return adItemData;
    }

    public static CommonAdData convertBannerAdModel(BannerAd model) {
        CommonAdData adItemData = new CommonAdData();
        adItemData.setAdId(model.adId);
        adItemData.setClickThroughInfo(model.clickThroughInfo);
        adItemData.setPlId(model.plId);
        adItemData.setAlbumId(model.albumId);
        adItemData.setTvId(model.tvId);
        adItemData.setCarouselId(model.carouselId);
        adItemData.setCarouselNo(model.carouselNo);
        adItemData.setCarouselName(model.carouselName);
        adItemData.setFocusImageAdType(model.adClickType);
        adItemData.setItemDataType(ItemDataType.BANNER_IMAGE_AD);
        adItemData.setTitle(model.title);
        return adItemData;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setFocusImageAdType(AdClickType focusImageAdType) {
        this.focusImageAdType = focusImageAdType;
    }

    public void setClickThroughInfo(String clickThroughInfo) {
        this.clickThroughInfo = clickThroughInfo;
    }

    public void setPlId(String plId) {
        this.plId = plId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public void setCarouselId(String carouselId) {
        this.carouselId = carouselId;
    }

    public void setCarouselNo(String carouselNo) {
        this.carouselNo = carouselNo;
    }

    public void setCarouselName(String carouselName) {
        this.carouselName = carouselName;
    }

    public void setItemDataType(ItemDataType itemDataType) {
        this.itemDataType = itemDataType;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getAdId() {
        return this.adId;
    }

    public AdClickType getAdClickType() {
        return this.focusImageAdType;
    }

    public String getClickThroughInfo() {
        return this.clickThroughInfo;
    }

    public String getPlId() {
        return this.plId;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public String getTvId() {
        return this.tvId;
    }

    public String getCarouselId() {
        return this.carouselId;
    }

    public String getCarouselNo() {
        return this.carouselNo;
    }

    public String getCarouselName() {
        return this.carouselName;
    }

    public ItemDataType getItemDataType() {
        return this.itemDataType;
    }

    public String getTitle() {
        return this.mTitle;
    }
}
