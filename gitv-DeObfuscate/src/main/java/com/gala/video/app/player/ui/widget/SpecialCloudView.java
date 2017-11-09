package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.gala.video.app.player.C1291R;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SpecialCloudView extends CloudView {
    private final String TAG;
    private CuteImageView mMainImage;
    private CuteTextView mTitleText;

    public SpecialCloudView(Context context, int type) {
        super(context);
        this.TAG = "SpecialCloudView@" + hashCode() + ", type=" + type;
        init(type);
    }

    public SpecialCloudView(Context context, AttributeSet attributeSet, int defStyle, int type) {
        super(context, attributeSet, defStyle);
        this.TAG = "SpecialCloudView@" + hashCode();
        init(type);
    }

    public SpecialCloudView(Context context, AttributeSet attrs, int type) {
        super(context, attrs);
        this.TAG = "SpecialCloudView@" + hashCode();
        init(type);
    }

    private void init(int type) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> initView");
        }
        setStyle("home/allentryfunctionitem.json");
        setBackground(ImageCacheUtil.RECT_BTN_DRAWABLE);
        this.mMainImage = getImageView("ID_IMAGE");
        this.mTitleText = getTextView("ID_TITLE");
        this.mTitleText.setNormalColor(ResourceUtil.getColor(C1291R.color.albumview_normal_color));
        this.mTitleText.setMarginTop(ResourceUtil.getDimen(C1291R.dimen.dimen_30dp));
        this.mMainImage.setMarginBottom(ResourceUtil.getDimen(C1291R.dimen.dimen_14dp));
        initListener();
        setupViewContent(type);
    }

    private void initListener() {
        final OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
    }

    private void setupViewContent(int type) {
        switch (type) {
            case 2:
                this.mTitleText.setText(ResourceUtil.getStr(C1291R.string.detail_tab_content_title_more_article));
                this.mMainImage.setDrawable(ResourceUtil.getDrawable(C1291R.drawable.share_all_entry_function_icon));
                return;
            case 3:
                this.mTitleText.setText(ResourceUtil.getStr(C1291R.string.detail_exit_dialog_home_entry));
                this.mTitleText.setMarginLeft(ResourceUtil.getDimen(C1291R.dimen.dimen_1dp));
                this.mTitleText.setMarginTop(ResourceUtil.getDimen(C1291R.dimen.dimen_35dp));
                this.mMainImage.setWidth(ResourceUtil.getDimen(C1291R.dimen.dimen_86dp));
                this.mMainImage.setHeight(ResourceUtil.getDimen(C1291R.dimen.dimen_86dp));
                this.mMainImage.setDrawable(ResourceUtil.getDrawable(C1291R.drawable.player_detail_exit_dialog_logo));
                this.mTitleText.setNormalColor(ResourceUtil.getColor(C1291R.color.albumview_normal_color_for_exit_dialog));
                this.mTitleText.setFocusColor(ResourceUtil.getColor(C1291R.color.albumview_focus_color_for_exit_dialog));
                return;
            default:
                return;
        }
    }

    public String toString() {
        return this.TAG;
    }
}
