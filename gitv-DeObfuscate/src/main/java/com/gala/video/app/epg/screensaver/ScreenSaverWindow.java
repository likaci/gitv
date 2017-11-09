package com.gala.video.app.epg.screensaver;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.screensaver.ScreenSaverAdAnimation.AdCallback;
import com.gala.video.app.epg.screensaver.ScreenSaverImageAnimation.ScreenSaverPreEndCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterAd;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterImage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverBeforeFadeIn;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

class ScreenSaverWindow extends Dialog {
    private static final String TAG = "ScreenSaverWindow";
    private static ScreenSaverAdAnimation mAdAnimation = new ScreenSaverAdAnimation();
    private static ScreenSaverImageAnimation mImgAnimation = new ScreenSaverImageAnimation();
    private static ScreenSaverStatusDispatcher mStatusDispatcher = new ScreenSaverStatusDispatcher();
    private TextView mAdLabelTxv;
    private View mContainerView;
    private OnKeyListener mOnKeyListener;
    private TextView mQrDescTxv;
    private ImageView mQrImv;
    private View mQrLayout;
    private TextView mQrTitleTxv;
    private ImageView mScreenSaverAdImv;
    private ImageView mScreenSaverView;
    private TextView mTvClick;

    public interface OnKeyListener {
        boolean onKeyEvent(KeyEvent keyEvent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(-1, -1);
    }

    public ScreenSaverWindow(Context context) {
        super(context, C0508R.style.alert_full_screen_dialog);
        init(context);
    }

    private void init(Context context) {
        this.mContainerView = LayoutInflater.from(context).inflate(C0508R.layout.share_screen_saver_ad, null);
        setContentView(this.mContainerView);
        this.mScreenSaverAdImv = (ImageView) findViewById(C0508R.id.share_screen_saver_ad_imv_image);
        this.mAdLabelTxv = (TextView) findViewById(C0508R.id.share_screen_saver_ad_txv_ad);
        this.mQrLayout = findViewById(C0508R.id.share_screen_saver_ad_layout_qr);
        this.mQrImv = (ImageView) findViewById(C0508R.id.share_screen_saver_ad_imv_qr);
        this.mQrTitleTxv = (TextView) findViewById(C0508R.id.share_screen_saver_ad_txv_qr_title);
        this.mQrDescTxv = (TextView) findViewById(C0508R.id.share_screen_saver_ad_txv_qr_desc);
        this.mTvClick = (TextView) findViewById(C0508R.id.share_screen_saver_click);
        initClickTextView();
        this.mScreenSaverView = (ImageView) findViewById(C0508R.id.share_screen_saver_ad_imv_image);
    }

    void setCurrentWindowStyle(int currentWindowStyle) {
        LogUtils.m1568d(TAG, "setCurrentWindowStyle:" + currentWindowStyle);
        switch (currentWindowStyle) {
            case 0:
                this.mTvClick.setVisibility(4);
                return;
            case 1:
                this.mQrLayout.setVisibility(4);
                this.mAdLabelTxv.setVisibility(4);
                this.mTvClick.setVisibility(4);
                return;
            default:
                LogUtils.m1568d(TAG, "setCurrentWindowStyle: windowStyle is error, currentWindowStyle = " + currentWindowStyle);
                return;
        }
    }

    private void initClickTextView() {
        String text = ResourceUtil.getStr(C0508R.string.screen_saver_click_text);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        ForegroundColorSpan white = new ForegroundColorSpan(-921103);
        ForegroundColorSpan orange = new ForegroundColorSpan(-19456);
        ForegroundColorSpan white1 = new ForegroundColorSpan(-921103);
        ssb.setSpan(white, 0, 1, 33);
        ssb.setSpan(orange, 2, 4, 33);
        ssb.setSpan(white1, 5, text.length(), 33);
        this.mTvClick.setText(ssb);
    }

    void setScreenSaverCallback(ScreenSaverPreEndCallback scrSaverEndCallback) {
        if (mImgAnimation != null) {
            mImgAnimation.setSceenSaverEndCallback(scrSaverEndCallback);
        }
    }

    void setAdPlayCallback(AdCallback adEndCallback) {
        if (mAdAnimation != null) {
            mAdAnimation.setAdEndCallback(adEndCallback);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.m1568d(TAG, "dispatchKeyEvent,keycode=" + event.getKeyCode() + ",action=" + event.getAction());
        onKeyEvent(event);
        return true;
    }

    public void show() {
        try {
            super.show();
            LogUtils.m1568d(TAG, "ScreenSaverWindow show:");
            getWindow().addFlags(128);
            mStatusDispatcher.start();
        } catch (Exception e) {
            LogUtils.m1568d(TAG, "ScreenSaverWindow  Exception:" + e.getMessage());
        }
    }

    void showAnimationAd(List<ScreenSaverAdModel> dataSource) {
        LogUtils.m1568d(TAG, "showAnimationAd:");
        if (!isShowing()) {
            show();
        }
        if (mAdAnimation != null) {
            mAdAnimation.setWidgets(this.mContainerView, this.mQrLayout, this.mScreenSaverAdImv, this.mQrImv, this.mQrTitleTxv, this.mQrDescTxv, this.mAdLabelTxv, this.mTvClick);
            mAdAnimation.setData(dataSource);
            mAdAnimation.start();
        }
    }

    void showAnimation(ScreenSaverImageProvider imageProvider) {
        LogUtils.m1568d(TAG, "showAnimation:");
        if (!isShowing()) {
            show();
        }
        if (mImgAnimation != null) {
            mImgAnimation.setWidgets(this.mContainerView, this.mScreenSaverView, this.mTvClick);
            mImgAnimation.start(imageProvider);
        }
    }

    public void dismiss() {
        super.dismiss();
        LogUtils.m1568d(TAG, "ScreenSaverWindow dismiss");
        if (mAdAnimation != null) {
            mAdAnimation.stop();
        }
        if (mImgAnimation != null) {
            mImgAnimation.stop();
        }
        mStatusDispatcher.stop();
    }

    void clearPingBackCount() {
        if (mImgAnimation != null) {
            mImgAnimation.clearShowingCount();
        }
        if (mAdAnimation != null) {
            mAdAnimation.clearShowingCount();
        }
    }

    private boolean onKeyEvent(KeyEvent event) {
        if (this.mOnKeyListener != null) {
            return this.mOnKeyListener.onKeyEvent(event);
        }
        return false;
    }

    void setOnKeyListener(OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;
    }

    boolean isShowingImage() {
        return mImgAnimation.isShowing();
    }

    boolean isShowingAd() {
        return mAdAnimation.isShowing();
    }

    static IRegisterAd getAdRegister() {
        return mAdAnimation;
    }

    static IRegisterImage getImgRegister() {
        return mImgAnimation;
    }

    static IScreenSaverStatusDispatcher getStatusDispatcher() {
        return mStatusDispatcher;
    }

    public void setScreenSaverBeforeFadeInCallBack(IScreenSaverBeforeFadeIn callBack) {
        if (mImgAnimation != null) {
            mImgAnimation.setScreenSaverBeforeFadeInCallBack(callBack);
        }
    }
}
