package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class PlstGroupActionModel extends BaseActionModel<ChannelLabel> {
    private String from;
    private String mPingback;
    private String plId;

    public PlstGroupActionModel(ItemDataType itemDataType) {
        super(itemDataType);
        this.from = PingBackUtils.getTabName() + "_专题回顾";
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.plId = label.itemKvs.dataid;
        return this;
    }

    public void setPlId(String plId) {
        this.plId = plId;
    }

    public String getFrom() {
        return this.from;
    }

    public String getPlId() {
        return this.plId;
    }
}
