package com.gala.video.lib.share.uikit.action.model;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.UcenterRecordAllData;

public class UcenterRecordAllActionModel extends BaseActionModel<UcenterRecordAllData> {
    private UcenterRecordAllData mUcenterRecordAllData;

    public UcenterRecordAllActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(UcenterRecordAllData dataSource) {
        this.mUcenterRecordAllData = dataSource;
        return this;
    }

    public UcenterRecordAllData getUcenterRecordAllData() {
        return this.mUcenterRecordAllData;
    }
}
