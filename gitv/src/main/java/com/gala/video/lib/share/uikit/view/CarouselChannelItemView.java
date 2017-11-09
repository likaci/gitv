package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import com.gala.video.lib.share.uikit.contract.ItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;

public class CarouselChannelItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    private String TAG = "CarouselChannelItemView";
    private ItemInfoModel mItemModel;

    public CarouselChannelItemView(Context context) {
        super(context);
    }

    public void onBind(Presenter object) {
        loadUIStyle(object);
        this.mItemModel = object.getModel();
        updateUI(this.mItemModel);
        setContentDescription(this.mItemModel.getCuteViewData("ID_TITLE", "text"));
    }

    public void onUnbind(Presenter object) {
        recycle();
    }

    public void onShow(Presenter object) {
    }

    public void onHide(Presenter object) {
    }

    protected void loadUIStyle(Presenter object) {
        setStyleByName(object.getModel().getStyle());
    }
}
