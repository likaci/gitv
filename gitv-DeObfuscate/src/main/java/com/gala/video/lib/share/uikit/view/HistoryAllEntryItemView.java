package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;

public class HistoryAllEntryItemView extends UIKitCloudItemView implements IViewLifecycle {
    public HistoryAllEntryItemView(Context context) {
        super(context);
    }

    public void onBind(Object object) {
        setStyleByName("history_all_entry");
    }

    public void onUnbind(Object object) {
        recycle();
    }

    public void onShow(Object object) {
    }

    public void onHide(Object object) {
    }
}
