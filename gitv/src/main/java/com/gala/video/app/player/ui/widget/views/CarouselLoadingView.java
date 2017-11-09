package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.config.ILoadingViewUiConfig;
import com.gala.video.app.player.ui.config.LoadingViewAnimManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;

public class CarouselLoadingView extends RelativeLayout implements IScreenUISwitcher {
    private static final int MSG_UPDATE = 101;
    private static final String TAG = "Player/Ui/CarouselLoadingView";
    private static float mAnimHeight;
    private static float mAnimWidth;
    private boolean mAlreayPlay;
    private float mAnimHeightWindow;
    private float mAnimMarginTopFullSize;
    private float mAnimMarginTopWindowSize;
    private ImageView mAnimView = null;
    private float mAnimWidthWindow;
    private ILoadingViewUiConfig mConfig;
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            LogUtils.d(CarouselLoadingView.TAG, "handleMessage()" + msg.what);
            switch (msg.what) {
                case 101:
                    if (CarouselLoadingView.this.mConfig != null) {
                        CarouselLoadingView.this.mConfig.checkLoadingFrameAnim();
                        CarouselLoadingView.this.mConfig.checkLoadingBackground();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mHasLoadingTip;
    private int mHelpIndex;
    private String[] mHelpList;
    private Drawable mHelpTipBubble;
    private float mHelpTipSizeFull;
    private float mHelpTipSizeWindow;
    private TextView mHelpTipView;
    private float mHelpTxtMarginTopFull;
    private float mHelpTxtMarginTopWindow;
    private float mHelpTxtSizeFull;
    private float mHelpTxtSizeWindow;
    private LoadingViewAnimManager mInstacnce;
    private boolean mIsFullScreen;
    private RelativeLayout mLoadingContainer;
    private RelativeLayout mLoadingLayout;
    private ImageView mLogo;
    private float mLogoHeight;
    private float mLogoHeightWindow;
    private float mLogoMarginLeft;
    private float mLogoMarginLeftWindow;
    private float mLogoWidth;
    private float mLogoWidthWindow;
    private View mReturnLayout;
    private TextView mTxtName;
    private float mTxtNameFullSize;
    private float mTxtNameMaginLRFullSize;
    private float mTxtNameMaginLRWindowSize;
    private float mTxtNameWindowSize;

    public CarouselLoadingView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CarouselLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public CarouselLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    private void initHelpTipData() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        CharSequence loadingHelpText = model != null ? model.getCarouselLoadingInfo() : "";
        this.mHasLoadingTip = !StringUtils.isEmpty(loadingHelpText);
        if (this.mHasLoadingTip) {
            this.mHelpList = loadingHelpText.split("%n");
            this.mHelpIndex = (int) (Math.random() * ((double) this.mHelpList.length));
            LogUtils.d(TAG, "initHelpTipData == " + loadingHelpText);
        }
    }

    private void initView() {
        removeAllViews();
        LogUtils.d(TAG, "initViews");
        this.mLoadingLayout = (RelativeLayout) LayoutInflater.from(this.mContext).inflate(R.layout.player_carousel_loadingscreen, null);
        initHelpTipData();
        if (this.mHasLoadingTip) {
            this.mHelpTipView = (TextView) this.mLoadingLayout.findViewById(R.id.help_description);
            this.mHelpTipBubble = getResources().getDrawable(R.drawable.player_carousel_help);
        }
        this.mTxtName = (TextView) this.mLoadingLayout.findViewById(R.id.description);
        this.mAnimView = (ImageView) this.mLoadingLayout.findViewById(R.id.loading);
        this.mLoadingContainer = (RelativeLayout) this.mLoadingLayout.findViewById(R.id.loading_container);
        this.mLogo = (ImageView) this.mLoadingLayout.findViewById(R.id.logo);
        this.mConfig = PlayerAppConfig.getConfig4Loading();
        Bitmap logoBitmap = this.mConfig.getLoadingLogo();
        if (!(this.mLogo == null || logoBitmap == null)) {
            this.mLogo.setImageBitmap(logoBitmap);
        }
        this.mInstacnce = LoadingViewAnimManager.getInstance(this.mContext.getApplicationContext());
        AnimationDrawable anim = this.mInstacnce.getAnim();
        if (!(this.mAnimView == null || anim == null)) {
            anim.setOneShot(false);
            this.mAnimView.setImageDrawable(anim);
        }
        BitmapDrawable bgDrawable = this.mInstacnce.getBackground();
        if (!(this.mLoadingContainer == null || bgDrawable == null)) {
            this.mLoadingContainer.setBackgroundDrawable(bgDrawable);
        }
        this.mLoadingLayout.setVisibility(0);
        addView(this.mLoadingLayout, -1, -1);
        initViewSize();
        setVisibility(8);
    }

    private void initViewSize() {
        if (this.mConfig != null) {
            this.mTxtNameFullSize = (float) this.mConfig.getTxtNameSize();
            this.mTxtNameMaginLRFullSize = (float) this.mConfig.getTxtNameMarginLeftAndRight();
            this.mLogoWidth = (float) this.mConfig.getLogoWidth();
            this.mLogoHeight = (float) this.mConfig.getLogoHeight();
            this.mLogoMarginLeft = (float) this.mConfig.getLogoMarginLeft();
            mAnimHeight = (float) this.mInstacnce.getAnimHeight();
            mAnimWidth = (float) this.mInstacnce.getAnimWidth();
            this.mAnimMarginTopFullSize = (float) this.mConfig.getAnimMarginTop();
            if (this.mHasLoadingTip) {
                this.mHelpTipSizeFull = (float) this.mConfig.getHelpTipSizeFull();
                this.mHelpTxtMarginTopFull = (float) this.mConfig.getHelpTipMarginTopFull();
                this.mHelpTxtSizeFull = (float) this.mConfig.getHelpTipTxtSizeFull();
            }
        }
    }

    public void startLoadingAnim() {
        AnimationDrawable animDrawable = (AnimationDrawable) this.mAnimView.getDrawable();
        if (animDrawable != null) {
            animDrawable.start();
        }
    }

    public void showPlaying() {
        LogUtils.d(TAG, "showPlaying() mAlreayPlay=" + this.mAlreayPlay);
        if (!this.mAlreayPlay) {
            this.mHandler.sendEmptyMessageDelayed(101, 10000);
            this.mAlreayPlay = true;
        }
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showLoading()");
        }
        setVisibility(0);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideLoading()");
        }
        setVisibility(8);
        this.mHandler.removeMessages(101);
    }

    private void updateWindowSize(float zoomRatio) {
        if (0.0f == this.mTxtNameWindowSize) {
            this.mTxtNameMaginLRWindowSize = this.mTxtNameMaginLRFullSize * zoomRatio;
            this.mTxtNameWindowSize = this.mTxtNameFullSize * zoomRatio;
            this.mLogoWidthWindow = this.mLogoWidth * zoomRatio;
            this.mLogoHeightWindow = this.mLogoHeight * zoomRatio;
            this.mLogoMarginLeftWindow = this.mLogoMarginLeft * zoomRatio;
            this.mAnimHeightWindow = mAnimHeight * zoomRatio;
            this.mAnimWidthWindow = mAnimWidth * zoomRatio;
            this.mAnimMarginTopWindowSize = this.mAnimMarginTopFullSize * zoomRatio;
            if (this.mHasLoadingTip) {
                this.mHelpTipSizeWindow = this.mHelpTipSizeFull * zoomRatio;
                this.mHelpTxtSizeWindow = this.mHelpTxtSizeFull * zoomRatio;
                this.mHelpTxtMarginTopWindow = this.mHelpTxtMarginTopFull * zoomRatio;
            }
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        LogUtils.d(TAG, "switchScreen: " + isFullScreen);
        this.mIsFullScreen = isFullScreen;
        updateWindowSize(zoomRatio);
        LayoutParams params3;
        LayoutParams params4;
        RelativeLayout.LayoutParams logoParams;
        if (isFullScreen) {
            if (this.mTxtName != null) {
                this.mTxtName.setTextSize(0, this.mTxtNameFullSize);
                params3 = (LayoutParams) this.mTxtName.getLayoutParams();
                params3.leftMargin = (int) this.mTxtNameMaginLRFullSize;
                params3.rightMargin = (int) this.mTxtNameMaginLRFullSize;
                this.mTxtName.setLayoutParams(params3);
                params4 = (LayoutParams) this.mAnimView.getLayoutParams();
                if (!(mAnimHeight == 0.0f || mAnimWidth == 0.0f)) {
                    params4.height = (int) mAnimHeight;
                    params4.width = (int) mAnimWidth;
                }
                params4.topMargin = (int) this.mAnimMarginTopFullSize;
                this.mAnimView.setLayoutParams(params4);
                logoParams = (RelativeLayout.LayoutParams) this.mLogo.getLayoutParams();
                logoParams.height = (int) this.mLogoHeight;
                logoParams.width = (int) this.mLogoWidth;
                logoParams.leftMargin = (int) this.mLogoMarginLeft;
                this.mLogo.setLayoutParams(logoParams);
            }
        } else if (this.mTxtName != null) {
            this.mTxtName.setTextSize(0, this.mTxtNameWindowSize);
            params3 = (LayoutParams) this.mTxtName.getLayoutParams();
            params3.leftMargin = (int) this.mTxtNameMaginLRWindowSize;
            params3.rightMargin = (int) this.mTxtNameMaginLRWindowSize;
            this.mTxtName.setLayoutParams(params3);
            params4 = (LayoutParams) this.mAnimView.getLayoutParams();
            if (!(mAnimHeight == 0.0f || mAnimWidth == 0.0f)) {
                params4.height = (int) this.mAnimHeightWindow;
                params4.width = (int) this.mAnimWidthWindow;
            }
            params4.topMargin = (int) this.mAnimMarginTopWindowSize;
            this.mAnimView.setLayoutParams(params4);
            logoParams = (RelativeLayout.LayoutParams) this.mLogo.getLayoutParams();
            logoParams.height = (int) this.mLogoHeightWindow;
            logoParams.width = (int) this.mLogoWidthWindow;
            logoParams.leftMargin = (int) this.mLogoMarginLeftWindow;
            this.mLogo.setLayoutParams(logoParams);
        }
        if (this.mHasLoadingTip) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "measureHelpTip=" + this.mIsFullScreen);
            }
            measureHelpTip();
        }
    }

    private void measureHelpTip() {
        if (this.mIsFullScreen) {
            LayoutParams helpTxtParams = (LayoutParams) this.mHelpTipView.getLayoutParams();
            helpTxtParams.height = ((int) this.mHelpTipSizeFull) + 1;
            helpTxtParams.topMargin = ((int) this.mHelpTxtMarginTopFull) + 1;
            this.mHelpTipView.setLayoutParams(helpTxtParams);
            this.mHelpTipView.setTextSize(0, this.mHelpTxtSizeFull);
            this.mHelpTipBubble.setBounds(0, 0, ((int) this.mHelpTipSizeFull) - 1, ((int) this.mHelpTipSizeFull) - 1);
            this.mHelpTipView.setCompoundDrawables(this.mHelpTipBubble, null, null, null);
            return;
        }
        helpTxtParams = (LayoutParams) this.mHelpTipView.getLayoutParams();
        helpTxtParams.height = ((int) this.mHelpTipSizeWindow) + 1;
        helpTxtParams.topMargin = ((int) this.mHelpTxtMarginTopWindow) + 1;
        this.mHelpTipView.setLayoutParams(helpTxtParams);
        this.mHelpTipView.setTextSize(0, this.mHelpTxtSizeWindow);
        this.mHelpTipBubble.setBounds(0, 0, ((int) this.mHelpTipSizeWindow) - 1, ((int) this.mHelpTipSizeWindow) - 1);
        this.mHelpTipView.setCompoundDrawables(this.mHelpTipBubble, null, null, null);
    }

    public void setCurrentChannel(TVChannelCarousel channelCarousel) {
        if (channelCarousel != null) {
            CharSequence name = channelCarousel.name;
            if (!StringUtils.isEmpty(name)) {
                this.mTxtName.setText(name);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setCurrentChannel name=" + name);
            }
        }
        if (this.mHasLoadingTip) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "showHelpTip=" + this.mHasLoadingTip);
            }
            measureHelpTip();
            randomHelpTip();
        }
    }

    private void randomHelpTip() {
        int i = this.mHelpIndex;
        this.mHelpIndex = i + 1;
        this.mHelpTipView.setText(this.mHelpList[i % this.mHelpList.length]);
        this.mHelpTipView.setVisibility(0);
    }
}
