package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class H5ActionModel extends BaseActionModel<ChannelLabel> {
    private ChannelLabel mLabel;

    public H5ActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mLabel = label;
        return this;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }
}
