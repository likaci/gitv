package com.gala.video.app.epg.uikit.card;

import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.LinearLayout;
import com.gala.video.app.epg.uikit.item.UCenterItem;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import java.util.Collections;
import java.util.List;

public class UCenterCard extends Card {
    private UCenterItem mItem = new UCenterItem();

    public void parserItems(CardInfoModel cardInfoModel) {
        super.parserItems(cardInfoModel);
        if (this.mItem == null) {
            this.mItem = new UCenterItem();
        }
        this.mItem.assignParent(this);
        this.mItem.setHeight(cardInfoModel.getBodyHeight());
        this.mItem.setWidth(-1);
    }

    public BlockLayout onCreateBlockLayout() {
        LinearLayout layout = new LinearLayout();
        layout.setItemCount(1);
        return layout;
    }

    public List<Item> getItems() {
        return Collections.singletonList(this.mItem);
    }
}
