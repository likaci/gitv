package com.gala.video.lib.share.uikit.action.model;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.CommonAdData;

public class AdActionModel extends BaseActionModel<HomeFocusImageAdModel> {
    private CommonAdData mCommonAdData;

    public CommonAdData getCommonAdData() {
        return this.mCommonAdData;
    }

    public AdActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public void onItemClick(Context context) {
        GetInterfaceTools.getIActionJump().onItemClickAD(context, this.mCommonAdData);
    }

    public BaseActionModel buildActionModel(HomeFocusImageAdModel dataSource) {
        this.mCommonAdData = CommonAdData.convertAdModel(dataSource);
        return super.buildActionModel(dataSource);
    }
}
