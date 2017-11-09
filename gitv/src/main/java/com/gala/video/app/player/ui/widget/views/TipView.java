package com.gala.video.app.player.ui.widget.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.TipType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.pingback.Sender.TipPingbackSender;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.overlay.AbsMediaControllerStrategy.ITipStateListener;
import com.gala.video.app.player.ui.overlay.OnAdStateListener;
import com.gala.video.app.player.ui.overlay.TipWrapper;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.app.player.utils.PlayerTipAccountHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class TipView extends FrameLayout implements IThreeDimensional, ThreeDimensionalParams, IScreenUISwitcher {
    private static final int ANIM_DURATION = 400;
    private static final int ANIM_DURATION_200 = 200;
    private static final String TAG = "Player/Ui/TipView";
    private RelativeLayout mAdView = null;
    private AnimationListener mAnimationListener = new AnimationListener() {
        public void onAnimationEnd(Animation animation) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TipView.TAG, "hide() onAnimationEnd");
            }
            TipView.this.hideHalo();
            TipView.this.hideTxtTip();
            TipView.this.mIsAnimProcessing = false;
            if (LogUtils.mIsDebug) {
                LogUtils.d(TipView.TAG, "onAnimationEnd mIsAnimProcessing=" + TipView.this.mIsAnimProcessing);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            TipView.this.mIsAnimProcessing = true;
            if (LogUtils.mIsDebug) {
                LogUtils.d(TipView.TAG, "onAnimationStart mIsAnimProcessing=" + TipView.this.mIsAnimProcessing);
            }
        }
    };
    private ImageView mBackIcon;
    private LinearLayout mBackPanel;
    private Runnable mClickRunnable;
    private Context mContext;
    private boolean mFirstTip = false;
    private ImageView mFrontIcon;
    private LinearLayout mFrontPanel;
    private GuideTip mGuideTip;
    private ImageView mHalo;
    private Handler mHandler = new Handler();
    private PlayerTipAccountHelper mHelper;
    private ImageView mImageIndication;
    private boolean mIs3D;
    private boolean mIsAnimProcessing = false;
    private boolean mIsFullScreen;
    private boolean mIsPrimary;
    private ITipStateListener mListener;
    private TipWrapper mOldTip;
    private OnAdStateListener mOnAdStateListener;
    private IPingbackContext mPingbackContext;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            ViewGroup showView;
            ViewGroup goneView;
            if (!TipView.this.mFirstTip && TipView.this.mSecondTip) {
                showView = TipView.this.mBackPanel;
                goneView = TipView.this.mFrontPanel;
            } else if (TipView.this.mFirstTip && !TipView.this.mSecondTip) {
                showView = TipView.this.mFrontPanel;
                goneView = TipView.this.mBackPanel;
            } else {
                return;
            }
            ObjectAnimator anim = ObjectAnimator.ofFloat(goneView, "rotationX", new float[]{0.0f, 90.0f});
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(400).start();
            anim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    goneView.setVisibility(4);
                    int count = goneView.getChildCount();
                    for (int i = 0; i < count; i++) {
                        View view = goneView.getChildAt(i);
                        if (view != null) {
                            view.setVisibility(8);
                        }
                    }
                    showView.setVisibility(0);
                    ObjectAnimator animEnd = ObjectAnimator.ofFloat(showView, "rotationX", new float[]{-90.0f, 0.0f});
                    animEnd.setInterpolator(new LinearInterpolator());
                    animEnd.setDuration(400).start();
                }
            });
        }
    };
    private boolean mSecondTip = false;
    private TipPingbackSender mSender;
    private float mTextSizeFullScreen;
    private float mTextSizeWindow;
    private TextView mTextTipBack;
    private TextView mTextTipFront;
    private TipWrapper mTip = null;
    private FrameLayout mTipContent;
    private Animation mTranslateAnimation;
    private IVideo mVideo;

    public TipView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.mPingbackContext = (IPingbackContext) this.mContext;
        this.mSender = new TipPingbackSender();
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.player_layout_tipmessage, this);
        this.mTipContent = (FrameLayout) findViewById(R.id.fl_tipcontent);
        this.mFrontPanel = (LinearLayout) findViewById(R.id.front_panel);
        this.mFrontIcon = (ImageView) findViewById(R.id.front_icon);
        this.mTextTipFront = (TextView) findViewById(R.id.tv_tipmessage_front);
        this.mBackPanel = (LinearLayout) findViewById(R.id.back_panel);
        this.mBackIcon = (ImageView) findViewById(R.id.back_icon);
        this.mTextTipBack = (TextView) findViewById(R.id.tv_tipmessage_back);
        this.mImageIndication = (ImageView) findViewById(R.id.tip_indication);
        this.mHalo = (ImageView) findViewById(R.id.tip_halo);
        this.mTextSizeFullScreen = (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_27dp);
        this.mTextTipFront.setTextSize(0, this.mTextSizeFullScreen);
        this.mTextTipFront.setTextScaleX(1.0f);
        this.mTextTipFront.setShadowLayer(8.0f, 0.0f, 3.0f, 1711276032);
        this.mTextTipBack.setTextSize(0, this.mTextSizeFullScreen);
        this.mTextTipBack.setTextScaleX(1.0f);
        this.mTextTipBack.setShadowLayer(8.0f, 0.0f, 3.0f, 1711276032);
        setVisibility(8);
        initTranAnim();
        this.mGuideTip = new GuideTip(this.mContext);
        this.mGuideTip.init(this);
        this.mHelper = new PlayerTipAccountHelper();
    }

    private void initTranAnim() {
        this.mTranslateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 100.0f);
        this.mTranslateAnimation.setDuration(200);
        this.mTranslateAnimation.setAnimationListener(this.mAnimationListener);
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showTip tip(" + tip + ")");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showTip mFirstTip=" + this.mFirstTip + ", mSecondTip=" + this.mSecondTip);
        }
        if (tip != null) {
            this.mClickRunnable = null;
            this.mTip = tip;
            switch (tip.getTipType().getConcreteTipType()) {
                case 308:
                    if (!PlayerAppConfig.isSelectionPanelShown() && NetworkUtils.isNetworkAvaliable()) {
                        hideTxtTip();
                        showGuide();
                        return;
                    }
                    return;
                default:
                    if (this.mGuideTip.isShow()) {
                        hideGuide();
                    }
                    showTxtTip(tip);
                    if (tip.getAdView() != null) {
                        showAd(tip.getAdView());
                    } else {
                        hideAd();
                    }
                    if (this.mOldTip != null) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(TAG, "showTip olddrawable:" + this.mOldTip.getDrawable() + ", drawable:" + this.mTip.getDrawable());
                        }
                        if (this.mOldTip.getDrawable() != null && this.mTip.getDrawable() == null) {
                            fadeOutAnimation();
                        } else if (this.mOldTip.getDrawable() == null && this.mTip.getDrawable() != null) {
                            setHalo(tip);
                            fadeInAnimation();
                        }
                    } else if (this.mTip.getDrawable() != null) {
                        setHalo(tip);
                        showHalo();
                    } else {
                        hideHalo();
                    }
                    this.mOldTip = tip;
                    return;
            }
        }
    }

    private void showTxtTip(TipWrapper tip) {
        if (!StringUtils.isEmpty(tip.getContent())) {
            CharSequence tipContent = tip.getContent();
            Runnable runnable = tip.getRunnable();
            if (runnable != null) {
                this.mClickRunnable = runnable;
            }
            if (!this.mFirstTip && !this.mSecondTip) {
                hideTxtTip();
                this.mFirstTip = true;
                this.mIsAnimProcessing = false;
                showTipPanel();
                showFrontTip(tip);
            } else if (this.mFirstTip && !this.mSecondTip) {
                this.mFirstTip = false;
                this.mSecondTip = true;
                showBackTip(tip);
                this.mHandler.post(this.mRunnable);
            } else if (!this.mFirstTip && this.mSecondTip) {
                this.mFirstTip = true;
                this.mSecondTip = false;
                showFrontTip(tip);
                this.mHandler.post(this.mRunnable);
            }
        }
    }

    private void fadeInAnimation() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fadeInAnimation()");
        }
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(400);
        this.mImageIndication.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                TipView.this.showHalo();
            }
        });
    }

    private void fadeOutAnimation() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fadeOutAnimation()");
        }
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        this.mHalo.setVisibility(8);
        animation.setDuration(400);
        this.mImageIndication.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                TipView.this.hideHalo();
            }
        });
    }

    private void hideTxtTip() {
        this.mFrontPanel.setVisibility(8);
        this.mFrontIcon.setVisibility(8);
        this.mTextTipFront.setVisibility(8);
        this.mBackPanel.setVisibility(8);
        this.mBackIcon.setVisibility(8);
        this.mTextTipBack.setVisibility(8);
        this.mTextTipFront.setText(null);
        this.mTextTipBack.setText(null);
        setVisibility(8);
    }

    public void showTipPanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showTipPanel()");
        }
        setVisibility(0);
        Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 100.1f, 0.0f);
        translateAnimation.setDuration(200);
        startAnimation(translateAnimation);
        translateAnimation.start();
    }

    private void showFrontTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showFrontTip: " + tip.getContent());
        }
        if (tip.getTipType().getTipShowType() == 203) {
            this.mFrontIcon.setVisibility(0);
        }
        this.mFrontPanel.setVisibility(0);
        this.mTextTipFront.setVisibility(0);
        this.mTextTipFront.setText(tip.getContent());
        sendTipShowPingback();
        saveTxtTipCount(tip);
    }

    private void showBackTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showBackTip: " + tip.getContent());
        }
        if (tip.getTipType().getTipShowType() == 203) {
            this.mBackIcon.setVisibility(0);
        }
        this.mTextTipBack.setText(tip.getContent());
        this.mTextTipBack.setVisibility(0);
        sendTipShowPingback();
        saveTxtTipCount(tip);
    }

    private void setHalo(TipWrapper tip) {
        if (tip.getDrawable() != null) {
            int width = tip.getWidth();
            int height = tip.getHeight();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setHalo mIs3D=" + this.mIs3D + "width=" + width + ", height=" + height);
            }
            if (this.mIs3D) {
                LayoutParams haloparams = (LayoutParams) this.mHalo.getLayoutParams();
                haloparams.width = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_300dp);
                this.mHalo.setLayoutParams(haloparams);
                width /= 2;
            }
            LayoutParams params = (LayoutParams) this.mImageIndication.getLayoutParams();
            params.width = width;
            params.height = height;
            this.mImageIndication.setLayoutParams(params);
            this.mImageIndication.setImageDrawable(tip.getDrawable());
            this.mImageIndication.setVisibility(0);
            this.mHalo.setVisibility(0);
        }
    }

    public void setThreeDimensional(boolean enable) {
        if (!(this.mFrontIcon == null || this.mBackIcon == null)) {
            LinearLayout.LayoutParams frontParams = (LinearLayout.LayoutParams) this.mFrontIcon.getLayoutParams();
            LinearLayout.LayoutParams backParams = (LinearLayout.LayoutParams) this.mBackIcon.getLayoutParams();
            if (enable) {
                frontParams.width = (int) (this.mContext.getResources().getDimension(R.dimen.dimen_35dp) * ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
                backParams.width = (int) (this.mContext.getResources().getDimension(R.dimen.dimen_35dp) * ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
            } else {
                frontParams.width = (int) this.mContext.getResources().getDimension(R.dimen.dimen_35dp);
                backParams.width = (int) this.mContext.getResources().getDimension(R.dimen.dimen_35dp);
            }
            this.mFrontIcon.setLayoutParams(frontParams);
            this.mBackIcon.setLayoutParams(backParams);
        }
        if (enable) {
            this.mIs3D = true;
            this.mTextTipFront.setTextScaleX(ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
            this.mTextTipBack.setTextScaleX(ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
            return;
        }
        this.mTextTipFront.setTextScaleX(1.0f);
        this.mTextTipBack.setTextScaleX(1.0f);
    }

    public void hide(boolean isNeedAnim) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hide() mFirstTip=" + this.mFirstTip + ", mSecondTip=" + this.mSecondTip + ", isShown=" + getVisibility() + ", mIsFullScreen=" + this.mIsFullScreen);
        }
        if (getVisibility() == 8) {
            resetTipPosition();
            return;
        }
        if (isNeedAnim) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "hide mIsAnimProcessing=" + this.mIsAnimProcessing);
            }
            if (!this.mIsAnimProcessing) {
                startAnimation(this.mTranslateAnimation);
                this.mTranslateAnimation.start();
            }
        } else {
            setVisibility(8);
            hideHalo();
            hideAd();
            hideTxtTip();
            this.mIsAnimProcessing = false;
        }
        resetTipPosition();
        hideGuide();
        this.mOldTip = null;
        if (this.mHelper != null) {
            this.mHelper.clear();
        }
    }

    private void hideHalo() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideHalo()");
        }
        this.mImageIndication.setVisibility(8);
        this.mHalo.setVisibility(8);
        this.mImageIndication.setImageDrawable(null);
    }

    private void showHalo() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showHalo()");
        }
        this.mImageIndication.setVisibility(0);
        this.mHalo.setVisibility(0);
    }

    private void resetTipPosition() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "resetTipPosition() mFirstTip=" + this.mFirstTip + ", mSecondTip=" + this.mSecondTip);
        }
        if (!this.mFirstTip && this.mSecondTip) {
            ObjectAnimator animStart = ObjectAnimator.ofFloat(this.mBackPanel, "rotationX", new float[]{0.0f, 90.0f});
            animStart.setInterpolator(new LinearInterpolator());
            animStart.setDuration(0).start();
            animStart.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    ObjectAnimator animEnd = ObjectAnimator.ofFloat(TipView.this.mFrontPanel, "rotationX", new float[]{-90.0f, 0.0f});
                    animEnd.setInterpolator(new LinearInterpolator());
                    animEnd.setDuration(400).start();
                }
            });
        }
        this.mTip = null;
        this.mFirstTip = false;
        this.mSecondTip = false;
        this.mClickRunnable = null;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchKeyEvent=" + event);
        }
        int code = event.getKeyCode();
        if (code != 23 && code != 66) {
            return false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "mRunnable=" + this.mClickRunnable);
        }
        if (this.mVideo == null || this.mTip == null) {
            return false;
        }
        if (this.mTip.getTipType().isSupportLogin() && this.mClickRunnable != null) {
            if (this.mIsPrimary) {
                this.mClickRunnable.run();
                sendTipClickPingback();
            }
            hide(false);
            if (this.mListener != null) {
                this.mListener.setTipState(false);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "dispatchKeyEvent isSupportLogin");
            }
            return true;
        } else if (this.mTip.getTipType().getConcreteTipType() == 302 && this.mClickRunnable != null) {
            if (this.mIsPrimary) {
                this.mClickRunnable.run();
            }
            hide(false);
            if (this.mListener != null) {
                this.mListener.setTipState(false);
            }
            return true;
        } else if ((this.mVideo.getCurrentBitStream() == null || this.mVideo.getCurrentBitStream().getBenefitType() != 2) && !this.mVideo.isPreview()) {
            if (this.mVideo.getProvider().getSourceType() != SourceType.LIVE || !this.mVideo.getProvider().getLiveVideo().isLiveVipShowTrailer() || this.mClickRunnable == null) {
                return false;
            }
            this.mClickRunnable.run();
            return true;
        } else if (getVisibility() != 0) {
            if (!LogUtils.mIsDebug) {
                return false;
            }
            LogUtils.d(TAG, "dispatchKeyEvent isVisible:" + getVisibility());
            return false;
        } else if (this.mVideo.getSourceType() == SourceType.PUSH) {
            return false;
        } else {
            if (this.mClickRunnable != null) {
                this.mClickRunnable.run();
            }
            return true;
        }
    }

    private void updateWindowSize(float zoomRatio) {
        if (0.0f == this.mTextSizeWindow) {
            this.mTextSizeWindow = this.mTextSizeFullScreen * zoomRatio;
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        updateWindowSize(zoomRatio);
        this.mIsFullScreen = isFullScreen;
        if (isFullScreen) {
            this.mTextTipFront.setTextSize(0, this.mTextSizeFullScreen);
            this.mTextTipBack.setTextSize(0, this.mTextSizeFullScreen);
            return;
        }
        this.mTextTipFront.setTextSize(0, this.mTextSizeWindow);
        this.mTextTipBack.setTextSize(0, this.mTextSizeWindow);
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setVideo=" + video);
        }
        this.mVideo = video;
    }

    public void setTipStateListener(ITipStateListener listener) {
        this.mListener = listener;
    }

    public void clearAd() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clearAd()");
        }
        if (this.mAdView != null) {
            removeView(this.mAdView);
            this.mAdView = null;
        }
    }

    private void showAd(RelativeLayout view) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showAd()");
        }
        if (!this.mIs3D) {
            if (this.mAdView == null) {
                this.mAdView = view;
                LayoutParams params = new LayoutParams(this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_180dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_60dp));
                params.gravity = 80;
                params.bottomMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_64dp);
                params.leftMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_23dp);
                addView(this.mAdView, params);
            }
            notifyAdShow(this.mTip.getAdID());
            this.mAdView.setVisibility(0);
            if (this.mSender != null) {
                this.mSender.sendAdPingback(this.mPingbackContext);
            }
        }
    }

    private void hideAd() {
        if (this.mAdView != null) {
            this.mAdView.setVisibility(8);
        }
    }

    private void notifyAdShow(int id) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyAdShow()" + this.mOnAdStateListener);
        }
        if (this.mOnAdStateListener != null) {
            this.mOnAdStateListener.onShow(101, Integer.valueOf(id));
        }
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnPageAdvertiseStateChangeListener()" + listener);
        }
        this.mOnAdStateListener = listener;
    }

    private void sendTipShowPingback() {
        if (this.mIsPrimary && this.mSender != null && this.mPingbackContext != null && this.mVideo != null && this.mTip != null) {
            int type = this.mTip.getTipType().getConcreteTipType();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "sendTipShowPingback() type:" + type);
            }
            switch (type) {
                case TipType.CONCRETE_TYPE_LOGIN /*325*/:
                    this.mSender.sendLoginTipShow(this.mPingbackContext, this.mVideo);
                    return;
                default:
                    return;
            }
        }
    }

    private void sendTipClickPingback() {
        if (this.mIsPrimary && this.mSender != null && this.mPingbackContext != null && this.mVideo != null && this.mTip != null) {
            int type = this.mTip.getTipType().getConcreteTipType();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "sendTipClickPingback() type:" + type);
            }
            switch (type) {
                case TipType.CONCRETE_TYPE_LOGIN /*325*/:
                    this.mSender.sendLoginTipClick(this.mPingbackContext, this.mVideo);
                    return;
                default:
                    return;
            }
        }
    }

    private void showGuide() {
        if (this.mGuideTip != null) {
            this.mGuideTip.show();
            setVisibility(0);
            saveCount(TipType.SELECTION_S);
        }
    }

    private void saveCount(String name) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "saveCount() mIsPrimary:" + this.mIsPrimary);
        }
        if (this.mIsPrimary) {
            int[] iarray = this.mHelper.getCount(this.mContext, name);
            if (iarray != null && iarray[0] >= 0 && iarray[1] >= 0) {
                this.mHelper.saveCount(this.mContext, name, iarray[0] + 1, iarray[1] + 1);
            }
        }
    }

    public void hideGuide() {
        if (this.mGuideTip != null && this.mGuideTip.isShow()) {
            this.mGuideTip.hide();
            setVisibility(8);
        }
    }

    private void saveTxtTipCount(TipWrapper tip) {
        switch (tip.getTipType().getConcreteTipType()) {
            case 320:
                saveCount(TipType.SKIP_AD_S);
                return;
            case TipType.CONCRETE_TYPE_LOGIN /*325*/:
                saveCount("login");
                return;
            default:
                return;
        }
    }

    public void setPrimary(boolean b) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setPrimary():" + b);
        }
        this.mIsPrimary = b;
    }
}
