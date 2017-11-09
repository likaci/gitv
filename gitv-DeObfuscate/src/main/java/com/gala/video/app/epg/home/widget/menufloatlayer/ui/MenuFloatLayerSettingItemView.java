package com.gala.video.app.epg.home.widget.menufloatlayer.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.C0508R;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MenuFloatLayerSettingItemView extends CloudView {
    private static final String TAG = "menufloatlayer/ui/MenuFloatLayerSettingItemView";
    private Drawable mFocusIconDrawable;
    private Drawable mIconDrawable;
    private CuteImageView mImageView;
    private ItemDataType mItemDataType;
    private CuteImageView mLeftCornerImageView;
    private CuteTextView mLeftCornerTextView;
    private CuteTextView mTitleView;

    public MenuFloatLayerSettingItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setStyle("setting/menu/settingitem.json");
        initView();
    }

    private void initView() {
        if (this.mTitleView != null) {
            this.mTitleView.setNormalColor(ResourceUtil.getColor(C0508R.color.albumview_normal_color));
        }
        setBackgroundDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_item_rect_btn_selector));
    }

    public void setTitle(String title) {
        getTitleView();
        if (this.mTitleView != null) {
            this.mTitleView.setText(title);
        }
    }

    public String getTitle() {
        if (this.mTitleView == null) {
            return "";
        }
        return String.valueOf(this.mTitleView.getText());
    }

    public void setItemType(ItemDataType itemDataType) {
        this.mItemDataType = itemDataType;
    }

    public ItemDataType getItemType() {
        return this.mItemDataType;
    }

    private void setDrawable(Drawable drawable) {
        getImageView();
        if (this.mImageView != null) {
            this.mImageView.setDrawable(drawable);
        }
    }

    public void setFocusDrawable(Drawable drawable) {
        this.mFocusIconDrawable = drawable;
        if (hasFocus()) {
            setDrawable(drawable);
        }
    }

    public void setNormalDrawable(Drawable drawable) {
        if (!hasFocus()) {
            setDrawable(drawable);
        }
        this.mIconDrawable = drawable;
    }

    public void setTipText(String count) {
        getLTBubbleView();
        if (this.mLeftCornerTextView != null) {
            this.mLeftCornerTextView.setText(count);
        }
    }

    public void setTipView(boolean isVisible) {
        getCornerLTView();
        if (this.mLeftCornerImageView != null) {
            this.mLeftCornerImageView.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.epg_setting_item_tip_bg));
            if (isVisible) {
                this.mLeftCornerImageView.setVisible(0);
            } else {
                this.mLeftCornerImageView.setVisible(4);
            }
        }
    }

    private CuteTextView getTitleView() {
        if (this.mTitleView == null) {
            this.mTitleView = getTextView("ID_TITLE");
        }
        return this.mTitleView;
    }

    private CuteImageView getImageView() {
        if (this.mImageView == null) {
            this.mImageView = getImageView("ID_IMAGE");
        }
        return this.mImageView;
    }

    public CuteImageView getCornerLTView() {
        if (this.mLeftCornerImageView == null) {
            this.mLeftCornerImageView = getImageView("ID_CORNER_L_T");
        }
        return this.mLeftCornerImageView;
    }

    public CuteTextView getLTBubbleView() {
        if (this.mLeftCornerTextView == null) {
            this.mLeftCornerTextView = getTextView("ID_LT_BUBBLE");
        }
        return this.mLeftCornerTextView;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }
}
