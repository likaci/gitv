package com.gala.video.app.epg.home.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;

public class PageData extends DataSource {
    private boolean mIsNew;
    private String mResourceId;
    private WidgetChangeStatus mStatus = WidgetChangeStatus.NoChange;

    public String getResourceId() {
        return this.mResourceId;
    }

    public void setResourceId(String resourceId) {
        this.mResourceId = resourceId;
    }

    public boolean isNew() {
        return this.mIsNew;
    }

    public void setIsNew(boolean isNew) {
        this.mIsNew = isNew;
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mStatus;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus status) {
        this.mStatus = status;
    }
}
