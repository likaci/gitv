package com.gala.video.app.epg.screensaver;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.screensaver.utils.BitmapUtil;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IAd;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterAd;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

class ScreenSaverAdAnimation extends FadeAnim implements IRegisterAd {
    private static final String TAG = "ScreenSaverAnimationAd";
    private View mAdContainerView;
    private TextView mAdLabelTxv;
    private List<IAd> mAnimListener = new ArrayList();
    private AdCallback mCallback;
    private boolean mIsShowing = false;
    private TextView mQrDescTxv;
    private ImageView mQrImv;
    private View mQrLayout;
    private TextView mQrTitleTxv;
    private ImageView mScreenSaverAdImv;
    private List<ScreenSaverAdModel> mScreenSaverAdModelList;
    private TextView mTvClick;

    public interface AdCallback {
        void onAdEnd();

        void onAdPlay(int i, ScreenSaverAdModel screenSaverAdModel, boolean z);
    }

    ScreenSaverAdAnimation() {
    }

    public void setData(List<ScreenSaverAdModel> dataSource) {
        this.mScreenSaverAdModelList = dataSource;
    }

    public void setWidgets(View containerView, View qrLayout, ImageView view, ImageView qrImv, TextView qrTitleTxv, TextView qrDescTxv, TextView adLabelTxv, TextView tvClick) {
        this.mAdContainerView = containerView;
        this.mQrLayout = qrLayout;
        this.mScreenSaverAdImv = view;
        this.mQrImv = qrImv;
        this.mQrTitleTxv = qrTitleTxv;
        this.mQrDescTxv = qrDescTxv;
        this.mAdLabelTxv = adLabelTxv;
        this.mTvClick = tvClick;
    }

    public void register(IAd listener) {
        this.mAnimListener.add(listener);
    }

    public void unregister(IAd listener) {
        this.mAnimListener.remove(listener);
    }

    public void setAdEndCallback(AdCallback AdCallback) {
        this.mCallback = AdCallback;
    }

    protected View getAnimView() {
        return this.mAdContainerView;
    }

    protected boolean getNext(int index) {
        if (index >= this.mScreenSaverAdModelList.size()) {
            return false;
        }
        ScreenSaverAdModel model = (ScreenSaverAdModel) this.mScreenSaverAdModelList.get(index);
        LogUtils.m1568d(TAG, "getNext, currentIndex = " + index);
        Bitmap bitmap = new BitmapUtil().getBitmapFromPath(model.getImageLocalPath());
        if (bitmap == null) {
            LogUtils.m1577w(TAG, "getNext, the bitmap of trying to generate is null");
            return false;
        }
        this.mScreenSaverAdImv.setImageBitmap(bitmap);
        return true;
    }

    protected boolean isLoopLast(int showingCount) {
        return showingCount >= this.mScreenSaverAdModelList.size();
    }

    protected boolean isEndWithoutPlay(int currentIndex) {
        return currentIndex >= this.mScreenSaverAdModelList.size() + -1;
    }

    protected void onEndWithoutPlay() {
        clearShowingCount();
        this.mScreenSaverAdImv.setImageBitmap(null);
        if (this.mCallback != null) {
            this.mIsShowing = false;
            this.mCallback.onAdEnd();
        }
    }

    protected void onEndWithPlay() {
        clearShowingCount();
        this.mScreenSaverAdImv.setImageBitmap(null);
        if (this.mCallback != null) {
            this.mIsShowing = false;
            this.mCallback.onAdEnd();
        }
    }

    protected int getShowingDuration(int currentIndex) {
        return ((((ScreenSaverAdModel) this.mScreenSaverAdModelList.get(currentIndex)).getAdDuration() - 2000) - 2000) + 2000;
    }

    protected void beforeFadeIn(int currentIndex) {
        boolean isShowQr;
        ScreenSaverAdModel model = (ScreenSaverAdModel) this.mScreenSaverAdModelList.get(currentIndex);
        if (model != null) {
            for (IAd listener : this.mAnimListener) {
                listener.beforeFadeIn(model);
            }
        }
        if (model.isNeedAdBadge()) {
            this.mAdLabelTxv.setVisibility(0);
        } else {
            this.mAdLabelTxv.setVisibility(8);
        }
        LogUtils.m1568d(TAG, "show screen saver animation info = " + model);
        if (!model.shouldShowQr()) {
            this.mQrLayout.setVisibility(4);
            checkJumpTipsShow(model);
            isShowQr = false;
        } else if (model.getQrBitmap() != null) {
            this.mTvClick.setVisibility(8);
            this.mQrLayout.setVisibility(0);
            if (ScreenSaverPingBack.SEAT_KEY_LEFT.equals(model.getQrPosition())) {
                ((LayoutParams) this.mQrLayout.getLayoutParams()).rightMargin = ResourceUtil.getDimensionPixelSize(C1632R.dimen.dimen_1110dp);
            } else {
                ((LayoutParams) this.mQrLayout.getLayoutParams()).rightMargin = ResourceUtil.getDimensionPixelSize(C1632R.dimen.dimen_30dp);
            }
            this.mQrTitleTxv.setText(model.getQrTitle());
            this.mQrDescTxv.setText(model.getQrPosition());
            this.mQrImv.setImageBitmap(model.getQrBitmap());
            isShowQr = true;
        } else {
            LogUtils.m1571e(TAG, "create QR Bitmap failed");
            this.mQrLayout.setVisibility(4);
            checkJumpTipsShow(model);
            isShowQr = false;
        }
        this.mCallback.onAdPlay(currentIndex, model, isShowQr);
    }

    private void checkJumpTipsShow(ScreenSaverAdModel model) {
        if (model == null) {
            this.mTvClick.setVisibility(8);
        } else if (model.isEnableJumping()) {
            this.mTvClick.setVisibility(0);
        } else {
            this.mTvClick.setVisibility(8);
        }
    }

    public void start() {
        this.mIsShowing = true;
        this.mHandler.postDelayed(this.mFadeInRunnable, 1);
    }

    public void stop() {
        this.mHandler.removeCallbacks(this.mFadeInRunnable);
        this.mHandler.removeCallbacks(this.mFadeOutRunnable);
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }
}
