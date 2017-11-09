package com.gala.video.app.epg.home.widget.menufloatlayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist4.utils.AnimationUtils;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.widget.menufloatlayer.MenuFloatLayerItemClickUtils;
import com.gala.video.app.epg.home.widget.menufloatlayer.data.MenuFloatLayerItemModel;
import com.gala.video.app.epg.home.widget.menufloatlayer.ui.MenuFloatLayerItemView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import java.util.ArrayList;
import java.util.List;

public class MenuFloatLayerAdapter implements MenuFloatLayerStateListener {
    private static final String TAG = "home/widget/MenuFloatLayerAdapter";
    private Context mContext;
    private List<MenuFloatLayerItemModel> mItemInfoList = new ArrayList();

    public MenuFloatLayerAdapter(Context context, List<MenuFloatLayerItemModel> models) {
        this.mContext = context;
        this.mItemInfoList = models;
    }

    public void onChildFocusChanged(View view, boolean hasFocus) {
        AnimationUtils.zoomAnimation(view, hasFocus, 1.1f, 200, true);
        if (view instanceof MenuFloatLayerItemView) {
            MenuFloatLayerItemView menuFloatLayerItemView = (MenuFloatLayerItemView) view;
            if (hasFocus) {
                menuFloatLayerItemView.setTextColor(Color.parseColor("#f1f1f1"));
            } else {
                menuFloatLayerItemView.setTextColor(Color.parseColor("#b2b2b2"));
            }
        }
    }

    public void onClick(View view) {
        if (view instanceof MenuFloatLayerItemView) {
            MenuFloatLayerItemClickUtils.onClick(this.mContext, (MenuFloatLayerItemView) view);
        }
    }

    public int getCount() {
        return this.mItemInfoList.size();
    }

    public int getItemId(int pos) {
        return pos;
    }

    public View getView(int position, View view, ViewGroup parent) {
        MenuFloatLayerItemView itemView = new MenuFloatLayerItemView(this.mContext);
        itemView.setOrientation(1);
        MenuFloatLayerItemModel menuFloatLayerItemModel = (MenuFloatLayerItemModel) this.mItemInfoList.get(position);
        String title = menuFloatLayerItemModel.getTitle();
        int iconResId = menuFloatLayerItemModel.getIconResId();
        ItemDataType itemDataType = menuFloatLayerItemModel.getItemType();
        LogUtils.m1568d(TAG, "getView, menu float layer item,  title = " + title + " item type = " + itemDataType);
        itemView.setText(title);
        itemView.setItemType(itemDataType);
        itemView.setBackgroundResource(C0508R.drawable.epg_home_menu_float_layer_item_selector);
        itemView.setIconDrawable(iconResId);
        return itemView;
    }
}
