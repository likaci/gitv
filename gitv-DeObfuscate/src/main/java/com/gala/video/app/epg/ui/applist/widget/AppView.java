package com.gala.video.app.epg.ui.applist.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.C0508R;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class AppView extends CloudView {
    private Context mContext;
    private CuteImageView mImageView;
    private CuteImageView mRBImageView;
    private CuteTextView mTitleView;

    public AppView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setStyle("applist/appview.json");
        initView();
    }

    private void initView() {
        getTitleView();
        if (this.mTitleView != null) {
            this.mTitleView.setNormalColor(ResourceUtil.getColor(C0508R.color.album_des_text_color));
        }
        setBackgroundDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_item_rect_btn_selector));
    }

    public void setTitle(String title) {
        getTitleView();
        if (this.mTitleView != null) {
            this.mTitleView.setText(title);
        }
    }

    public void setDrawable(Drawable drawable) {
        getImageView();
        if (this.mImageView != null) {
            this.mImageView.setDrawable(drawable);
        }
    }

    public void setRBDrawable(Drawable drawable) {
        getRBImageView();
        if (this.mRBImageView != null) {
            this.mRBImageView.setDrawable(drawable);
        }
    }

    public void rBDrawableShow() {
        getRBImageView();
        if (this.mRBImageView != null) {
            this.mRBImageView.setVisible(0);
        }
    }

    public void rBDrawableHint() {
        getRBImageView();
        if (this.mRBImageView != null) {
            this.mRBImageView.setVisible(8);
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

    private CuteImageView getRBImageView() {
        if (this.mRBImageView == null) {
            this.mRBImageView = getImageView("ID_CORNER_R_B");
        }
        return this.mRBImageView;
    }
}
