package com.gala.video.lib.share.uikit.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.adapter.GroupBaseAdapter;
import com.gala.video.lib.share.uikit.contract.HScrollContract;
import com.gala.video.lib.share.uikit.contract.HScrollContract.Presenter;
import com.gala.video.lib.share.uikit.core.BinderViewHolder;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.protocol.ServiceManager;
import com.gala.video.lib.share.uikit.resolver.BaseBinderResolver;
import com.gala.video.lib.share.uikit.resolver.BaseItemBinderResolver;
import java.util.ArrayList;
import java.util.List;

public class HScrollItem extends Item implements Presenter {
    private BaseAdapter mBaseAdapter;
    private List<Item> mItems = new ArrayList();
    private CardInfoModel mModel;
    private ServiceManager mServiceManager;

    public static class BaseAdapter extends GroupBaseAdapter<Item> {
        public BaseAdapter(Context context, BaseBinderResolver<Item> baseViewBinderResolver) {
            super(context, baseViewBinderResolver);
        }

        public void onBindViewHolder(BinderViewHolder<Item, ? extends View> holder, int position) {
            super.onBindViewHolder((BinderViewHolder) holder, position);
            ItemInfoModel itemInfoModel = ((Item) holder.data).getModel();
            LayoutParams params = holder.itemView.getLayoutParams();
            params.width = ((Item) holder.data).getWidth();
            params.height = ((Item) holder.data).getHeight();
        }
    }

    public BaseAdapter getAdapter() {
        if (this.mBaseAdapter == null) {
            this.mBaseAdapter = new BaseAdapter((Context) this.mServiceManager.getService(Context.class), (BaseItemBinderResolver) this.mServiceManager.getService(BaseItemBinderResolver.class));
            this.mBaseAdapter.setData(this.mItems);
        }
        return this.mBaseAdapter;
    }

    public void setItems(List<Item> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.mServiceManager = serviceManager;
    }

    public ServiceManager getServiceManager() {
        return this.mServiceManager;
    }

    public void setCardModel(CardInfoModel model) {
        this.mModel = model;
    }

    public CardInfoModel getCardModel() {
        return this.mModel;
    }

    public ActionPolicy getActionPolicy() {
        return null;
    }

    public void setFocusPosition(int position) {
    }

    public void setView(HScrollContract.View view) {
    }

    public int getWidth() {
        return -1;
    }

    public int getHeight() {
        return this.mModel.getBodyHeight();
    }

    public int getType() {
        return 106;
    }
}
