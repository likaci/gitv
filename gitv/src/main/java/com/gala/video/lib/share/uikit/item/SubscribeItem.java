package com.gala.video.lib.share.uikit.item;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.subscribe.SubscribeObserver;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.uikit.action.data.SubscribeBtnActionData;
import com.gala.video.lib.share.uikit.action.model.SubscribeBtnActionModel;
import com.gala.video.lib.share.uikit.contract.StandardItemContract;
import com.gala.video.lib.share.uikit.contract.SubscribeItemContract.Presenter;
import com.gala.video.lib.share.uikit.contract.SubscribeItemContract.View;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.uikit.utils.TypeUtils;

public class SubscribeItem extends Item implements Presenter {
    private static final String TAG = "SubscribeItem";
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private SubscribeObserver mObserver = new SubscribeObserver() {
        public void onItemState(String qpid, int state) {
            boolean isFeatureFilm = "1".equals(SubscribeItem.this.mItemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_FEATUREFILM));
            LogUtils.d(SubscribeItem.TAG, hashCode() + "@--observer,onItemState== qpid=" + qpid + ",state=" + state + ",isFeatureFilm=" + isFeatureFilm);
            SubscribeItem.this.mSubscribeType = state;
            SubscribeItem.this.mSubscribeType = isFeatureFilm ? 3 : SubscribeItem.this.mSubscribeType;
            SubscribeItem.this.mHandler.post(new Runnable() {
                public void run() {
                    if (SubscribeItem.this.mView != null) {
                        SubscribeItem.this.mView.updateBtn(SubscribeItem.this.mSubscribeType);
                    }
                }
            });
        }
    };
    private int mSubscribeType = 0;
    private View mView;

    public int getType() {
        return 215;
    }

    public void setModel(ItemInfoModel itemInfoModel) {
        super.setModel(itemInfoModel);
        if ("1".equals(itemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_FEATUREFILM))) {
            this.mSubscribeType = 3;
        }
    }

    public void addSubscribeObserver() {
        GetInterfaceTools.getISubscribeProvider().removeObserver(this.mObserver);
        GetInterfaceTools.getISubscribeProvider().addObserver(this.mObserver, this.mItemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_QPID));
    }

    public void removeSubscribeObserver() {
        GetInterfaceTools.getISubscribeProvider().removeObserver(this.mObserver);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void onBtnClick(CharSequence title, Context context) {
        SubscribeBtnActionModel actionModel = new SubscribeBtnActionModel();
        SubscribeBtnActionData data = new SubscribeBtnActionData();
        data.setQpId(this.mItemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_QPID));
        data.setChannelLabelActionModel(this.mItemInfoModel.getActionModel());
        data.setSubscribeType(getSubscribeType());
        data.setChnId(TypeUtils.castToInt(this.mItemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_CHNID)));
        data.setCardId(getParent().getModel().mCardId);
        data.setLine(ClickPingbackUtils.getLine(getParent().getParent(), getParent(), this));
        data.setCardLine(getLine());
        data.setAllLine("" + getParent().getAllLine());
        data.setPos(0);
        actionModel.buildActionModel(data).onItemClick(context);
        ClickPingbackUtils.subscribeClickForPingbackPost(context, data);
    }

    public int getSubscribeType() {
        return (this.mItemInfoModel == null || !"1".equals(this.mItemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_FEATUREFILM))) ? this.mSubscribeType : 3;
    }

    public void addLiveCornerObserver() {
    }

    public void setView(StandardItemContract.View view) {
    }

    public void removeLiveCornerObserver() {
    }

    public void setPlayingGif(boolean showGif) {
    }
}
