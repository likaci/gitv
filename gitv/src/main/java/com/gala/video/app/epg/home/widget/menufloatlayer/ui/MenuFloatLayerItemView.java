package com.gala.video.app.epg.home.widget.menufloatlayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MenuFloatLayerItemView extends LinearLayout {
    private Context mContext;
    private ImageView mIconImv;
    private ItemDataType mItemDataType;
    private TextView mNameTxt;

    public MenuFloatLayerItemView(Context context) {
        super(context);
        initView(context);
    }

    public MenuFloatLayerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MenuFloatLayerItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.epg_home_menu_float_layer_item, this, true);
        this.mIconImv = (ImageView) findViewById(R.id.epg_menu_float_layer_item_icon);
        this.mNameTxt = (TextView) findViewById(R.id.epg_menu_float_layer_item_name);
        this.mNameTxt.setTextSize(0, (float) ResourceUtil.getDimensionPixelSize(R.dimen.dimen_20dp));
        setFocusable(true);
        setDescendantFocusability(393216);
    }

    public void setIconDrawable(int resId) {
        this.mIconImv.setImageResource(resId);
    }

    public void setText(String str) {
        this.mNameTxt.setText(str);
    }

    public void setTextColor(int color) {
        if (this.mNameTxt != null) {
            this.mNameTxt.setTextColor(color);
        }
    }

    public String getText() {
        return String.valueOf(this.mNameTxt.getText());
    }

    public void setItemType(ItemDataType itemDataType) {
        this.mItemDataType = itemDataType;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
    }
}
