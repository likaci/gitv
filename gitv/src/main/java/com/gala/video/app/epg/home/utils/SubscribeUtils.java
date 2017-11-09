package com.gala.video.app.epg.home.utils;

import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class SubscribeUtils {
    public static String getQpId(ItemData data) {
        if (data.getItemType() == ItemDataType.LIVE) {
            return data.mLabel.itemId;
        }
        return data.mLabel.tvQipuId;
    }
}
