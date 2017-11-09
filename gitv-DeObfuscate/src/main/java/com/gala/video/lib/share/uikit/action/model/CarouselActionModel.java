package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class CarouselActionModel extends BaseActionModel<ChannelLabel> {
    private ChannelLabel mLabel;

    public CarouselActionModel(ItemDataType itemDataType, ChannelLabel label) {
        super(itemDataType);
        this.mLabel = label;
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mLabel = label;
        return this;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }
}
