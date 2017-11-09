package com.gala.video.lib.share.uikit.action.model;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.CommonAdData;
import com.gala.video.lib.share.uikit.loader.data.BannerAd;

public class BannerAdActionModel extends BaseActionModel<BannerAd> {
    private CommonAdData mCommonAdData;

    public CommonAdData getCommonAdData() {
        return this.mCommonAdData;
    }

    public BannerAdActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public void onItemClick(Context context) {
        GetInterfaceTools.getIActionJump().onItemClickAD(context, this.mCommonAdData);
    }

    public BaseActionModel buildActionModel(BannerAd dataSource) {
        this.mCommonAdData = CommonAdData.convertBannerAdModel(dataSource);
        return super.buildActionModel(dataSource);
    }
}
