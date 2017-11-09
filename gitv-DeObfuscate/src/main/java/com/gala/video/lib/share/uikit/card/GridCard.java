package com.gala.video.lib.share.uikit.card;

import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.GridLayout;
import com.gala.video.albumlist.layout.GridLayout.CountCallback;
import com.gala.video.albumlist.layout.GridLayout.NumRowsController;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;

public class GridCard extends Card {
    public BlockLayout onCreateBlockLayout() {
        GridLayout layout = new GridLayout();
        NumRowsController numRowsController = new NumRowsController();
        for (int i = 0; i < ListUtils.getArraySize(this.mCardInfoModel.getItemInfoModels()); i++) {
            ItemInfoModel[] itemInfoModels = this.mCardInfoModel.getItemInfoModels()[i];
            if (itemInfoModels != null) {
                int size = 0;
                for (int j = 0; j < ListUtils.getArraySize(itemInfoModels); j++) {
                    if (itemInfoModels[j] != null) {
                        size++;
                    }
                }
                final int count = size;
                numRowsController.add(count, new CountCallback() {
                    public int count() {
                        return count;
                    }
                });
            }
        }
        layout.setItemCount(this.mItems.size());
        layout.setNumRowsController(numRowsController);
        return layout;
    }
}
