package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;

public class PersonActionModel extends BaseActionModel<ChannelLabel> {
    private String from;
    private String mId;
    private String mItemId;
    private String mPingback;
    private String mTitle;

    public PersonActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mTitle = DataBuildTool.getPrompt(label);
        this.mItemId = label.itemId;
        this.from = PingBackUtils.getTabName() + "_明星";
        this.mId = label.id;
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getItemId() {
        return this.mItemId;
    }

    public String getFrom() {
        return this.from;
    }

    public String getId() {
        return this.mId;
    }
}
