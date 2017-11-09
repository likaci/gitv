package com.gala.video.lib.share.ifimpl.multisubject;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.MultiSubjectImp;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.ShareDebug;

public class MultiSubjectHAdapter extends Adapter<PrivateHViewHolder> {
    private String TAG = "EPG/multisubject/MultiSubjectHAdapter";
    private CardModel mCardModel;
    private Context mContext;
    private boolean mInvalidateBitmap;

    public class PrivateHViewHolder extends ViewHolder {
        public MultiSubjectImp item;

        public PrivateHViewHolder(MultiSubjectImp item) {
            super(item.onCreateViewHolder(MultiSubjectHAdapter.this.mContext));
            this.item = item;
        }
    }

    public MultiSubjectHAdapter(Context context) {
        this.mContext = context;
    }

    public void setHData(CardModel cardModel) {
        this.mCardModel = cardModel;
    }

    public int getCount() {
        return this.mCardModel == null ? 0 : this.mCardModel.getSize();
    }

    public void setInvalidateBitmap(boolean invalidateBitmap) {
        this.mInvalidateBitmap = invalidateBitmap;
    }

    public void onBindViewHolder(PrivateHViewHolder holder, int position) {
        if (ListUtils.isLegal(this.mCardModel.getItemModelList(), position)) {
            ItemModel itemModel = (ItemModel) this.mCardModel.getItemModelList().get(position);
            holder.item.onBindViewHolder(CreateInterfaceTools.createModelHelper().convertToDataSource(itemModel));
            holder.itemView.setFocusable(true);
            LayoutParams params = holder.itemView.getLayoutParams();
            int itemWidth = holder.item.getWidth();
            int itemHeight = holder.item.getHeight();
            int imageH = GetInterfaceTools.getMultiSubjectViewFactory().getItemImageHeight(this.mCardModel.getWidgetType(), 0);
            int extraH = GetInterfaceTools.getMultiSubjectViewFactory().getItemExtraHeight(this.mCardModel.getWidgetType(), itemModel.getWidgetType());
            int pLR = GetInterfaceTools.getMultiSubjectViewFactory().getItemNinePatchLeftRight();
            if (itemHeight == 0) {
                itemWidth = 250;
                LogUtils.e(this.TAG, "onBindViewHolder itemHeight = 0, dataSource.title = ", dataSource.getTitle());
            } else if (itemHeight > 0 && imageH > 0 && itemHeight != imageH) {
                itemWidth = (itemWidth * imageH) / itemHeight;
            }
            params.height = ResourceUtil.getPx(imageH + extraH);
            params.width = ResourceUtil.getPx((pLR * 2) + itemWidth);
            holder.item.setActualItemWidth(itemWidth);
            holder.item.setActualItemHeight(imageH);
            if (this.mInvalidateBitmap) {
                holder.item.loadImage();
            }
            if (ShareDebug.DEBUG_LOG) {
                Log.e(this.TAG, "hAdapter,onBindViewHolder,pos=" + position + ",cardModel.getWidgetType()=" + this.mCardModel.getWidgetType() + ",params.width=" + params.width + ",params. height=" + params.height + ",mInvalidateBitmap=" + this.mInvalidateBitmap);
            }
        }
    }

    public PrivateHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrivateHViewHolder(GetInterfaceTools.getMultiSubjectViewFactory().createItem(viewType));
    }

    public int getItemViewType(int position) {
        if (ListUtils.isLegal(this.mCardModel.getItemModelList(), position)) {
            return ((ItemModel) this.mCardModel.getItemModelList().get(position)).getWidgetType();
        }
        return 0;
    }
}
