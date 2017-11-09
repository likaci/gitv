package com.gala.video.lib.share.uikit.data;

import com.gala.tvapi.vrs.model.ChannelLabel;
import java.io.Serializable;

public class CardInfoModel implements Serializable {
    public boolean canShort = true;
    public int cardLayoutId;
    public int cardLayoutId_entryAll;
    public Object detailCreateInfo;
    public boolean isCacheData = false;
    public boolean isVIPTag;
    private short mBodyHeight;
    private short mBodyMarginBottom;
    private short mBodyMarginLeft;
    private short mBodyMarginRight;
    private short mBodyMarginTop;
    private short mBodyPaddingBottom;
    private short mBodyPaddingLeft;
    private short mBodyPaddingRight;
    private short mBodyPaddingTop;
    public String mCardId;
    private short mCardType;
    private short mDefaultFocus;
    private short mHeaderHeight;
    private short mHeaderPaddingBottom;
    private short mHeaderPaddingLeft;
    private short mHeaderPaddingRight;
    private short mHeaderPaddingTop;
    private String mId;
    private ItemInfoModel[][] mItemInfoModels;
    public int mPageNo;
    private float mScale;
    private short mShowPosition;
    public String mSource;
    private short mSpaceH;
    private short mSpaceV;
    private String mTitle;
    private String mTitleTips;
    public int mUikitEngineId;
    private short mWidth;
    public ChannelLabel tvTagAll;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public short getCardType() {
        return this.mCardType;
    }

    public void setCardType(short cardType) {
        this.mCardType = cardType;
    }

    public short getWidth() {
        return this.mWidth;
    }

    public void setWidth(short width) {
        this.mWidth = width;
    }

    public short getBodyHeight() {
        return this.mBodyHeight;
    }

    public void setBodyHeight(short bodyHeight) {
        this.mBodyHeight = bodyHeight;
    }

    public short getBodyPaddingLeft() {
        return this.mBodyPaddingLeft;
    }

    public void setBodyPaddingLeft(short bodyPaddingLeft) {
        this.mBodyPaddingLeft = bodyPaddingLeft;
    }

    public short getBodyPaddingTop() {
        return this.mBodyPaddingTop;
    }

    public void setBodyPaddingTop(short bodyPaddingTop) {
        this.mBodyPaddingTop = bodyPaddingTop;
    }

    public short getBodyPaddingRight() {
        return this.mBodyPaddingRight;
    }

    public void setBodyPaddingRight(short bodyPaddingRight) {
        this.mBodyPaddingRight = bodyPaddingRight;
    }

    public short getBodyPaddingBottom() {
        return this.mBodyPaddingBottom;
    }

    public void setBodyPaddingBottom(short bodyPaddingBottom) {
        this.mBodyPaddingBottom = bodyPaddingBottom;
    }

    public short getBodyMarginLeft() {
        return this.mBodyMarginLeft;
    }

    public void setBodyMarginLeft(short bodyMarginLeft) {
        this.mBodyMarginLeft = bodyMarginLeft;
    }

    public short getBodyMarginTop() {
        return this.mBodyMarginTop;
    }

    public void setBodyMarginTop(short bodyMarginTop) {
        this.mBodyMarginTop = bodyMarginTop;
    }

    public short getBodyMarginRight() {
        return this.mBodyMarginRight;
    }

    public void setBodyMarginRight(short bodyMarginRight) {
        this.mBodyMarginRight = bodyMarginRight;
    }

    public short getBodyMarginBottom() {
        return this.mBodyMarginBottom;
    }

    public void setBodyMarginBottom(short bodyMarginBottom) {
        this.mBodyMarginBottom = bodyMarginBottom;
    }

    public short getHeaderHeight() {
        return this.mHeaderHeight;
    }

    public void setHeaderHeight(short headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    public short getHeaderPaddingLeft() {
        return this.mHeaderPaddingLeft;
    }

    public void setHeaderPaddingLeft(short headerPaddingLeft) {
        this.mHeaderPaddingLeft = headerPaddingLeft;
    }

    public short getHeaderPaddingTop() {
        return this.mHeaderPaddingTop;
    }

    public void setHeaderPaddingTop(short headerPaddingTop) {
        this.mHeaderPaddingTop = headerPaddingTop;
    }

    public short getHeaderPaddingRight() {
        return this.mHeaderPaddingRight;
    }

    public void setHeaderPaddingRight(short headerPaddingRight) {
        this.mHeaderPaddingRight = headerPaddingRight;
    }

    public short getHeaderPaddingBottom() {
        return this.mHeaderPaddingBottom;
    }

    public void setHeaderPaddingBottom(short headerPaddingBottom) {
        this.mHeaderPaddingBottom = headerPaddingBottom;
    }

    public short getSpaceV() {
        return this.mSpaceV;
    }

    public void setSpaceV(short spaceV) {
        this.mSpaceV = spaceV;
    }

    public short getSpaceH() {
        return this.mSpaceH;
    }

    public void setSpaceH(short spaceH) {
        this.mSpaceH = spaceH;
    }

    public int getShowPosition() {
        return this.mShowPosition;
    }

    public void setShowPosition(short showPosition) {
        this.mShowPosition = showPosition;
    }

    public short getDefaultFocus() {
        return this.mDefaultFocus;
    }

    public void setDefaultFocus(short defaultFocus) {
        this.mDefaultFocus = defaultFocus;
    }

    public float getScale() {
        return this.mScale;
    }

    public void setScale(float scale) {
        this.mScale = scale;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitleTips() {
        return this.mTitleTips;
    }

    public void setTitleTips(String tips) {
        this.mTitleTips = tips;
    }

    public ItemInfoModel[][] getItemInfoModels() {
        return this.mItemInfoModels;
    }

    public void setItemInfoModels(ItemInfoModel[][] mItemInfoModels) {
        this.mItemInfoModels = mItemInfoModels;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("}}}}}}}}}}}}CardInfoModel@");
        sb.append(hashCode()).append(", mPageNo=").append(this.mPageNo).append(", mUikitEngineId=").append(this.mUikitEngineId).append(", cardLayoutId=").append(this.cardLayoutId).append(", isVIPTag=").append(this.isVIPTag).append(", mCardId=").append(this.mCardId).append(", mSource=").append(this.mSource).append(", mTitle=").append(this.mTitle).append(", mId=").append(this.mId).append(", mScale=").append(this.mScale).append("}");
        return sb.toString();
    }
}
