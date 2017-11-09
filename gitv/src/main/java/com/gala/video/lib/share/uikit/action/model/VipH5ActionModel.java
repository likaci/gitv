package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;

public class VipH5ActionModel extends BaseActionModel<ChannelLabel> {
    private ChannelLabel mLabel;
    private String mTitle;

    public VipH5ActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mLabel = label;
        this.mTitle = DataBuildTool.getPrompt(label);
        return this;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }

    public String getTitle() {
        return this.mTitle;
    }
}
