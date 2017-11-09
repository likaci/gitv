package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class RecommendAppActionModel extends BaseActionModel<ChannelLabel> {
    private ChannelLabel mChannelLable;

    public RecommendAppActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mChannelLable = label;
        return super.buildActionModel(label);
    }

    public ChannelLabel getChannelLable() {
        return this.mChannelLable;
    }
}
