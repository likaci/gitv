package com.gala.video.app.epg.ui.star.widget;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class StarsTopView {
    public static final int TOP_HEIGHT = ResourceUtil.getDimen(R.dimen.dimen_74dp);
    private AlbumInfoModel mAlbumInfoModel;
    private boolean mIsTopShown = false;
    private View mRootView;
    private TextView mTitleText;
    private View mTopView;

    public StarsTopView(View root, AlbumInfoModel model) {
        this.mRootView = root;
        this.mAlbumInfoModel = model;
    }

    @TargetApi(16)
    private void initViews() {
        ViewStub stub = (ViewStub) this.mRootView.findViewById(R.id.epg_stars_top_id);
        if (Integer.valueOf(VERSION.SDK).intValue() >= 16) {
            stub.setLayoutInflater(LayoutInflater.from(this.mRootView.getContext()));
        }
        this.mTopView = stub.inflate();
        this.mTitleText = (TextView) this.mTopView.findViewById(R.id.epg_stars_top_title_text);
        setTitleText();
        setBackgroud();
    }

    private void setBackgroud() {
        this.mTopView.setBackgroundDrawable(createBackGroundDrawable(Project.getInstance().getControl().getBackgroundDrawable()));
    }

    private static Drawable createBackGroundDrawable(Drawable drawable) {
        int bitmapWidth = ResourceUtil.getScreenWidth();
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, TOP_HEIGHT, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, bitmapWidth, ResourceUtil.getScreenHeight());
        drawable.draw(canvas);
        return new BitmapDrawable(ResourceUtil.getResource(), bitmap);
    }

    private void setTitleText() {
        if (this.mTitleText != null && this.mAlbumInfoModel != null) {
            this.mTitleText.setText(this.mAlbumInfoModel.getSearchModel().getKeyWord());
        }
    }

    private void show() {
        if (!this.mIsTopShown) {
            if (this.mTopView == null) {
                initViews();
            }
            this.mTopView.setVisibility(0);
            this.mIsTopShown = true;
        }
    }

    private void showView() {
        if (!isShown()) {
            show();
        }
    }

    private void hideView() {
        if (isShown()) {
            hide();
        }
    }

    private void hide() {
        if (this.mIsTopShown) {
            this.mTopView.setVisibility(8);
            this.mIsTopShown = false;
        }
    }

    private boolean isShown() {
        return this.mIsTopShown;
    }

    public void showTopView(boolean isShow) {
        if (isShow) {
            showView();
        } else {
            hideView();
        }
    }
}
