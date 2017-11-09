package com.gala.video.app.player.albumdetail.ui.card;

import android.util.Log;
import com.gala.video.albumlist.widget.BlocksView.LayoutParams;
import com.gala.video.app.player.albumdetail.ui.card.BasicContract.Presenter;
import com.gala.video.app.player.albumdetail.ui.card.BasicContract.View;
import com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay;
import com.gala.video.lib.share.uikit.item.Item;

public class BasicInfoItem extends Item implements Presenter {
    private static final String TAG = "BasicInfoItem";
    DetailOverlay mDetailOverlay;
    BasicInfoContent mView;

    public int getType() {
        return 1000;
    }

    public void setDetailOverlay(DetailOverlay albumCardCreat) {
        this.mDetailOverlay = albumCardCreat;
    }

    public BasicInfoContent getView() {
        return this.mView;
    }

    public void setView(View view) {
        this.mView = (BasicInfoContent) view;
    }

    public void setViewLayoutParams(LayoutParams layoutParams) {
        setWidth(layoutParams.width);
        setHeight(layoutParams.height);
        Log.v(TAG, "view.getLayoutParams().height = " + layoutParams.height);
        Log.v(TAG, "view.getLayoutParams().width = " + layoutParams.width);
    }

    public DetailOverlay getDetailOverlay() {
        return this.mDetailOverlay;
    }
}
