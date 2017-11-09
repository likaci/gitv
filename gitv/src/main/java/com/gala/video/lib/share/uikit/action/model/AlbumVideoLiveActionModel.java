package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;

public class AlbumVideoLiveActionModel extends BaseActionModel<ChannelLabel> {
    private IMultiSubjectInfoModel mIntentModel;
    private ChannelLabel mLabel;
    private String mPingbackFrom;
    private String mTitle;

    public IMultiSubjectInfoModel getIntentModel() {
        return this.mIntentModel;
    }

    public void setIntentModel(IMultiSubjectInfoModel mIntentModel) {
        this.mIntentModel = mIntentModel;
    }

    public AlbumVideoLiveActionModel(ItemDataType itemDataType, ChannelLabel dataSource) {
        super(itemDataType);
        this.mLabel = dataSource;
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mTitle = DataBuildTool.getPrompt(label);
        this.mPingbackFrom = PingBackUtils.getTabName() + "_rec";
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getFrom() {
        return this.mPingbackFrom;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString()).append(" ").append(this.TAG);
        sb.append(": mTitle = ").append(this.mTitle).append(" from = " + this.mPingbackFrom);
        return sb.toString();
    }
}
