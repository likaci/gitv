package com.gala.video.app.epg.home.widget.menufloatlayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist4.utils.AnimationUtils;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.app.epg.home.widget.menufloatlayer.MenuFloatLayerItemClickUtils;
import com.gala.video.app.epg.home.widget.menufloatlayer.data.MenuFloatLayerItemModel;
import com.gala.video.app.epg.home.widget.menufloatlayer.ui.MenuFloatLayerSettingItemView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class MenuFloatLayerSettingListAdapter extends Adapter<ItemViewHolder> {
    private static final String TAG = "menufloatlayer/adapter/MenuFloatLayerSettingListAdapter";
    private Context mContext;
    private List<MenuFloatLayerItemModel> mDataList;

    class ItemViewHolder extends ViewHolder {
        public ItemViewHolder(View v) {
            super(v);
        }
    }

    public MenuFloatLayerSettingListAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataList(List<MenuFloatLayerItemModel> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void updateDataList(List<MenuFloatLayerItemModel> list) {
        this.mDataList = list;
        notifyDataSetUpdate();
    }

    public List<MenuFloatLayerItemModel> getDataList() {
        return this.mDataList;
    }

    public void onClick(ViewGroup viewGroup, ViewHolder viewHolder) {
        MenuFloatLayerItemClickUtils.onSettingItemClick(this.mContext, (MenuFloatLayerSettingItemView) viewHolder.itemView);
    }

    public void onItemFocusChange(ViewGroup viewGroup, ViewHolder viewHolder, boolean hasFocus) {
        if (viewHolder != null) {
            AnimationUtils.zoomAnimation(viewHolder.itemView, hasFocus, 1.1f, 200, false);
        }
    }

    public int getCount() {
        return ListUtils.getCount(this.mDataList);
    }

    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        if (viewHolder != null && viewHolder.itemView != null && ListUtils.isLegal(this.mDataList, position)) {
            viewHolder.itemView.setFocusable(true);
            MenuFloatLayerItemModel model = (MenuFloatLayerItemModel) this.mDataList.get(position);
            if (model != null) {
                ((MenuFloatLayerSettingItemView) viewHolder.itemView).setTitle(model.getTitle());
                ((MenuFloatLayerSettingItemView) viewHolder.itemView).setNormalDrawable(ResourceUtil.getDrawable(model.getIconResId()));
                ((MenuFloatLayerSettingItemView) viewHolder.itemView).setFocusDrawable(ResourceUtil.getDrawable(model.getFocusIconResId()));
                ((MenuFloatLayerSettingItemView) viewHolder.itemView).setItemType(model.getItemType());
                if (model.getItemType() != ItemDataType.SYSTEM_UPGRADE) {
                    return;
                }
                if (UpdateManager.getInstance().isNeedShowNewIcon()) {
                    ((MenuFloatLayerSettingItemView) viewHolder.itemView).setTipView(true);
                    ((MenuFloatLayerSettingItemView) viewHolder.itemView).setTipText("新");
                    return;
                }
                ((MenuFloatLayerSettingItemView) viewHolder.itemView).setTipView(false);
                ((MenuFloatLayerSettingItemView) viewHolder.itemView).setTipText("");
            }
        }
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ItemViewHolder(new MenuFloatLayerSettingItemView(this.mContext));
    }
}
