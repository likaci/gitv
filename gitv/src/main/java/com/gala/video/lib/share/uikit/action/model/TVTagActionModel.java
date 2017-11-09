package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class TVTagActionModel extends BaseActionModel<ChannelLabel> {
    private ItemKvs mItemKvs;
    private ChannelLabel mLabel;
    private TVTags mTvTags;

    public TVTagActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mTvTags = label.itemKvs.getTVTag();
        this.mItemKvs = label.itemKvs;
        this.mLabel = label;
        return this;
    }

    public TVTags getTvTags() {
        return this.mTvTags;
    }

    public ItemKvs getItemKvs() {
        return this.mItemKvs;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }
}
