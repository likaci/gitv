package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import java.util.List;

public class DailyActionModel extends BaseActionModel<ChannelLabel> {
    private List<DailyLabelModel> mDailyLabelModelList;
    private int mNewParamsPos;

    public DailyActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public void setNewParamsPos(int pos) {
        this.mNewParamsPos = pos;
    }

    public void setDailyLabelModelList(List<DailyLabelModel> dailyLabelModelList) {
        this.mDailyLabelModelList = dailyLabelModelList;
    }

    public int getNewParamsPos() {
        return this.mNewParamsPos;
    }

    public List<DailyLabelModel> getDailyLabelModelList() {
        return this.mDailyLabelModelList;
    }
}
