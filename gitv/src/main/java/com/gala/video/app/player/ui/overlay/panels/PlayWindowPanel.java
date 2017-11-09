package com.gala.video.app.player.ui.overlay.panels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RPAGETYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RSEATTYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.RFR;
import com.gala.sdk.event.AdSpecialEvent;
import com.gala.sdk.event.AdSpecialEvent.EventType;
import com.gala.sdk.event.OnAdSpecialEventListener;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.ISecondaryCodeError;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.controller.DataDispatcher;
import com.gala.video.app.player.controller.IDetailMultiListener;
import com.gala.video.app.player.controller.MultiScreenEventDispatcher;
import com.gala.video.app.player.controller.UIEventDispatcher;
import com.gala.video.app.player.data.DetailDataCacheManager;
import com.gala.video.app.player.multiscreen.MultiEventHelper;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.ui.overlay.UiHelper;
import com.gala.video.app.player.ui.widget.GalaPlayerView;
import com.gala.video.app.player.ui.widget.MovieVideoView;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.app.player.utils.DetailItemUtils;
import com.gala.video.app.player.utils.ImageViewUtils;
import com.gala.video.app.player.utils.PlayerUIHelper;
import com.gala.video.app.player.utils.RCMultiKeyEventUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.KeyValue;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class PlayWindowPanel implements IDetailMultiListener {
    private String TAG = "AlbumDetail/UI/PlayWindowPanel";
    private AlbumInfo mAlbumInfo;
    private IAlbumDetailUiConfig mConfig;
    private Context mContext;
    private int mCurPlayerStatus = 1;
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    private IVideo mCurVideo;
    private boolean mEnableWindowPlay = true;
    private GalaPlayerView mGalaVideoView = null;
    private ImageView mImgVideoSource;
    private boolean mIsPausedByUser = false;
    private KeyValue mKeyValue = new KeyValue();
    private MovieVideoView mMovieVideoView = null;
    private IMultiEventHelper mMultiEventHelper = null;
    private OnAdSpecialEventListener mOnSpecialEventListener = new OnAdSpecialEventListener() {
        public void onAdSpecialEvent(AdSpecialEvent specialEvent) {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, ">> onSpecialEvent" + specialEvent);
            if (specialEvent.getEventType() == EventType.AD_HIDE) {
                DataDispatcher.instance().postOnMainThread(PlayWindowPanel.this.mContext, 20, PlayWindowPanel.this.mCurVideo);
            }
        }
    };
    private IPingbackContext mPingbackContext;
    private OnPlayWindowClickedListener mPlayWindowClickedListener;
    private LayoutParams mPlayWindowUIParams;
    private View mPlayWindowView;
    private IGalaVideoPlayer mPlayer;
    private RCMultiKeyEventUtils mRCMultiKeyEventUtils;
    private View mRootView;
    private boolean mSurfaceAddedForTailer = false;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private FrameLayout mVideoContainer;
    private OnPlayerStateChangedListener mVideoStateListener = new OnPlayerStateChangedListener() {
        public void onVideoSwitched(IVideo video, int type) {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, "mVideoStateListener.onVideoSwitched + " + type + ", video=" + video);
            PlayWindowPanel.this.mCurVideo = video;
            PlayWindowPanel.this.hideVideoSourceImage();
            switch (type) {
                case 0:
                case 3:
                    UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 7, video);
                    return;
                case 1:
                case 5:
                case 6:
                case 7:
                case 11:
                case 12:
                    PlayWindowPanel.this.mSurfaceAddedForTailer = false;
                    UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 10, video);
                    return;
                case 2:
                case 4:
                case 10:
                case 13:
                    UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 12, video);
                    return;
                case 9:
                    UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 13, video);
                    return;
                default:
                    return;
            }
        }

        public void onVideoStarted(IVideo video) {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, "mVideoStateListener.onVideoStarted");
            if (PlayWindowPanel.this.mCurVideo == null) {
                LogRecordUtils.logd(PlayWindowPanel.this.TAG, "onVideoStarted, mCurVideoData is released.");
            } else if (PlayWindowPanel.this.mIsPausedByUser) {
                PlayWindowPanel.this.mPlayer.onUserPause();
            } else {
                PlayWindowPanel.this.showVideoSourceImage();
                PlayWindowPanel.this.updatePlayerStatus(3);
            }
        }

        public void onPlaybackFinished() {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, "mVideoStateListener.onPlaybackFinished");
            if (PlayWindowPanel.this.mCurPlayerStatus != 4 && PlayWindowPanel.this.mCurPlayerStatus != 2) {
                UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 8, null);
                PlayWindowPanel.this.hideVideoSourceImage();
                PlayWindowPanel.this.showAlbumImage();
                PlayWindowPanel.this.updatePlayerStatus(5);
            }
        }

        public void onScreenModeSwitched(ScreenMode newMode) {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, "mVideoStateListener.onScreenModeSwitched: " + newMode);
            UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 6, newMode);
            if (newMode == ScreenMode.FULLSCREEN && (PlayWindowPanel.this.mContext instanceof IPingbackContext)) {
                ((IPingbackContext) PlayWindowPanel.this.mContext).setItem("rfr", RFR.PLAYER);
                PingBackCollectionFieldUtils.setRfr(RFR.PLAYER.getValue());
            }
            if (PlayWindowPanel.this.mSurfaceAddedForTailer && PlayWindowPanel.this.mCurScreenMode == ScreenMode.FULLSCREEN && newMode == ScreenMode.WINDOWED) {
                LogRecordUtils.logd(PlayWindowPanel.this.TAG, "onScreenModeSwitched");
                PlayWindowPanel.this.removePlayerSurfaceView();
                PlayWindowPanel.this.pausePlayer();
                PlayWindowPanel.this.mSurfaceAddedForTailer = false;
            } else if (!PlayWindowPanel.this.mSurfaceAddedForTailer && PlayWindowPanel.this.mCurScreenMode == ScreenMode.FULLSCREEN && newMode == ScreenMode.WINDOWED) {
                PlayWindowPanel.this.loadAlbumImage();
                PlayWindowPanel.this.hideAlbumImage();
            }
            PlayWindowPanel.this.updateScreenMode(newMode);
        }

        public boolean onError(IVideo video, ISdkError error) {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, "mVideoStateListener.onError error=" + error);
            if (error != null && error.getModule() == 10000 && error.getCode() == 1000) {
                PlayWindowPanel.this.updatePlayerStatus(2);
                PlayWindowPanel.this.updateScreenMode(ScreenMode.WINDOWED);
                UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 9, ScreenMode.WINDOWED);
            } else if (error == null || !(DataHelper.isUserCannotPreviewCode(String.valueOf(error.getServerCode())) || ((error instanceof ISecondaryCodeError) && DataHelper.isUserCannotPreviewCode(((ISecondaryCodeError) error).getSecondaryCode())))) {
                PlayWindowPanel.this.updatePlayerStatus(4);
            } else {
                PlayWindowPanel.this.updatePlayerStatus(2);
                PlayWindowPanel.this.updateScreenMode(ScreenMode.WINDOWED);
                UIEventDispatcher.instance().post(PlayWindowPanel.this.mContext, 9, ScreenMode.WINDOWED);
            }
            PlayWindowPanel.this.hideVideoSourceImage();
            return false;
        }

        public void onAdStarted() {
            LogRecordUtils.logd(PlayWindowPanel.this.TAG, ">> mVideoStateListener.onAdStarted");
            PlayWindowPanel.this.updatePlayerStatus(6);
            PlayWindowPanel.this.hideVideoSourceImage();
        }

        public void onAdEnd() {
        }

        public void onPrepared() {
        }
    };
    private View mWindowBlackBG;
    private ImageView mWindowImageView;

    public interface OnPlayWindowClickedListener {
        void onPlayWindowClicked();
    }

    public PlayWindowPanel(Context context, View rootView, FrameLayout videoContainer, IAlbumDetailUiConfig config) {
        this.mRootView = rootView;
        this.mContext = context;
        this.mPingbackContext = (IPingbackContext) this.mContext;
        this.mConfig = config;
        this.mEnableWindowPlay = this.mConfig.isEnableWindowPlay();
        this.mVideoContainer = videoContainer;
        initViews();
        MultiScreenEventDispatcher.instance().register(this);
    }

    public void setVideo(AlbumInfo albumInfo, IVideo video) {
        LogRecordUtils.logd(this.TAG, ">> setVideo, video=" + video);
        this.mCurVideo = video;
        this.mAlbumInfo = albumInfo;
    }

    public void notifyBasicInfoReady() {
        if (this.mEnableWindowPlay || this.mWindowImageView.getVisibility() != 0) {
            loadAlbumImage();
        } else {
            showAlbumImage();
        }
    }

    public boolean createPlayer(int resultCode) {
        LogRecordUtils.logd(this.TAG, ">> createPlayer" + this.mCurVideo);
        if (!this.mConfig.isEnableWindowPlay()) {
            showAlbumImage();
            LogRecordUtils.logd(this.TAG, "createPlayer, does not support player window.");
            return false;
        } else if (this.mCurVideo == null) {
            LogRecordUtils.loge(this.TAG, "createPlayer, error, mCurVideo is null!");
            return false;
        } else if (this.mPlayer != null) {
            LogRecordUtils.logd(this.TAG, "createPlayer, Player has been initialized already!");
            return false;
        } else {
            initPlayer(this.mCurVideo, resultCode);
            LogRecordUtils.logd(this.TAG, "<< createPlayer, success.");
            return true;
        }
    }

    public void resumePlayer() {
        LogRecordUtils.logd(this.TAG, ">> resumePlayer");
        LogRecordUtils.logd(this.TAG, ">> mCurPlayerStatus = " + this.mCurPlayerStatus);
        if (this.mPlayer == null || !this.mPlayer.isPlaying()) {
            LogRecordUtils.logd(this.TAG, ">> resumePlayer, player is not playing.");
            if (this.mCurPlayerStatus == 2 || this.mCurPlayerStatus == 4) {
                LogRecordUtils.logd(this.TAG, ">> mCurPlayerStatus = " + this.mCurPlayerStatus);
            } else {
                showAlbumImage();
            }
            if (!(this.mPlayer == null || this.mCurPlayerStatus == 4 || this.mCurPlayerStatus == 2 || this.mCurPlayerStatus == 5)) {
                this.mPlayer.onUserPlay();
                if (this.mCurPlayerStatus == 3) {
                    showVideoSourceImage();
                }
                hideAlbumImage();
                this.mIsPausedByUser = false;
            }
            LogRecordUtils.logd(this.TAG, "<< resumePlayer");
            return;
        }
        hideAlbumImage();
        LogRecordUtils.logd(this.TAG, ">> resumePlayer, player is playing.");
    }

    public void fullScreenButton() {
        LogRecordUtils.logd(this.TAG, ">> fullScreenButton," + this.mCurVideo);
        if (this.mCurVideo == null) {
            LogRecordUtils.logd(this.TAG, "fullScreenButton, video is null.");
        } else if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "fullScreenButton, mPlayer is null.");
        } else {
            this.mIsPausedByUser = false;
            switch (this.mCurPlayerStatus) {
                case 1:
                case 3:
                case 6:
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    break;
                case 2:
                    updatePlayerStatus(1);
                    this.mPlayer.replay();
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    break;
                case 4:
                    this.mPlayer.onErrorClicked();
                    break;
                case 5:
                    updatePlayerStatus(1);
                    hideAlbumImage();
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    this.mPlayer.replay();
                    break;
                default:
                    LogRecordUtils.logd(this.TAG, "fullScreenButton, unhandled mCurPlayerStatus=" + this.mCurPlayerStatus);
                    break;
            }
            LogRecordUtils.logd(this.TAG, "<< fullScreenButton");
        }
    }

    public void startPlay(IVideo video) {
        LogRecordUtils.logd(this.TAG, ">> startPlay, video=" + video);
        if (video == null) {
            LogRecordUtils.logd(this.TAG, "startPlay, video is null.");
            return;
        }
        String from = "";
        String tabSource = "";
        if (this.mContext instanceof Activity) {
            Intent intent = ((Activity) this.mContext).getIntent();
            from = intent.getStringExtra("from");
            tabSource = intent.getStringExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE);
        } else {
            LogRecordUtils.logd(this.TAG, "startPlay, mContext is not instance of Activity.");
        }
        if (!this.mConfig.isEnableWindowPlay()) {
            if (DetailDataCacheManager.instance().getCurrentDetailInfo() != null) {
                video.setAlbumDetailPic(DetailDataCacheManager.instance().getCurrentDetailInfo().pic);
            }
            DetailItemUtils.startPlayerFromDetailVideo(this.mContext, video.getAlbum(), from, tabSource, this.mAlbumInfo.getAlbum());
        } else if (isPlayerSurfaceValidate()) {
            startPlayerInner(video, from);
        }
        LogRecordUtils.logd(this.TAG, "<< startPlay");
    }

    public void startTrailer(PlayParams param) {
        LogRecordUtils.logd(this.TAG, ">> startTrailer, param=" + param);
        if (param == null) {
            LogRecordUtils.logd(this.TAG, "startTrailer, param is null.");
            return;
        }
        String from = "";
        String tabSource = "";
        if (this.mContext instanceof Activity) {
            Intent intent = ((Activity) this.mContext).getIntent();
            from = intent.getStringExtra("from");
            tabSource = intent.getStringExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE);
        } else {
            LogRecordUtils.logd(this.TAG, "startPlay, mContext is not instance of Activity.");
        }
        if (this.mConfig.isEnableWindowPlay()) {
            startPlayerInnerForTrailer(param, from);
        } else {
            LogRecordUtils.logd(this.TAG, "startTrailer no support window play");
        }
        LogRecordUtils.logd(this.TAG, "<< startTrailer");
    }

    private void startPlayerInnerForTrailer(PlayParams params, String from) {
        LogRecordUtils.logd(this.TAG, ">> startPlayerInnerForTrailer, from=" + from + ", params=" + params);
        if (params == null) {
            LogRecordUtils.logd(this.TAG, "startPlayerInnerForTrailer, video is null.");
        } else if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "startPlayerInnerForTrailer, mPlayer is null.");
        } else {
            boolean sameVideo;
            this.mIsPausedByUser = false;
            if (this.mPlayer.getVideo() == null) {
                LogRecordUtils.logd(this.TAG, "startPlayerInnerForTrailer, mPlayer.getVideo() is null!!");
                sameVideo = false;
            } else {
                sameVideo = ((Album) params.continuePlayList.get(params.playIndex)).tvQid.equals(this.mPlayer.getVideo().getTvId());
            }
            LogRecordUtils.logd(this.TAG, "startPlayerInnerForTrailer, sameVideo=" + sameVideo + ", mPlayer=" + this.mPlayer + ", mCurPlayerStatus=" + this.mCurPlayerStatus);
            switch (this.mCurPlayerStatus) {
                case 1:
                case 3:
                case 6:
                    trailerFullScreen();
                    if (sameVideo) {
                        resumePlayer();
                    } else {
                        updatePlayerStatus(1);
                        this.mPlayer.switchPlaylist(params);
                    }
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    this.mVideoContainer.setVisibility(0);
                    break;
                case 2:
                    trailerFullScreen();
                    updatePlayerStatus(1);
                    if (sameVideo) {
                        this.mPlayer.replay();
                    } else {
                        this.mPlayer.switchPlaylist(params);
                    }
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    this.mVideoContainer.setVisibility(0);
                    break;
                case 4:
                    if (!sameVideo) {
                        trailerFullScreen();
                        updatePlayerStatus(1);
                        this.mPlayer.switchPlaylist(params);
                        this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                        break;
                    }
                    this.mPlayer.onErrorClicked();
                    break;
                case 5:
                    trailerFullScreen();
                    updatePlayerStatus(1);
                    hideAlbumImage();
                    if (sameVideo) {
                        this.mPlayer.replay();
                    } else {
                        this.mPlayer.switchPlaylist(params);
                    }
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    this.mVideoContainer.setVisibility(0);
                    break;
            }
            LogRecordUtils.logd(this.TAG, "startPlayerInnerForTrailer, unhandled mCurPlayerStatus=" + this.mCurPlayerStatus);
            LogRecordUtils.logd(this.TAG, "<< startPlayerInnerForTrailer");
        }
    }

    private void trailerFullScreen() {
        LogRecordUtils.logd(this.TAG, "trailerFullScreen" + isPlayerSurfaceValidate());
        if (!isPlayerSurfaceValidate()) {
            addPlayerSurfaceView();
            this.mSurfaceAddedForTailer = true;
            LogRecordUtils.logd(this.TAG, "trailerFullScreen");
        }
    }

    private void startPlayerInner(IVideo video, String from) {
        LogRecordUtils.logd(this.TAG, ">> startPlayerInner, from=" + from + ", video=" + video);
        if (video == null) {
            LogRecordUtils.logd(this.TAG, "startPlayerInner, video is null.");
        } else if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "startPlayerInner, mPlayer is null.");
        } else {
            boolean sameVideo;
            this.mIsPausedByUser = false;
            if (this.mPlayer.getVideo() == null) {
                LogRecordUtils.logd(this.TAG, "startPlayerInner, mPlayer.getVideo() is null!!");
                sameVideo = false;
            } else {
                sameVideo = video.getTvId().equals(this.mPlayer.getVideo().getTvId());
            }
            LogRecordUtils.logd(this.TAG, "startPlayerInner, sameVideo=" + sameVideo + ", mPlayer=" + this.mPlayer + ", mCurPlayerStatus=" + this.mCurPlayerStatus + ", video=" + video);
            switch (this.mCurPlayerStatus) {
                case 1:
                case 3:
                case 6:
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    if (!sameVideo) {
                        updatePlayerStatus(1);
                        this.mPlayer.switchVideo(video, from);
                        break;
                    }
                    break;
                case 2:
                    updatePlayerStatus(1);
                    if (sameVideo) {
                        this.mPlayer.replay();
                    } else {
                        this.mPlayer.switchVideo(video, from);
                    }
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    break;
                case 4:
                    if (!sameVideo) {
                        updatePlayerStatus(1);
                        this.mPlayer.switchVideo(video, from);
                        this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                        break;
                    }
                    this.mPlayer.onErrorClicked();
                    break;
                case 5:
                    updatePlayerStatus(1);
                    hideAlbumImage();
                    this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                    if (!sameVideo) {
                        this.mPlayer.switchVideo(video, from);
                        break;
                    } else {
                        this.mPlayer.replay();
                        break;
                    }
                default:
                    LogRecordUtils.logd(this.TAG, "startPlayerInner, unhandled mCurPlayerStatus=" + this.mCurPlayerStatus);
                    break;
            }
            LogRecordUtils.logd(this.TAG, "<< startPlayerInner");
        }
    }

    public boolean isPlayerSurfaceValidate() {
        boolean isValid = false;
        if (!(this.mGalaVideoView == null || this.mGalaVideoView.getParent() == null || this.mGalaVideoView.getVisibility() != 0)) {
            isValid = true;
        }
        LogRecordUtils.logd(this.TAG, "isPlayerSurfaceValidate" + isValid);
        return isValid;
    }

    public void pausePlayer() {
        LogRecordUtils.logd(this.TAG, ">> pausePlayer");
        if (this.mIsPausedByUser) {
            LogRecordUtils.logd(this.TAG, ">> pausePlayer, already paused.");
            return;
        }
        if (!(this.mPlayer == null || this.mCurPlayerStatus == 4 || this.mCurPlayerStatus == 2 || this.mCurPlayerStatus == 5)) {
            this.mPlayer.onUserPause();
            hideVideoSourceImage();
            showAlbumImage();
            this.mIsPausedByUser = true;
        }
        LogRecordUtils.logd(this.TAG, "<< pausePlayer");
    }

    public void sleepPlayer() {
        if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "sleepPlayer, player is released already.");
        } else {
            this.mPlayer.sleep();
        }
    }

    public void wakeupPlayer(int resultCode) {
        if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "wakeupPlayer, player is released already.");
            return;
        }
        boolean isSleeping = this.mPlayer.isSleeping();
        LogRecordUtils.logd(this.TAG, "wakeupPlayer, mPlayer.isSleeping()=" + isSleeping + ", resultCode=" + resultCode);
        if (!isSleeping || resultCode == 1 || resultCode == 10 || resultCode == 22) {
            releasePlayer();
            createPlayer(resultCode);
            return;
        }
        this.mPlayer.wakeUp();
    }

    private void releasePlayer() {
        LogRecordUtils.logd(this.TAG, ">> releasePlayer");
        updatePlayerStatus(1);
        if (this.mPlayer != null) {
            Intent intent = ((Activity) this.mContext).getIntent();
            PlayParams playParams = (PlayParams) intent.getSerializableExtra("play_list_info");
            if (playParams != null) {
                playParams.sourceType = this.mPlayer.getSourceType();
                LogRecordUtils.logd(this.TAG, "releasePlayer sourceType =" + this.mPlayer.getSourceType());
            } else {
                playParams = new PlayParams();
            }
            intent.putExtra("play_list_info", playParams);
            this.mPlayer.release();
            releasePlayerViews();
            this.mPlayer = null;
            LogRecordUtils.logd(this.TAG, ">> releasePlayer mPlayer == null");
        }
        LogRecordUtils.logd(this.TAG, "<< releasePlayer");
    }

    public void onActivityFinishing() {
        if (this.mGalaVideoView != null) {
            this.mGalaVideoView.setVisibility(8);
        }
        releasePlayer();
        if (!(this.mCurVideo == null || this.mCurVideo.getProvider() == null)) {
            LogRecordUtils.logd(this.TAG, "onActivityFinishing clear Cache");
            this.mCurVideo.getProvider().clearCache();
        }
        this.mCurVideo = null;
        MultiScreenEventDispatcher.instance().unregister();
    }

    public void setPlayWindowNextRightId(int id) {
        LogRecordUtils.logd(this.TAG, ">> setPlayWindowNextRightId, id=" + id + ", view=" + this.mRootView.findViewById(id));
        this.mPlayWindowView.setNextFocusRightId(id);
    }

    public void removePlayerSurfaceView() {
        LogRecordUtils.logd(this.TAG, ">> removePlayerSurfaceView");
        if (!this.mEnableWindowPlay) {
            LogRecordUtils.logd(this.TAG, "removePlayerSurfaceView, mEnableWindowPlay is false.");
        } else if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "removePlayerSurfaceView, mPlayer is null.");
            this.mVideoContainer.setVisibility(8);
            this.mVideoContainer.removeView(this.mGalaVideoView);
        } else {
            if (this.mGalaVideoView.getParent() != null) {
                LogRecordUtils.logd(this.TAG, "removePlayerSurfaceView, mGalaVideoView.getParent() is not null, ready to remove.");
                this.mMovieVideoView.setIgnoreWindowChange(true);
                this.mGalaVideoView.setVisibility(8);
                this.mMovieVideoView.setIgnoreWindowChange(false);
            } else {
                LogRecordUtils.logd(this.TAG, "removePlayerSurfaceView, mGalaVideoView.getParent() is null.");
            }
            this.mVideoContainer.setVisibility(8);
            LogRecordUtils.logd(this.TAG, "<< removePlayerSurfaceView");
        }
    }

    public void addPlayerSurfaceView() {
        LogRecordUtils.logd(this.TAG, ">> addPlayerSurfaceView");
        if (!this.mEnableWindowPlay) {
            LogRecordUtils.logd(this.TAG, "addPlayerSurfaceView, mEnableWindowPlay is false.");
        } else if (this.mPlayer == null) {
            LogRecordUtils.logd(this.TAG, "addPlayerSurfaceView, mPlayer is null.");
        } else {
            if (this.mGalaVideoView.getParent() == null) {
                LogRecordUtils.logd(this.TAG, "addPlayerSurfaceView, mGalaVideoView.getParent() is null, ready to add.");
                this.mMovieVideoView.setIgnoreWindowChange(true);
                this.mVideoContainer.addView(this.mGalaVideoView);
                this.mMovieVideoView.setIgnoreWindowChange(false);
            } else {
                LogRecordUtils.logd(this.TAG, "addPlayerSurfaceView, mGalaVideoView.getParent() is not null!");
            }
            if (this.mGalaVideoView.getVisibility() != 0) {
                LogRecordUtils.logd(this.TAG, "mGalaVideoView.getVisibility() != View.VISIBLE");
                this.mMovieVideoView.setIgnoreWindowChange(true);
                this.mGalaVideoView.setVisibility(0);
                this.mMovieVideoView.setIgnoreWindowChange(false);
            } else {
                LogRecordUtils.logd(this.TAG, "mGalaVideoView.getVisibility() is View.VISIBLE");
            }
            hideAlbumImage();
            this.mMovieVideoView.setVisibility(0);
            this.mVideoContainer.setVisibility(0);
            LogRecordUtils.logd(this.TAG, "<< addPlayerSurfaceView");
        }
    }

    private void releasePlayerViews() {
        LogRecordUtils.logd(this.TAG, ">> releasePlayerViews");
        if (this.mMovieVideoView != null) {
            this.mMovieVideoView.setVisibility(8);
            this.mMovieVideoView = null;
        }
        if (this.mGalaVideoView != null) {
            this.mGalaVideoView.setVisibility(8);
            this.mGalaVideoView = null;
        }
        LogRecordUtils.logd(this.TAG, "<< releasePlayerViews");
    }

    private void initViews() {
        LogRecordUtils.logd(this.TAG, ">> initViews, mRootView=" + this.mRootView);
        this.mPlayWindowView = this.mRootView.findViewById(R.id.share_detail_playwindow);
        this.mImgVideoSource = (ImageView) this.mRootView.findViewById(R.id.share_detail_iv_video_source);
        this.mWindowImageView = (ImageView) this.mRootView.findViewById(R.id.share_detail_img_album_detail);
        this.mWindowBlackBG = this.mRootView.findViewById(R.id.share_detail_black_block);
        this.mPlayWindowView.setNextFocusLeftId(this.mPlayWindowView.getId());
        adjustLayoutParams();
        if (this.mEnableWindowPlay) {
            this.mPlayWindowView.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    if (PlayWindowPanel.this.mConfig.isEnableWindowPlay() && PlayWindowPanel.this.isPlayerSurfaceValidate()) {
                        PlayWindowPanel.this.sendPlayWindowClickPingback();
                    }
                    PlayWindowPanel.this.startPlay(PlayWindowPanel.this.mCurVideo);
                    if (PlayWindowPanel.this.mCurPlayerStatus != 4) {
                        PlayWindowPanel.this.notifyPlayWindowClicked();
                    }
                }
            });
        } else {
            this.mPlayWindowView.setFocusable(false);
            this.mPlayWindowView.setClickable(false);
        }
        LogRecordUtils.logd(this.TAG, "<< initViews");
    }

    private void initPlayer(IVideo video, int resultCode) {
        LogRecordUtils.logd(this.TAG, ">> initPlayer, mCurScreenMode=" + this.mCurScreenMode + ", video=" + video);
        Intent intent = ((Activity) this.mContext).getIntent();
        intent.putExtra("albumdetailvideo", this.mCurVideo);
        intent.putExtra("videoType", SourceType.COMMON);
        intent.putExtra("albumInfo", video.getAlbum());
        intent.putExtra("detailorigenalalbum", this.mAlbumInfo.getAlbum());
        PlayParams playParams = (PlayParams) intent.getSerializableExtra("play_list_info");
        if (playParams != null) {
            if (playParams.sourceType == SourceType.BO_DAN && playParams.isPicVertical) {
                playParams.sourceType = SourceType.COMMON;
            }
            if (playParams.sourceType != null) {
                intent.putExtra("videoType", playParams.sourceType);
            } else {
                playParams.sourceType = SourceType.COMMON;
                intent.putExtra("videoType", SourceType.COMMON);
            }
            LogRecordUtils.logd(this.TAG, "initPlayer: plid=" + playParams);
        } else {
            playParams = new PlayParams();
        }
        playParams.isDetailEpisode = true;
        intent.putExtra("play_list_info", playParams);
        intent.putExtra(PlayerIntentConfig2.INTENT_PARAM_PAGENAME, "detail");
        intent.putExtra(PlayerIntentConfig2.INTENT_PARAM_RESULT_CODE, resultCode);
        WindowZoomRatio windowZoomRatio = new WindowZoomRatio(true, WindowZoomRatio.WINDOW_ZOOM_RATIO_16_BY_9_SMALL);
        this.mIsPausedByUser = false;
        this.mMultiEventHelper = new MultiEventHelper();
        this.mPlayer = GetInterfaceTools.getGalaVideoPlayerGenerator().createVideoPlayer(this.mContext, this.mVideoContainer, intent.getExtras(), this.mVideoStateListener, ScreenMode.WINDOWED, this.mPlayWindowUIParams, windowZoomRatio, this.mMultiEventHelper, this.mOnSpecialEventListener);
        if (this.mCurScreenMode == ScreenMode.FULLSCREEN) {
            this.mPlayer.changeScreenMode(this.mCurScreenMode);
        }
        hideAlbumImage();
        hideVideoSourceImage();
        getMainViews();
        LogRecordUtils.logd(this.TAG, "<< initPlayer end");
    }

    private void adjustLayoutParams() {
        LogRecordUtils.logd(this.TAG, ">> adjustLayoutParams");
        Rect bgPadding = UiHelper.getBgDrawablePaddings(this.mPlayWindowView.getBackground());
        MarginLayoutParams playWindowParams = (MarginLayoutParams) this.mPlayWindowView.getLayoutParams();
        int playWindowWidth = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_569dp);
        int playWindowHeiht = (playWindowWidth * 9) / 16;
        int playWindowMarginLeft = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_56dp);
        int playWindowMarginTop = ResourceUtil.getDimensionPixelSize(R.dimen.detail_scroll_top_margin);
        playWindowParams.width = (bgPadding.left + playWindowWidth) + bgPadding.right;
        playWindowParams.height = (bgPadding.top + playWindowHeiht) + bgPadding.bottom;
        playWindowParams.leftMargin = playWindowMarginLeft - bgPadding.left;
        playWindowParams.topMargin = 0;
        this.mPlayWindowView.setLayoutParams(playWindowParams);
        View topPanel = this.mRootView.findViewById(R.id.share_detail_top_panel);
        if (topPanel != null) {
            ViewGroup.LayoutParams topPanelParams = topPanel.getLayoutParams();
            if (topPanelParams != null) {
                topPanelParams.height = (bgPadding.top + playWindowHeiht) + bgPadding.bottom;
                this.mRootView.setLayoutParams(topPanelParams);
            }
        }
        LayoutParams layoutParams = (LayoutParams) this.mWindowImageView.getLayoutParams();
        layoutParams.width = playWindowWidth;
        layoutParams.height = playWindowHeiht;
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        this.mWindowImageView.setLayoutParams(layoutParams);
        this.mPlayWindowUIParams = new LayoutParams(playWindowWidth, playWindowHeiht);
        this.mPlayWindowUIParams.leftMargin = playWindowMarginLeft;
        this.mPlayWindowUIParams.topMargin = bgPadding.top + playWindowMarginTop;
        LogRecordUtils.logd(this.TAG, "<< adjustLayoutParams playWindowWidth=" + playWindowWidth + ", playWindowHeiht" + playWindowHeiht + ", playWindowMarginLeft=" + playWindowMarginLeft + ", playWindowMarginTop=" + playWindowMarginTop);
    }

    private void showAlbumImage() {
        LogRecordUtils.logd(this.TAG, ">> showAlbumImage");
        this.mWindowBlackBG.setVisibility(0);
        this.mWindowImageView.setVisibility(0);
        String oriUrl = DataHelper.getVideoImageURL(this.mAlbumInfo);
        String realUrl = PicSizeUtils.getUrlWithSize(PhotoSize._480_270, oriUrl);
        LogRecordUtils.logd(this.TAG, "loadDetailImage: oriUrl=" + oriUrl + ", realUrl=" + realUrl);
        ImageViewUtils.updateImageView(this.mWindowImageView, realUrl, this.mUiHandler);
    }

    private void loadAlbumImage() {
        LogRecordUtils.logd(this.TAG, ">> loadAlbumImage");
        String oriUrl = DataHelper.getVideoImageURL(this.mAlbumInfo);
        String realUrl = PicSizeUtils.getUrlWithSize(PhotoSize._480_270, oriUrl);
        LogRecordUtils.logd(this.TAG, "loadDetailImage: oriUrl=" + oriUrl + ", realUrl=" + realUrl);
        ImageViewUtils.updateImageView(this.mWindowImageView, realUrl, this.mUiHandler);
    }

    private void hideAlbumImage() {
        LogRecordUtils.logd(this.TAG, ">> hideAlbumImage");
        if (this.mEnableWindowPlay) {
            this.mWindowBlackBG.setVisibility(8);
            this.mWindowImageView.setVisibility(8);
            this.mWindowImageView.setImageResource(0);
        }
    }

    private void showVideoSourceImage() {
        String qpId = this.mCurVideo != null ? this.mCurVideo.getAlbumId() : "";
        LogRecordUtils.logd(this.TAG, ">> showVideoSourceImage, qpId=" + qpId);
        Bitmap bitmap = PlayerUIHelper.getVideoDeriveBitmap(qpId);
        LogRecordUtils.logd(this.TAG, "showVideoSourceImage bitmap=" + bitmap);
        if (bitmap != null) {
            this.mImgVideoSource.setAlpha(0.85f);
            this.mImgVideoSource.setImageBitmap(bitmap);
            this.mImgVideoSource.setVisibility(0);
        }
    }

    private void hideVideoSourceImage() {
        LogRecordUtils.logd(this.TAG, ">> hideVideoSourceImage");
        this.mImgVideoSource.setVisibility(8);
        this.mImgVideoSource.setImageBitmap(null);
    }

    private void getMainViews() {
        LogRecordUtils.logd(this.TAG, ">> getMainViews");
        this.mMovieVideoView = findMovieView();
        this.mGalaVideoView = findPlayerView();
    }

    private MovieVideoView findMovieView() {
        LogRecordUtils.logd(this.TAG, ">> findMovieView");
        if (this.mPlayer != null) {
            return (MovieVideoView) this.mPlayer.getVideoOverlay().getVideoSurfaceView();
        }
        LogRecordUtils.logd(this.TAG, "findMovieView, mPlayer is null!!!!");
        return null;
    }

    private GalaPlayerView findPlayerView() {
        LogRecordUtils.logd(this.TAG, ">> findPlayerView");
        View view = this.mVideoContainer.getChildAt(0);
        if (view == null) {
            LogRecordUtils.logd(this.TAG, "findPlayerView, mVideoContainer has no child.");
            return null;
        } else if (view instanceof GalaPlayerView) {
            return (GalaPlayerView) view;
        } else {
            LogRecordUtils.logd(this.TAG, "findPlayerView, child is not instance of PlayerView, child=" + view);
            return null;
        }
    }

    private void updatePlayerStatus(int status) {
        LogRecordUtils.logd(this.TAG, "updatePlayerStatus, status=" + status);
        this.mCurPlayerStatus = status;
    }

    private void updateScreenMode(ScreenMode newMode) {
        LogRecordUtils.logd(this.TAG, "updateScreenMode, mCurScreenMode=" + this.mCurScreenMode + ", newMode=" + newMode);
        this.mCurScreenMode = newMode;
    }

    public ScreenMode getPlayerScreenMode() {
        return this.mCurScreenMode;
    }

    public Notify onPhoneSync() {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.onPhoneSync() : null;
    }

    public boolean onSeekChanged(long newPosition) {
        return this.mMultiEventHelper != null && this.mMultiEventHelper.onSeekChanged(newPosition);
    }

    public boolean onResolutionChanged(String newRes) {
        return this.mMultiEventHelper != null && this.mMultiEventHelper.onResolutionChanged(newRes);
    }

    public long getPlayPosition() {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.getPlayPosition() : 0;
    }

    public boolean onKeyChanged(int keycode) {
        return this.mMultiEventHelper != null && this.mMultiEventHelper.onKeyChanged(keycode);
    }

    public List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> actions) {
        if (this.mMultiEventHelper == null) {
            return actions;
        }
        if (this.mCurVideo.getAlbum().isSourceType()) {
            actions = this.mMultiEventHelper.getSupportedVoicesWithoutPreAndNext(actions);
            LogRecordUtils.logd(this.TAG, "mMultiEventHelper.getSupportedVoicesWithoutPreAndNext()");
            return actions;
        }
        actions = this.mMultiEventHelper.getSupportedVoices(actions);
        LogRecordUtils.logd(this.TAG, "mMultiEventHelper.getSupportedVoices()");
        return actions;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getKeyCode() < 7 || event.getKeyCode() > 16) {
            if (this.mPlayer == null || !this.mPlayer.handleKeyEvent(event)) {
                return false;
            }
            return true;
        } else if (this.mMultiEventHelper != null) {
            if (this.mKeyValue.getReservedKeys().size() < 1) {
                this.mMultiEventHelper.onGetSceneAction(this.mKeyValue);
                this.mRCMultiKeyEventUtils = RCMultiKeyEventUtils.getInstance();
                this.mRCMultiKeyEventUtils.initialize(this.mContext.getApplicationContext(), this.mKeyValue);
            }
            if (!this.mCurVideo.isTvSeries() || this.mCurScreenMode != ScreenMode.FULLSCREEN || this.mPlayer == null || this.mPlayer.getVideo() == null || this.mPlayer.getVideo().getAlbum() == null) {
                return true;
            }
            this.mRCMultiKeyEventUtils.onKeyDown(event, this.mPlayer.getVideo().getAlbum().order);
            return true;
        } else {
            LogRecordUtils.logd(this.TAG, "mMultiEventHelper is null , event = " + event.toString());
            return true;
        }
    }

    public View getPlayWindowView() {
        return this.mPlayWindowView;
    }

    public void setOnPlayWindowClickedListener(OnPlayWindowClickedListener listener) {
        this.mPlayWindowClickedListener = listener;
    }

    private void notifyPlayWindowClicked() {
        if (this.mPlayWindowClickedListener != null) {
            this.mPlayWindowClickedListener.onPlayWindowClicked();
        }
    }

    private void sendPlayWindowClickPingback() {
        LogRecordUtils.logd(this.TAG, ">> sendPlayWindowClickPingback.");
        if (this.mCurVideo == null) {
            LogRecordUtils.logd(this.TAG, "sendPlayWindowClickPingback null == mVideoData");
            return;
        }
        PingbackFactory.instance().createPingback(9).addItem(PingbackStore.R.R_TYPE(this.mCurVideo.getAlbum().qpId)).addItem(this.mPingbackContext.getItem("block")).addItem(RTTYPE.RT_I).addItem(RSEATTYPE.PLAYER).addItem(RPAGETYPE.DETAIL).addItem(C1.C1_TYPE(String.valueOf(this.mCurVideo.getAlbum().chnId))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_QPID.NOW_QPID_TYPE(this.mCurVideo.getAlbumId())).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mCurVideo.getChannelId()))).post();
    }
}
