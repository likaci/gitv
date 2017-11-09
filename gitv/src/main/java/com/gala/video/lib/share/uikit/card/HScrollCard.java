package com.gala.video.lib.share.uikit.card;

import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.LinearLayout;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.HScrollItem;
import java.util.Collections;

public class HScrollCard extends Card {
    private HScrollItem mItem;

    public void parserItems(CardInfoModel cardInfoModel) {
        if (this.mItem == null) {
            this.mItem = new HScrollItem();
        }
        super.parserItems(cardInfoModel);
        this.mItem.setItems(getItems());
        this.mItem.setServiceManager(getServiceManager());
        this.mItem.setCardModel(getModel());
        this.mItem.assignParent(this);
        setItems(Collections.singletonList(this.mItem));
    }

    public BlockLayout onCreateBlockLayout() {
        LinearLayout layout = new LinearLayout();
        layout.setItemCount(1);
        return layout;
    }
}
