package com.gala.video.app.player.ui.overlay.contents;

import android.view.View;

public interface IContent<DataType, ItemType> {

    public interface IItemListener<ItemType> {
        void onItemClicked(ItemType itemType, int i);

        void onItemFilled();

        void onItemSelected(ItemType itemType, int i);
    }

    DataType getContentData();

    View getFocusableView();

    String getTitle();

    View getView();

    void hide();

    void setData(DataType dataType);

    void setItemListener(IItemListener<ItemType> iItemListener);

    void setSelection(ItemType itemType);

    void show();
}
