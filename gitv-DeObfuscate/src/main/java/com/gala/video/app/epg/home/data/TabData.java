package com.gala.video.app.epg.home.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;

public class TabData extends DataSource {
    private int mChannelId;
    private boolean mIsChannelTab;
    private boolean mIsFocusTab;
    private boolean mIsNew;
    private boolean mIsVipTab;
    private String mResourceId;
    private WidgetChangeStatus mWidgetChangeStatus;

    public boolean isIsFocusTab() {
        return this.mIsFocusTab;
    }

    public void setIsFocusTab(boolean mIsFocusTab) {
        this.mIsFocusTab = mIsFocusTab;
    }

    public boolean isIsVipTab() {
        return this.mIsVipTab;
    }

    public void setIsVipTab(boolean mIsVipTab) {
        this.mIsVipTab = mIsVipTab;
    }

    public boolean isIsChannelTab() {
        return this.mIsChannelTab;
    }

    public void setIsChannelTab(boolean mIsChannelTab) {
        this.mIsChannelTab = mIsChannelTab;
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mWidgetChangeStatus;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus widgetChangeStatus) {
        this.mWidgetChangeStatus = widgetChangeStatus;
    }

    public int getChannelId() {
        return this.mChannelId;
    }

    public void setChannelId(int channelId) {
        this.mChannelId = channelId;
    }

    public boolean isNew() {
        return this.mIsNew;
    }

    public void setIsNew(boolean isNew) {
        this.mIsNew = isNew;
    }

    public String getResourceId() {
        return this.mResourceId;
    }

    public void setResourceId(String resourceId) {
        this.mResourceId = resourceId;
    }

    public String toString() {
        return "(name = " + getTitle() + ", resource = " + this.mResourceId + ")";
    }
}
