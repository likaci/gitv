package com.gala.video.lib.share.uikit.action.model;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.SubscribeBtnActionData;

public class SubscribeBtnActionModel extends BaseActionModel<SubscribeBtnActionData> {
    private BaseActionModel mChannelLabelActionModel;
    private String mQpId;
    private int mSubscribeType;

    public SubscribeBtnActionModel() {
        super(ItemDataType.SUBSCRIBE_BTN);
    }

    public BaseActionModel buildActionModel(SubscribeBtnActionData data) {
        this.mChannelLabelActionModel = data.getChannelLabelActionModel();
        this.mQpId = data.getQpId();
        this.mSubscribeType = data.getSubscribeType();
        return this;
    }

    public String getQpId() {
        return this.mQpId;
    }

    public void setQpId(String qpId) {
        this.mQpId = qpId;
    }

    public int getSubscribeType() {
        return this.mSubscribeType;
    }

    public BaseActionModel getChannelLabelActionModel() {
        return this.mChannelLabelActionModel;
    }
}
