package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.text.TextUtils;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.uikit.contract.SettingItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;

public class SettingItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    public SettingItemView(Context context) {
        super(context);
    }

    public void onBind(Presenter object) {
        setStyleByName(object.getModel().getStyle());
        setTag(R.id.focus_res_ends_with, object.getModel().getSkinEndsWith());
        object.setItemView(this);
        ItemInfoModel itemInfoModel = object.getModel();
        updateUI(itemInfoModel);
        setLTDes(object.getLTDes());
        setContentDescription(itemInfoModel.getCuteViewData("ID_TITLE", "text"));
    }

    public void onUnbind(Presenter object) {
        recycle();
    }

    public void onShow(Presenter object) {
    }

    public void onHide(Presenter object) {
    }

    public void setLTDes(String des) {
        if (getLTBubbleView() != null) {
            getLTBubbleView().setText(des);
        }
        if (getLTCorner() != null) {
            getLTCorner().setVisible(TextUtils.isEmpty(des) ? 0 : 1);
        }
    }

    private CuteText getLTBubbleView() {
        return getCuteText("ID_LT_BUBBLE");
    }

    private CuteImage getLTCorner() {
        return getCuteImage("ID_CORNER_L_T");
    }
}
