package com.gala.video.app.epg.home.ads.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.ads.model.ExitAppAdModel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.utils.AnimationUtil;

public class ExitAppAdPresenter {
    private static final String TAG = "ads/ExitAppAdPresenter";
    private OnClickListener mAdImageClickListener = new C05731();
    private OnFocusChangeListener mAdImageFocusChangeListener = new C05742();
    private TextView mAdLabelTxv;
    private View mContentLayout;
    private Context mContext;
    private ImageView mExitAppAdImv;
    private ExitAppAdModel mExitAppAdModel;
    private int mNextFocusDownViewId;
    private OnAdImageClickListener mOnAdImageClickListener;
    private TextView mQrDescTxv;
    private ImageView mQrImv;
    private View mQrLayout;
    private TextView mQrTitleTxv;

    class C05731 implements OnClickListener {
        C05731() {
        }

        public void onClick(View v) {
            ExitAppAdPresenter.this.onClickAdImage();
        }
    }

    class C05742 implements OnFocusChangeListener {
        C05742() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200);
        }
    }

    public interface OnAdImageClickListener {
        void onClick(boolean z);
    }

    public ExitAppAdPresenter(Context context) {
        this.mContext = context;
    }

    public void setWidgets(View contentLayout, View qrLayout, ImageView adImv, ImageView qrImv, TextView qrTitleTxv, TextView qrDescTxv, TextView adLabelTxv) {
        this.mContentLayout = contentLayout;
        this.mAdLabelTxv = adLabelTxv;
        this.mQrLayout = qrLayout;
        this.mExitAppAdImv = adImv;
        this.mQrImv = qrImv;
        this.mQrTitleTxv = qrTitleTxv;
        this.mQrDescTxv = qrDescTxv;
    }

    private void onClickAdImage() {
        if (this.mExitAppAdModel == null) {
            LogUtils.m1577w(TAG, "exit app ad data is empty");
            return;
        }
        HomeAdPingbackModel pingbackModel = new HomeAdPingbackModel();
        pingbackModel.setH5EnterType(16);
        pingbackModel.setH5From("ad_jump");
        pingbackModel.setH5TabSrc("其他");
        pingbackModel.setPlFrom("ad_jump");
        pingbackModel.setPlTabSrc("其他");
        pingbackModel.setVideoFrom("ad_jump");
        pingbackModel.setVideoTabSource("其他");
        pingbackModel.setVideoBuySource("");
        pingbackModel.setCarouselFrom("ad_jump");
        pingbackModel.setCarouselTabSource("其他");
        AdsClientUtils.getInstance().onAdClicked(this.mExitAppAdModel.getAdId());
        if (!this.mExitAppAdModel.isEnableJumping() || this.mExitAppAdModel.shouldShowQr()) {
            GetInterfaceTools.getIAdProcessingUtils().onClickForNotOpenAdData(this.mContext);
            if (this.mOnAdImageClickListener != null) {
                this.mOnAdImageClickListener.onClick(false);
                return;
            }
            return;
        }
        GetInterfaceTools.getIAdProcessingUtils().onClickAd(this.mContext, this.mExitAppAdModel, pingbackModel);
        if (this.mOnAdImageClickListener != null) {
            this.mOnAdImageClickListener.onClick(true);
        }
    }

    private void setAdData(ExitAppAdModel exitAppAdModel) {
        this.mExitAppAdModel = exitAppAdModel;
    }

    public void setNextFocusDownId(int id) {
        if (this.mContentLayout != null) {
            this.mContentLayout.setNextFocusDownId(id);
        }
    }

    public void show(ExitAppAdModel exitAppAdModel, Bitmap bitmap) {
        setAdData(exitAppAdModel);
        boolean hasQr = exitAppAdModel.shouldShowQr();
        Bitmap qrBitmap = exitAppAdModel.getQrBitmap();
        if (hasQr && qrBitmap != null) {
            this.mQrLayout.setVisibility(0);
            this.mQrImv.setScaleType(ScaleType.FIT_XY);
            this.mQrImv.setImageBitmap(qrBitmap);
            this.mQrImv.setBackgroundResource(C0508R.color.white);
            this.mQrTitleTxv.setText(exitAppAdModel.getmQrTitle());
            this.mQrDescTxv.setText(exitAppAdModel.getmQrDesc());
        }
        this.mAdLabelTxv.setVisibility(0);
        this.mExitAppAdImv.setScaleType(ScaleType.FIT_XY);
        this.mExitAppAdImv.setImageBitmap(bitmap);
        LogUtils.m1568d(TAG, "show, exit apk ad show");
        this.mContentLayout.setFocusable(true);
        ((ViewGroup) this.mContentLayout).setDescendantFocusability(393216);
        this.mContentLayout.setClickable(true);
        this.mContentLayout.setBackgroundResource(C0508R.drawable.share_item_rect_selector);
        this.mContentLayout.setOnClickListener(this.mAdImageClickListener);
        this.mContentLayout.setOnFocusChangeListener(this.mAdImageFocusChangeListener);
    }

    public void setOnOperateImageClickListener(OnAdImageClickListener listener) {
        this.mOnAdImageClickListener = listener;
    }
}
