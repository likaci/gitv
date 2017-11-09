package com.gala.video.app.player.ui.overlay.panels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class TopTitlePanel {
    private static final String TAG = "TopTitlePanel";
    private Context mContext;
    private boolean mIsPanelShown = false;
    private View mRootView;
    private TextView mTitleText;
    private View mTopTitleView;

    public TopTitlePanel(View root) {
        this.mContext = root.getContext();
        this.mRootView = root;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> initViews");
        }
        this.mTopTitleView = ((ViewStub) this.mRootView.findViewById(R.id.stub_detail_top_title)).inflate();
        this.mTopTitleView.findViewById(R.id.detail_top_view).setBackgroundColor(this.mContext.getResources().getColor(R.color.detail_top_title_background_color));
        this.mTitleText = (TextView) this.mTopTitleView.findViewById(R.id.detail_top_title_text);
        this.mTitleText.setTextColor(this.mContext.getResources().getColor(R.color.detail_top_title_text_color));
        setBackgroud();
    }

    private void setBackgroud() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setBackgroud");
        }
        AppClientUtils.setBackgroundDrawable(this.mTopTitleView, createBackGroundDrawable(Project.getInstance().getControl().getBackgroundDrawable()));
    }

    private Drawable createBackGroundDrawable(Drawable drawable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> createBackGroundDrawable, drawable=" + drawable);
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createBackGroundDrawable, drawable h=" + h + ", w=" + w);
        }
        int[] deviceSize = DeviceUtils.getDeviceSize(this.mContext);
        int deviceWidth = deviceSize[0];
        int deviceHeight = deviceSize[1];
        int bitmapHeight = ResourceUtil.getDimen(R.dimen.dimen_74dp);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createBackGroundDrawable, bitmap bitmapHeight=" + bitmapHeight + ", deviceWidth=" + deviceWidth);
        }
        Bitmap bitmap = Bitmap.createBitmap(deviceWidth, bitmapHeight, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, deviceWidth, deviceHeight);
        drawable.draw(canvas);
        return new BitmapDrawable(this.mContext.getResources(), bitmap);
    }

    public void show(String title) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> show");
        }
        if (!this.mIsPanelShown) {
            if (this.mTopTitleView == null) {
                initViews();
            }
            setTitleText(title);
            this.mTopTitleView.setVisibility(0);
            this.mIsPanelShown = true;
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "show, panel is shown.");
        }
    }

    private void setTitleText(String title) {
        if (!TextUtils.equals(title, this.mTitleText.getText())) {
            this.mTitleText.setText(title);
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> hide");
        }
        if (this.mIsPanelShown) {
            this.mTopTitleView.setVisibility(8);
            this.mIsPanelShown = false;
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hide, panel is hidden.");
        }
    }

    public boolean isShown() {
        return this.mIsPanelShown;
    }

    public String toString() {
        return "TopTitlePanel{mIsPanelShown=" + this.mIsPanelShown + ", mTitleText=" + this.mTitleText + '}';
    }
}
