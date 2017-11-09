package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Color;
import com.gala.cloudui.block.CuteText;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.contract.SubscribeItemContract.Presenter;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;

public class SubscribeBtnItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    public SubscribeBtnItemView(Context context) {
        super(context);
    }

    public void onBind(Presenter object) {
        setStyleByName(StringUtils.append("subscribebtn", object.getModel().getSkinEndsWith()));
        setTag(CardFocusHelper.FocusRect, Boolean.valueOf(true));
        updateState(object.getSubscribeType());
        setContentDescription(getTitleView().getText());
    }

    public void onUnbind(Presenter object) {
        recycle();
    }

    public void onShow(Presenter object) {
    }

    public void onHide(Presenter object) {
    }

    private CuteText getTitleView() {
        return getCuteText("ID_TITLE");
    }

    public void updateState(int subscribeType) {
        CuteText titleView = getTitleView();
        if (titleView != null) {
            if (subscribeType == 1 || subscribeType == 2) {
                titleView.setFontColor(Color.parseColor("#ffb400"));
                titleView.setFocusFontColor(Color.parseColor("#f1f1f1"));
                titleView.setText("预约成功");
            } else if (subscribeType == -1) {
                titleView.setFontColor(Color.parseColor("#818181"));
                titleView.setFocusFontColor(Color.parseColor("#f1f1f1"));
                titleView.setText("暂不支持预约");
            } else if (subscribeType == 3) {
                titleView.setFontColor(Color.parseColor("#b2b2b2"));
                titleView.setFocusFontColor(Color.parseColor("#f1f1f1"));
                titleView.setText("正在热播");
            } else {
                titleView.setFontColor(Color.parseColor("#b2b2b2"));
                titleView.setFocusFontColor(Color.parseColor("#f1f1f1"));
                titleView.setText("预约");
            }
        }
    }
}
