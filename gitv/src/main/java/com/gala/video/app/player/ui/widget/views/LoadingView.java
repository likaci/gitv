package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.config.ILoadingViewUiConfig;
import com.gala.video.app.player.ui.config.LoadingViewAnimManager;
import com.gala.video.app.player.utils.PlayerUIHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;

public class LoadingView extends FrameLayout implements IScreenUISwitcher {
    private static final int MSG_UPDATE = 101;
    private static final String TAG = "Player/Ui/LoadingView";
    private String mAlbumId;
    private boolean mAlreayPlay;
    private float mAnimHeight;
    private float mAnimHeightWindow;
    private float mAnimMarginTopFullSize;
    private float mAnimMarginTopWindowSize;
    private ImageView mAnimView = null;
    private float mAnimWidth;
    private float mAnimWidthWindow;
    private ILoadingViewUiConfig mConfig;
    private Context mContext;
    private float mDeriveHeight;
    private float mDeriveHeightWindow;
    private float mDeriveMarginTop;
    private float mDeriveMarginTopWindow;
    private float mDeriveWidth;
    private float mDeriveWidthWindow;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            LogUtils.d(LoadingView.TAG, "handleMessage()" + msg.what);
            switch (msg.what) {
                case 101:
                    if (LoadingView.this.mConfig != null) {
                        LoadingView.this.mConfig.checkLoadingFrameAnim();
                        LoadingView.this.mConfig.checkLoadingBackground();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private LoadingViewAnimManager mInstacnce;
    private boolean mIsFullScreen;
    private RelativeLayout mLoadingContainer;
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
    private ImageView mVideoDerive;
    private float mWindowZoomRatio;
    private RelativeLayout sLoadingLayout = null;

    public LoadingView(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoadingView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
    }

    public void initViews() {
        removeAllViews();
        LogUtils.d(TAG, "initViews");
        this.sLoadingLayout = (RelativeLayout) Project.getInstance().getConfig().getPlayerLoadingView(getContext());
        this.mTxtName = (TextView) this.sLoadingLayout.findViewById(R.id.share_description);
        this.mAnimView = (ImageView) this.sLoadingLayout.findViewById(R.id.share_loading);
        this.mLoadingContainer = (RelativeLayout) this.sLoadingLayout.findViewById(R.id.share_loading_container);
        this.mLogo = (ImageView) this.sLoadingLayout.findViewById(R.id.share_logo);
        this.mVideoDerive = (ImageView) this.sLoadingLayout.findViewById(R.id.share_video_derive);
        this.mConfig = PlayerAppConfig.getConfig4Loading();
        Bitmap logoBitmap = this.mConfig.getLoadingLogo();
        if (!(this.mLogo == null || logoBitmap == null)) {
            this.mLogo.setImageBitmap(logoBitmap);
        }
        this.mInstacnce = LoadingViewAnimManager.getInstance(this.mContext.getApplicationContext());
        BitmapDrawable bgDrawable = this.mInstacnce.getBackground();
        if (!(this.mLoadingContainer == null || bgDrawable == null)) {
            this.mLoadingContainer.setBackgroundDrawable(bgDrawable);
        }
        this.sLoadingLayout.setVisibility(0);
        addView(this.sLoadingLayout, -1, -1);
        initViewSize();
    }

    private void initViewSize() {
        if (this.mConfig != null) {
            this.mTxtNameFullSize = (float) this.mConfig.getTxtNameSize();
            this.mTxtNameMaginLRFullSize = (float) this.mConfig.getTxtNameMarginLeftAndRight();
            this.mLogoWidth = (float) this.mConfig.getLogoWidth();
            this.mLogoHeight = (float) this.mConfig.getLogoHeight();
            this.mLogoMarginLeft = (float) this.mConfig.getLogoMarginLeft();
            this.mAnimHeight = (float) this.mInstacnce.getAnimHeight();
            this.mAnimWidth = (float) this.mInstacnce.getAnimWidth();
            this.mAnimMarginTopFullSize = (float) this.mConfig.getAnimMarginTop();
            this.mDeriveWidth = (float) this.mConfig.getDeriveWidth();
            this.mDeriveHeight = (float) this.mConfig.getDeriveHeight();
            this.mDeriveMarginTop = (float) this.mConfig.getDeriveMarginTop();
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
        LogUtils.d(TAG, "show mIsFullScreen=" + this.mIsFullScreen);
        AnimationDrawable anim = this.mInstacnce.getAnim();
        if (!(this.mAnimView == null || anim == null)) {
            anim.setOneShot(false);
            this.mAnimView.setImageDrawable(anim);
        }
        setVisibility(0);
    }

    public void hide() {
        LogUtils.d(TAG, "hide");
        if (this.mAnimView != null) {
            AnimationDrawable animDrawable = (AnimationDrawable) this.mAnimView.getDrawable();
            if (animDrawable != null) {
                animDrawable.stop();
            }
            this.mAnimView.setImageDrawable(null);
        }
        setVisibility(8);
    }

    private void removeMessage() {
        this.mHandler.removeMessages(101);
    }

    public void onActivityDestroyed() {
        hide();
        removeMessage();
    }

    public void onActivityStop() {
        removeMessage();
    }

    public void onError() {
        removeMessage();
    }

    public void setLoadingText(String text) {
        LogUtils.d(TAG, "addText: " + text);
        this.mVideoDerive.setImageDrawable(null);
        if (this.mTxtName != null) {
            switchScreen(this.mIsFullScreen, this.mWindowZoomRatio);
            this.mTxtName.setText(text);
        }
        LogUtils.d(TAG, "setLoadingText: mAlbumId" + this.mAlbumId);
        if (!StringUtils.isEmpty(this.mAlbumId)) {
            showVideoDerive();
        }
    }

    public void setAlbumId(String id) {
        LogUtils.d(TAG, "setAlbumId: " + id);
        this.mAlbumId = id;
        showVideoDerive();
    }

    private void showVideoDerive() {
        if (PlayerAppConfig.isShowVideoDerive()) {
            Bitmap bitmap = PlayerUIHelper.getVideoDeriveBitmap(this.mAlbumId);
            if (bitmap != null) {
                this.mVideoDerive.setImageBitmap(bitmap);
                this.mVideoDerive.setVisibility(0);
            }
        }
    }

    public void startLoadingAnimation() {
        LogUtils.d(TAG, "startLoadingAnimation()");
        if (this.mAnimView != null) {
            Animation animation = Project.getInstance().getControl().getLoadingViewAnimation();
            if (animation != null) {
                this.mAnimView.clearAnimation();
                this.mAnimView.setAnimation(animation);
                animation.startNow();
                return;
            }
            AnimationDrawable animDrawable = (AnimationDrawable) this.mAnimView.getDrawable();
            if (animDrawable != null) {
                animDrawable.start();
            }
        }
    }

    private void updateWindowSize(float zoomRatio) {
        this.mTxtNameMaginLRWindowSize = this.mTxtNameMaginLRFullSize * zoomRatio;
        this.mTxtNameWindowSize = this.mTxtNameFullSize * zoomRatio;
        this.mLogoWidthWindow = this.mLogoWidth * zoomRatio;
        this.mLogoHeightWindow = this.mLogoHeight * zoomRatio;
        this.mLogoMarginLeftWindow = this.mLogoMarginLeft * zoomRatio;
        this.mAnimHeightWindow = this.mAnimHeight * zoomRatio;
        this.mAnimWidthWindow = this.mAnimWidth * zoomRatio;
        this.mDeriveWidthWindow = this.mDeriveWidth * zoomRatio;
        this.mDeriveHeightWindow = this.mDeriveHeight * zoomRatio;
        this.mDeriveMarginTopWindow = this.mDeriveMarginTop * zoomRatio;
        this.mAnimMarginTopWindowSize = this.mAnimMarginTopFullSize * zoomRatio;
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        LogUtils.d(TAG, "switchScreen: " + isFullScreen);
        this.mIsFullScreen = isFullScreen;
        this.mWindowZoomRatio = zoomRatio;
        updateWindowSize(zoomRatio);
        LayoutParams params3;
        LayoutParams params4;
        RelativeLayout.LayoutParams logoParams;
        RelativeLayout.LayoutParams deriveParams;
        if (isFullScreen) {
            if (this.mTxtName != null) {
                this.mTxtName.setTextSize(0, this.mTxtNameFullSize);
                params3 = (LayoutParams) this.mTxtName.getLayoutParams();
                params3.leftMargin = (int) this.mTxtNameMaginLRFullSize;
                params3.rightMargin = (int) this.mTxtNameMaginLRFullSize;
                this.mTxtName.setLayoutParams(params3);
                params4 = (LayoutParams) this.mAnimView.getLayoutParams();
                if (!(this.mAnimHeight == 0.0f || this.mAnimWidth == 0.0f)) {
                    params4.height = (int) this.mAnimHeight;
                    params4.width = (int) this.mAnimWidth;
                }
                params4.topMargin = (int) this.mAnimMarginTopFullSize;
                this.mAnimView.setLayoutParams(params4);
                logoParams = (RelativeLayout.LayoutParams) this.mLogo.getLayoutParams();
                logoParams.height = (int) this.mLogoHeight;
                logoParams.width = (int) this.mLogoWidth;
                logoParams.leftMargin = (int) this.mLogoMarginLeft;
                this.mLogo.setLayoutParams(logoParams);
                deriveParams = (RelativeLayout.LayoutParams) this.mVideoDerive.getLayoutParams();
                deriveParams.height = (int) this.mDeriveHeight;
                deriveParams.width = (int) this.mDeriveWidth;
                deriveParams.topMargin = (int) this.mDeriveMarginTop;
                this.mVideoDerive.setLayoutParams(deriveParams);
            }
        } else if (this.mTxtName != null) {
            this.mTxtName.setTextSize(0, this.mTxtNameWindowSize);
            params3 = (LayoutParams) this.mTxtName.getLayoutParams();
            params3.leftMargin = (int) this.mTxtNameMaginLRWindowSize;
            params3.rightMargin = (int) this.mTxtNameMaginLRWindowSize;
            this.mTxtName.setLayoutParams(params3);
            params4 = (LayoutParams) this.mAnimView.getLayoutParams();
            if (!(this.mAnimHeight == 0.0f || this.mAnimWidth == 0.0f)) {
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
            deriveParams = (RelativeLayout.LayoutParams) this.mVideoDerive.getLayoutParams();
            deriveParams.height = (int) this.mDeriveHeightWindow;
            deriveParams.width = (int) this.mDeriveWidthWindow;
            deriveParams.topMargin = (int) this.mDeriveMarginTopWindow;
            this.mVideoDerive.setLayoutParams(deriveParams);
        }
    }
}
