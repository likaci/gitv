package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.carousel.CarouselMediaControllerOverlay;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.overlay.MediaControllerContainer;
import com.gala.video.app.player.ui.overlay.panels.AbsMenuPanel;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel;
import com.gala.video.app.player.ui.widget.views.CarouselLoadingView;
import com.gala.video.app.player.ui.widget.views.LoadingView;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class GalaPlayerView extends RelativeLayout {
    private static final String TAG = "Player/Ui/GalaPlayerView";
    private CarouselLoadingView mCarouselLoadingView;
    private CarouselMediaControllerOverlay mCarouselMediaController;
    private Context mContext;
    private ViewMode mCurrentViewMode;
    private PlayerErrorPanel mErrorPanel;
    private AbsFullScreenHint mFullScreenHint;
    private LoadingView mLoadingView;
    private MediaControllerContainer mMediaController;
    private AbsMenuPanel mMenuPanel;
    private MovieVideoView mVideoView;
    private FrameLayout mVideoViewPanel;

    public enum ViewMode {
        COMMON,
        CAROUSEL
    }

    public GalaPlayerView(Context context, ViewMode mode) {
        super(context);
        init(context, mode);
    }

    public GalaPlayerView(Context context, AttributeSet attrs, ViewMode mode) {
        super(context, attrs);
        init(context, mode);
    }

    public GalaPlayerView(Context context, AttributeSet attrs, int defStyleAttr, ViewMode mode) {
        super(context, attrs, defStyleAttr);
        init(context, mode);
    }

    private void init(Context context, ViewMode mode) {
        this.mContext = context;
        this.mCurrentViewMode = mode;
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C1291R.layout.player_view_gala_videoview, this);
        this.mVideoView = (MovieVideoView) findViewById(C1291R.id.movie_video_view);
        this.mVideoViewPanel = (FrameLayout) findViewById(C1291R.id.mediacontroller);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "init: context=" + context + ", mLoadingView=" + this.mLoadingView + ", mVideoView=" + this.mVideoView + ", mMediaController=" + this.mMediaController + ", mFullScreenHint=" + this.mFullScreenHint);
        }
        if (PlayerAppConfig.getStretchPlaybackToFullScreen()) {
            this.mVideoView.setVideoRatio(4);
        } else {
            this.mVideoView.setVideoRatio(1);
        }
        if (this.mCurrentViewMode == ViewMode.CAROUSEL) {
            initCarouselLoadingView();
            initCarouselMediaController();
            initMenuPanel();
            initCarouselFullscreenHint();
        } else {
            initMediaController();
            initMenuPanel();
            initLoadingView();
            initFullscreenHint();
        }
        this.mErrorPanel = new PlayerErrorPanel(this.mContext);
    }

    private void initFullscreenHint() {
        this.mFullScreenHint = new FullScreenHint(this.mContext);
        addView(this.mFullScreenHint, new LayoutParams(-1, -1));
        this.mFullScreenHint.setVisibility(8);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initFullscreenHint: " + this.mFullScreenHint);
        }
    }

    private void initCarouselFullscreenHint() {
        this.mFullScreenHint = new CarouselFullScreenHint(this.mContext);
        addView(this.mFullScreenHint, new LayoutParams(-1, -1));
        this.mFullScreenHint.setVisibility(8);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initCarouselFullscreenHint: " + this.mFullScreenHint);
        }
    }

    private void initLoadingView() {
        this.mLoadingView = new LoadingView(this.mContext);
        LayoutParams params = new LayoutParams(-1, -1);
        this.mLoadingView.setVisibility(8);
        addView(this.mLoadingView, params);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initLoadingView: " + this.mLoadingView);
        }
    }

    private void initCarouselLoadingView() {
        this.mCarouselLoadingView = new CarouselLoadingView(this.mContext);
        addView(this.mCarouselLoadingView, new LayoutParams(-1, -1));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initCarouselLoadingView: " + this.mLoadingView);
        }
    }

    private void initMenuPanel() {
        this.mMenuPanel = PlayerAppConfig.getMenuPanel(this.mContext);
        this.mMenuPanel.setGravity(3);
        this.mMenuPanel.setClipChildren(false);
        IPlayerMenuPanelUIStyle style4MenuPanel = PlayerAppConfig.getUIStyle().getPlayerMenuPanelUIStyle();
        this.mMenuPanel.setBackgroundResource(style4MenuPanel.getMenuPanelBgResId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(style4MenuPanel.getMenuPanelLayoutParaW(), style4MenuPanel.getMenuPanelLayoutParaH());
        params.addRule(12, -1);
        addView(this.mMenuPanel, params);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initMenuPanel: " + this.mMenuPanel);
        }
    }

    private void initMediaController() {
        this.mMediaController = new MediaControllerContainer(this.mContext);
        this.mMediaController.setGravity(17);
        addView(this.mMediaController, new LayoutParams(-1, -1));
    }

    private void initCarouselMediaController() {
        this.mCarouselMediaController = new CarouselMediaControllerOverlay(this.mContext);
        this.mCarouselMediaController.setGravity(17);
        addView(this.mCarouselMediaController, new LayoutParams(-1, -1));
    }

    public FrameLayout getVideoViewPanel() {
        return this.mVideoViewPanel;
    }

    public AbsFullScreenHint getFullScreenHint() {
        return this.mFullScreenHint;
    }

    public MovieVideoView getVideoView() {
        return this.mVideoView;
    }

    public MediaControllerContainer getMediaController() {
        return this.mMediaController;
    }

    public AbsMenuPanel getMenuPanel() {
        return this.mMenuPanel;
    }

    public LoadingView getLoadingView() {
        return this.mLoadingView;
    }

    public CarouselLoadingView getCarouselLoadingView() {
        return this.mCarouselLoadingView;
    }

    public CarouselMediaControllerOverlay getCarouselMediaController() {
        return this.mCarouselMediaController;
    }

    public PlayerErrorPanel getPlayerErrorPanel() {
        return this.mErrorPanel;
    }
}
