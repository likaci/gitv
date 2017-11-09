package com.gala.video.lib.share.uikit.card;

import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.LinearLayout;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.LoadingItem;
import java.util.Collections;
import java.util.List;

public class LoadingCard extends Card {
    private LoadingItem mItem = new LoadingItem();

    public void parserItems(CardInfoModel cardInfoModel) {
        super.parserItems(cardInfoModel);
        if (this.mItem == null) {
            this.mItem = new LoadingItem();
        }
        this.mItem.assignParent(this);
        this.mItem.setHeight(cardInfoModel.getBodyHeight());
        this.mItem.setWidth(-1);
    }

    public List<Item> getItems() {
        return Collections.singletonList(this.mItem);
    }

    public BlockLayout onCreateBlockLayout() {
        LinearLayout layout = new LinearLayout();
        layout.setItemCount(1);
        return layout;
    }
}
