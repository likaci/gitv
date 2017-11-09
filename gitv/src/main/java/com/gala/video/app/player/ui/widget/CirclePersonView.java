package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.gala.video.app.player.R;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class CirclePersonView extends CloudView {
    private static final int CIRCLE_SIZE = ResourceUtil.getDimen(R.dimen.dimen_153dp);
    private static final String TAG = "CirclePersonView";
    private CuteImageView mBGImage;
    private CuteImageView mMainImage;
    private CuteTextView mTitleText;

    public CirclePersonView(Context context) {
        super(context);
        initView();
        initListener();
    }

    public CirclePersonView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        initView();
        initListener();
    }

    public CirclePersonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListener();
    }

    private void initView() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> initView");
        }
        setStyle("home/circleitem.json");
        this.mMainImage = getImageView("ID_IMAGE");
        this.mBGImage = getImageView("ID_CORNER_L_T");
        this.mTitleText = getTextView("ID_TITLE");
        this.mMainImage.setWidth(CIRCLE_SIZE);
        this.mMainImage.setHeight(CIRCLE_SIZE);
        this.mTitleText.setNormalColor(ResourceUtil.getColor(R.color.albumview_normal_color));
        this.mTitleText.setFocusColor(ResourceUtil.getColor(R.color.item_normal_focus_color));
    }

    private void initListener() {
        final OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
                CirclePersonView.this.setBGImage(hasFocus);
            }
        });
    }

    private void setBGImage(boolean hasFocus) {
        if (this.mBGImage == null) {
            return;
        }
        if (hasFocus) {
            this.mBGImage.setDrawable(ImageCacheUtil.CIRCLE_BG_FOCUS_DRAWABLE);
        } else {
            this.mBGImage.setDrawable(null);
        }
    }

    public void setDefaultImage() {
        if (this.mMainImage != null) {
            this.mMainImage.setDrawable(ImageCacheUtil.DEFAULT_CIRCLE_DRAWABLE);
        }
    }

    public void setMainImage(Bitmap bitmap) {
        if (this.mMainImage != null) {
            this.mMainImage.setBitmap(bitmap);
        }
    }

    public void setTitleText(String text) {
        if (this.mTitleText != null) {
            this.mTitleText.setText(text);
        }
    }

    public void clearViewContent() {
        setTitleText(null);
        setDefaultImage();
    }
}
