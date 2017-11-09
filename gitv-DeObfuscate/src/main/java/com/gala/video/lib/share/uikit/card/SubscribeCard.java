package com.gala.video.lib.share.uikit.card;

import android.view.ViewGroup;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.TimeLineCard.TimeLineActionPolicy;
import com.gala.video.lib.share.uikit.contract.SubscribeCardContract.Presenter;
import com.gala.video.lib.share.uikit.contract.SubscribeItemContract;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SubscribeCard extends TimeLineCard implements Presenter {
    private static final String TAG = "SubscribeCard";

    public class SubscribeActionPolicy extends TimeLineActionPolicy {
        public SubscribeActionPolicy(Card card) {
            super(card);
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            SubscribeCard.this.getHeaderItem().setFocusPosition(hasFocus ? holder.getLayoutPosition() - SubscribeCard.this.getBlockLayout().getFirstPosition() : -1);
        }
    }

    public void setModel(CardInfoModel cardInfoModel) {
        super.setModel(cardInfoModel);
        registerItemObserver();
        updateSubscribeState();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterItemObserver();
    }

    private void registerItemObserver() {
        List items = getItems();
        int count = ListUtils.getCount(items);
        LogUtils.m1585d(TAG, hashCode() + "@registerItemObserver,count=" + count);
        for (int i = 0; i < count; i++) {
            Item item = (Item) items.get(i);
            if (item != null && (item instanceof SubscribeItemContract.Presenter)) {
                ((SubscribeItemContract.Presenter) item).addSubscribeObserver();
            }
        }
    }

    private void unregisterItemObserver() {
        List items = getItems();
        int count = ListUtils.getCount(items);
        LogUtils.m1585d(TAG, hashCode() + "@unregisterItemObserver,count=" + count);
        for (int i = 0; i < count; i++) {
            Item item = (Item) items.get(i);
            if (item != null && (item instanceof SubscribeItemContract.Presenter)) {
                ((SubscribeItemContract.Presenter) item).removeSubscribeObserver();
            }
        }
    }

    public void updateSubscribeState() {
        LogUtils.m1585d(TAG, hashCode() + "@updateSubscribeState");
        ItemInfoModel[] items = this.mCardInfoModel.getItemInfoModels()[0];
        int count = ListUtils.getArraySize(items);
        final Collection list = new LinkedList();
        for (int i = 0; i < count; i++) {
            ItemInfoModel itemInfoModel = items[i];
            if (itemInfoModel != null) {
                if (!"1".equals(itemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_FEATUREFILM))) {
                    list.add(itemInfoModel.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_QPID));
                }
            }
        }
        if (ListUtils.getCount((List) list) != 0) {
            GetInterfaceTools.getISubscribeProvider().getSubscribeState(new IVrsCallback<ApiResult>() {
                public void onSuccess(ApiResult apiResult) {
                    LogUtils.m1585d(SubscribeCard.TAG, SubscribeCard.this.hashCode() + "@success,list.size:" + list.size());
                }

                public void onException(ApiException e) {
                    LogUtils.m1588e(SubscribeCard.TAG, SubscribeCard.this.hashCode() + "@exception " + e + ",list.size:" + list.size());
                }
            }, list);
        }
    }

    public ActionPolicy getActionPolicy() {
        return new SubscribeActionPolicy(this);
    }
}
