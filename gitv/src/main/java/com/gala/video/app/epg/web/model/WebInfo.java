package com.gala.video.app.epg.web.model;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentModel;
import java.io.Serializable;
import java.util.ArrayList;

public class WebInfo implements Serializable {
    private static final String TAG = "EPG/web/WebInfo";
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
    private int mPageUrlType = 99;
    public String mResGroupId;
    private String mState;
    public String mTabSrc;
    private int mType;

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

    public WebInfo(WebIntentModel model) {
        if (model == null) {
            LogUtils.e(TAG, "WebInfo intentModel is null !!");
            return;
        }
        this.mBuyFrom = model.getBuyFrom();
        this.mBuyVip = model.getBuyVip();
        this.mType = model.getType();
        this.mId = model.getId();
        this.mName = model.getName();
        this.mFrom = model.getFrom();
        this.mAlbum = model.getAlbum();
        this.mFlowerList = model.getFlowerList();
        this.mBuySource = model.getBuySource();
        this.mAlbumJson = model.getAlbumJson();
        this.mAlbumListJson = model.getAlbumListJson();
        this.mFlowerListJson = model.getFlowerListJson();
        this.mPageUrl = model.getPageUrl();
        this.mPageUrlType = model.getPageUrlType();
        this.mPageType = model.getPageType();
        this.mEenterType = model.getEnterType();
        this.mEventId = model.getEventId();
        this.mState = model.getState();
        this.mResGroupId = model.getResGroupId();
        this.mIncomesrc = model.getIncomesrc();
        this.mTabSrc = model.getTabSrc();
        this.mCurrentPageType = model.getCurrentPageType();
        this.mCouponActivityCode = model.getCouponActivityCode();
        this.mCouponSignKey = model.getCouponSignKey();
    }

    public int getCurrentPageType() {
        return this.mCurrentPageType;
    }

    public void setCurrentPageType(int mCurrentPageType) {
        this.mCurrentPageType = mCurrentPageType;
    }

    public String getAlbumListJson() {
        return this.mAlbumListJson;
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

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getBuyFrom() {
        return this.mBuyFrom;
    }

    public void setBuyFrom(String mSource) {
        this.mBuyFrom = mSource;
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

    public String toString() {
        return new StringBuilder(TAG).append(":[mType=").append(this.mType).append(",mName=").append(this.mName).append(",mFrom=").append(this.mFrom).append(",mId=").append(this.mId).append(",mPageUrl=").append(this.mPageUrl).append(",mPageUrlType=").append(this.mPageUrlType).append(",mPageType=").append(this.mPageType).append(",mEenterType=").append(this.mEenterType).append(",mEventId=").append(this.mEventId).append(",mState=").append(this.mState).append(",mCurrentPageType=").append(this.mCurrentPageType).append(",mBuyFrom=").append(this.mBuyFrom).append(",mResGroupId=").append(this.mResGroupId).append(",mBuyVip=").append(this.mBuyVip).append(",mIncomesrc=").append(this.mIncomesrc).append(",mTabSrc=").append(this.mTabSrc).append(",mCouponSignKey=").append(this.mCouponSignKey).append(",mCouponActivityCode=").append(this.mCouponActivityCode).append(AlbumEnterFactory.SIGN_STR).toString();
    }
}
