package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

public class TabModel extends HomeModel {
    private static final long serialVersionUID = 1;
    private int id;
    private int mChannelId = -1;
    private boolean mIsAlternative = false;
    private boolean mIsFocusTab = false;
    private boolean mIsNew = false;
    private boolean mIsPlaceChanged = false;
    private boolean mIsSupportSort = false;
    private boolean mIsVipTab = false;
    private String mResourceGroupId;
    private int mResult = 0;
    private boolean mShown;
    private WidgetChangeStatus mStatus = WidgetChangeStatus.NoChange;
    private String mTitle;

    public boolean isNew() {
        return this.mIsNew;
    }

    public void setIsNew(boolean isAdded) {
        this.mIsNew = isAdded;
    }

    public void setIsVipTab(int isVipTab) {
        boolean z = true;
        if (isVipTab != 1) {
            z = false;
        }
        this.mIsVipTab = z;
    }

    public boolean isVipTab() {
        return this.mIsVipTab;
    }

    public void setIsFocusTab(boolean isFocusTab) {
        this.mIsFocusTab = isFocusTab;
    }

    public boolean isFocusTab() {
        return this.mIsFocusTab;
    }

    public void setIsPlaceChanged(boolean isPlaceChanged) {
        this.mIsPlaceChanged = isPlaceChanged;
    }

    public boolean isPlaceChanged() {
        return this.mIsPlaceChanged;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setChannelId(int id) {
        this.mChannelId = id;
    }

    public int getChannelId() {
        return this.mChannelId;
    }

    public boolean isChannelTab() {
        return this.mChannelId != -1;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.mResourceGroupId = resourceGroupId;
    }

    public String getResourceGroupId() {
        return this.mResourceGroupId;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus status) {
        this.mStatus = status;
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mStatus;
    }

    public void setRequestResult(int result) {
        this.mResult = result;
    }

    public int getRequestResult() {
        return this.mResult;
    }

    public void setIsSupportSort(boolean isSupportSort) {
        this.mIsSupportSort = isSupportSort;
    }

    public boolean isSupportSort() {
        return this.mIsSupportSort;
    }

    public boolean isShown() {
        return this.mShown;
    }

    public void setShown(boolean mShown) {
        this.mShown = mShown;
    }

    public void setIsAlternative(boolean isAlternative) {
        this.mIsAlternative = isAlternative;
    }

    public boolean isAlternative() {
        return this.mIsAlternative;
    }

    public String toString() {
        return "tittle = " + this.mTitle + ",tab status = " + this.mStatus + ",isFocusTab = " + this.mIsFocusTab + ",isVip tab = " + this.mIsVipTab + ",group id = " + this.mResourceGroupId + ",channel id = " + this.mChannelId + ",is place changed = " + this.mIsPlaceChanged + ",isNew = " + this.mIsNew;
    }
}
