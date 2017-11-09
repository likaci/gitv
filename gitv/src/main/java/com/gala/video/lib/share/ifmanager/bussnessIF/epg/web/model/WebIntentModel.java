package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.io.Serializable;
import java.util.ArrayList;

public class WebIntentModel implements Serializable {
    private static final long serialVersionUID = 1;
    private Album mAlbum;
    private String mAlbumJson;
    private String mAlbumListJson;
    public String mBuyFrom;
    private String mBuySource;
    public int mBuyVip = 0;
    public String mCouponActivityCode;
    public String mCouponSignKey;
    private int mCurrentPageType = -1;
    private int mEenterType;
    private String mEventId;
    private ArrayList<Album> mFlowerList;
    private String mFlowerListJson;
    private String mFrom;
    private String mId;
    public String mIncomesrc;
    private String mName;
    private int mPageType;
    private String mPageUrl;
    private int mPageUrlType;
    public String mResGroupId;
    private String mState;
    public String mTabSrc;
    private int mType;

    public static WebIntentModel build() {
        return new WebIntentModel();
    }

    public static WebIntentModel build(int mCurrentPageType) {
        return new WebIntentModel(mCurrentPageType);
    }

    public static WebIntentModel build(WebIntentParams params) {
        return new WebIntentModel(params);
    }

    public WebIntentModel(int mCurrentPageType) {
        this.mCurrentPageType = mCurrentPageType;
    }

    public WebIntentModel(WebIntentParams params) {
        if (params != null) {
            this.mId = params.id;
            this.mBuyFrom = params.buyFrom;
            this.mFrom = params.from;
            this.mName = params.name;
            this.mPageUrl = params.pageUrl;
            this.mPageType = params.pageType;
            this.mEenterType = params.enterType;
            this.mEventId = params.eventId;
            this.mState = params.state;
            this.mAlbum = params.albumInfo;
            this.mBuySource = params.buySource;
            this.mResGroupId = params.resGroupId;
            this.mBuyVip = params.buyVip;
            this.mIncomesrc = params.incomesrc;
            this.mTabSrc = params.tabSrc;
            this.mCouponActivityCode = params.couponActivityCode;
            this.mCouponSignKey = params.couponSignKey;
            LogUtils.i("WebIntentModel", "incomesrc:" + params.incomesrc + ",buyVip:" + params.buyVip + ",buyFrom:" + params.buyFrom + ",mIncomesrc:" + params.tabSrc + ",mCouponActivityCode:" + params.couponActivityCode + ",mCouponSignKey:" + params.couponSignKey);
        }
    }

    public int getCurrentPageType() {
        return this.mCurrentPageType;
    }

    public void setCurrentPageType(int mCurrentPageType) {
        this.mCurrentPageType = mCurrentPageType;
    }

    public String getState() {
        return this.mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public int getPageType() {
        return this.mPageType;
    }

    public void setPageType(int pageType) {
        this.mPageType = pageType;
    }

    public int getEnterType() {
        return this.mEenterType;
    }

    public void setEnterType(int enterType) {
        this.mEenterType = enterType;
    }

    public String getEventId() {
        return this.mEventId;
    }

    public void setEventId(String eventId) {
        this.mEventId = eventId;
    }

    public String getAlbumListJson() {
        return this.mAlbumListJson;
    }

    public String getPageUrl() {
        return this.mPageUrl;
    }

    public void setPageUrl(String mPageUrl) {
        this.mPageUrl = mPageUrl;
    }

    public int getPageUrlType() {
        return this.mPageUrlType;
    }

    public void setPageUrlType(int mPageUrlType) {
        this.mPageUrlType = mPageUrlType;
    }

    public void setAlbumListJson(String albumListJson) {
        this.mAlbumListJson = albumListJson;
    }

    public String getAlbumJson() {
        return this.mAlbumJson;
    }

    public void setAlbumJson(String albumJson) {
        this.mAlbumJson = albumJson;
    }

    public String getFlowerListJson() {
        return this.mFlowerListJson;
    }

    public void setFlowerListJson(String flowerListJson) {
        this.mFlowerListJson = flowerListJson;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }

    public ArrayList<Album> getFlowerList() {
        return this.mFlowerList;
    }

    public void setFlowerList(ArrayList<Album> flowerList) {
        this.mFlowerList = flowerList;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getFrom() {
        return this.mFrom;
    }

    public void setFrom(String from) {
        this.mFrom = from;
    }

    public String getBuySource() {
        return this.mBuySource;
    }

    public void setBuySource(String buySource) {
        this.mBuySource = buySource;
    }

    public String getBuyFrom() {
        return this.mBuyFrom;
    }

    public void setBuyFrom(String buyFrom) {
        this.mBuyFrom = buyFrom;
    }

    public String getResGroupId() {
        return this.mResGroupId;
    }

    public void setResGroupId(String resGroupId) {
        this.mResGroupId = resGroupId;
    }

    public int getBuyVip() {
        return this.mBuyVip;
    }

    public void setBuyVip(int mBuyVip) {
        this.mBuyVip = mBuyVip;
    }

    public String getIncomesrc() {
        return this.mIncomesrc;
    }

    public void setIncomesrc(String mIncomesrc) {
        this.mIncomesrc = mIncomesrc;
    }

    public String getTabSrc() {
        return this.mTabSrc;
    }

    public void setTabSrc(String tabSrc) {
        this.mTabSrc = tabSrc;
    }

    public String getCouponActivityCode() {
        return this.mCouponActivityCode;
    }

    public void setCouponActivityCode(String couponActivityCode) {
        this.mCouponActivityCode = couponActivityCode;
    }

    public String getCouponSignKey() {
        return this.mCouponSignKey;
    }

    public void setCouponSignKey(String couponSignKey) {
        this.mCouponSignKey = couponSignKey;
    }
}
