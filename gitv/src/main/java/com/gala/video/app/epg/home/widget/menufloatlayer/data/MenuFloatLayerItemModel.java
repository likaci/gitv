package com.gala.video.app.epg.home.widget.menufloatlayer.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class MenuFloatLayerItemModel {
    private int mFocusIconResId;
    private int mIconResId;
    private boolean mIsOnlineData = false;
    private ItemDataType mItemDataType = null;
    private String mTitle = "";

    public MenuFloatLayerItemModel(String mTitle, ItemDataType mItemDataType, int mIconResId, boolean isOnlineData) {
        this.mTitle = mTitle;
        this.mItemDataType = mItemDataType;
        this.mIconResId = mIconResId;
        this.mIsOnlineData = isOnlineData;
    }

    public MenuFloatLayerItemModel(String mTitle, ItemDataType mItemDataType, int mIconResId, int mFocusIconResId, boolean mIsOnlineData) {
        this.mTitle = mTitle;
        this.mIconResId = mIconResId;
        this.mFocusIconResId = mFocusIconResId;
        this.mItemDataType = mItemDataType;
        this.mIsOnlineData = mIsOnlineData;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public void setIconResId(int mIconResId) {
        this.mIconResId = mIconResId;
    }

    public int getFocusIconResId() {
        return this.mFocusIconResId;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
    }

    public void setItemType(ItemDataType mItemDataType) {
        this.mItemDataType = mItemDataType;
    }

    public boolean isOnlineData() {
        return this.mIsOnlineData;
    }

    public void setOnlineData(boolean onlineData) {
        this.mIsOnlineData = onlineData;
    }

    public String toString() {
        return "MenuFloatLayerItemModel{mTitle='" + this.mTitle + '\'' + ", mIconResId=" + this.mIconResId + ", mItemDataType=" + this.mItemDataType + ", mIsOnlineData=" + this.mIsOnlineData + '}';
    }
}
