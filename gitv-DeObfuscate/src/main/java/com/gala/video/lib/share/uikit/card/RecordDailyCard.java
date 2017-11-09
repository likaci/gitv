package com.gala.video.lib.share.uikit.card;

import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.RecordItem;

public class RecordDailyCard extends GridCard {
    public void setModel(CardInfoModel model) {
        super.setModel(model);
        int itemCount = ListUtils.getCount(this.mItems);
        for (int i = 0; i < itemCount; i++) {
            Item item = (Item) this.mItems.get(i);
            if (217 == item.getType()) {
                ((RecordItem) item).setPos(i + 1);
            }
        }
    }
}
