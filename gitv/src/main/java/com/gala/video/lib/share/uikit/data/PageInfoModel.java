package com.gala.video.lib.share.uikit.data;

import java.io.Serializable;
import java.util.List;

public class PageInfoModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String mBaseName;
    private String mBgUrl;
    private List<CardInfoModel> mCardInfoModels;
    private int mDefaultFocus;
    private long mExpTime;
    private String mId;
    private int mIsSkin;
    private short mIsVip;
    private String mLocation;
    private String mMatrix;
    private String mOrientation;
    private String mPageT;
    private String mVersion;

    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return this.mId;
    }

    public short getIsVip() {
        return this.mIsVip;
    }

    public void setIsVip(short isVip) {
        this.mIsVip = isVip;
    }

    public void setPageT(String pageT) {
        this.mPageT = pageT;
    }

    public String getPageT() {
        return this.mPageT;
    }

    public void setBaseName(String baseName) {
        this.mBaseName = baseName;
    }

    public String getBaseName() {
        return this.mBaseName;
    }

    public void setExpTime(long expTime) {
        this.mExpTime = expTime;
    }

    public long getExpTime() {
        return this.mExpTime;
    }

    public String getBgUrl() {
        return this.mBgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.mBgUrl = bgUrl;
    }

    public void setIsSkin(int isSkin) {
        this.mIsSkin = isSkin;
    }

    public int getIsSkin() {
        return this.mIsSkin;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setOrientation(String orientation) {
        this.mOrientation = orientation;
    }

    public String getOrientation() {
        return this.mOrientation;
    }

    public void setMatrix(String matrix) {
        this.mMatrix = matrix;
    }

    public String getMatrix() {
        return this.mMatrix;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public void setDefaultFocus(int defaultFocus) {
        this.mDefaultFocus = defaultFocus;
    }

    public int getDefaultFocus() {
        return this.mDefaultFocus;
    }

    public List<CardInfoModel> getCardInfoModels() {
        return this.mCardInfoModels;
    }

    public void setCardInfoModels(List<CardInfoModel> CardInfoModels) {
        this.mCardInfoModels = CardInfoModels;
    }
}
