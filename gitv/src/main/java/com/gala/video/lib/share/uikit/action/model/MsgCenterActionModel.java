package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;

public class MsgCenterActionModel extends BaseActionModel<ChannelLabel> {
    private String mTitle;

    public MsgCenterActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mTitle = DataBuildTool.getPrompt(label);
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }
}
