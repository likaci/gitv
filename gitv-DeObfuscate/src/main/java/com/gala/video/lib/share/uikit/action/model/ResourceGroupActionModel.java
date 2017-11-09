package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class ResourceGroupActionModel extends BaseActionModel<ChannelLabel> {
    private String from;
    private String mBuySource;
    private String mItemId;
    private String mPingback;

    public ResourceGroupActionModel(ItemDataType itemDataType) {
        super(itemDataType);
        this.mBuySource = "tabrec";
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mItemId = label.itemId;
        this.from = PingBackUtils.getTabName() + "_rec";
        return this;
    }

    public String getItemId() {
        return this.mItemId;
    }

    public String getFrom() {
        return this.from;
    }

    public String getBuySource() {
        return this.mBuySource;
    }
}
