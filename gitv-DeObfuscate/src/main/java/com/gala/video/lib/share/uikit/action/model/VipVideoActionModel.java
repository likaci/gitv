package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;

public class VipVideoActionModel extends BaseActionModel<ChannelLabel> {
    private transient boolean isOpenapi;
    private transient int mFlag;
    private String mTitle;

    public VipVideoActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        this.mTitle = DataBuildTool.getPrompt(label);
        return this;
    }

    public void setFlag(int flag) {
        this.mFlag = flag;
    }

    public void setOpenapi(boolean openapi) {
        this.isOpenapi = openapi;
    }

    public int getFlag() {
        return this.mFlag;
    }

    public boolean isOpenapi() {
        return this.isOpenapi;
    }

    public String getTitle() {
        return this.mTitle;
    }
}
