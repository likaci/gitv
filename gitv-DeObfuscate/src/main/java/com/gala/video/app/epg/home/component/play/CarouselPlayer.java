package com.gala.video.app.epg.home.component.play;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;

public class CarouselPlayer {
    public static final int FULL_SCREEN = 1;
    private static final String TAG = "Home/CarouselPlayer";
    public static final int WINDOWED = 0;
    private Context mContext;
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    private onWindowModeSwitchListener mListener;
    private IMultiEventHelper mMultiEventHelper = null;
    private View mPlayBrotherView;
    private FrameLayout mPlayLayout;
    private IGalaVideoPlayer mPlayer;
    private OnPlayerStateChangedListener mPlayerStateListener = new C06041();
    private PlayStatus mPlayerStatus = PlayStatus.IDLE;
    private boolean mShouldHidePlayViewWhenPause = true;
    private LayoutParams mVideoLayoutParams;

    class C06041 implements OnPlayerStateChangedListener {
        C06041() {
        }

        public void onVideoSwitched(IVideo video, int type) {
            LogUtils.m1568d(CarouselPlayer.TAG, "onVideoSwitched video = " + video);
        }

        public void onPlaybackFinished() {
            LogUtils.m1568d(CarouselPlayer.TAG, "on carousel video play finished");
        }

        public void onScreenModeSwitched(ScreenMode newMode) {
            int i = 1;
            LogUtils.m1568d(CarouselPlayer.TAG, "Home/CarouselPlayer---onScreenModeSwitched() newMode=" + newMode);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselPlayer.TAG, "onScreenModeSwitched newMode=" + newMode);
            }
            if (newMode == ScreenMode.WINDOWED) {
                CarouselPlayer.this.mPlayBrotherView.requestFocus();
                CarouselPlayer.this.mShouldHidePlayViewWhenPause = true;
            } else {
                CarouselPlayer.this.mShouldHidePlayViewWhenPause = false;
            }
            CarouselPlayer.this.mCurScreenMode = newMode;
            if (CarouselPlayer.this.mListener != null) {
                onWindowModeSwitchListener access$300 = CarouselPlayer.this.mListener;
                if (CarouselPlayer.this.mCurScreenMode != ScreenMode.FULLSCREEN) {
                    i = 0;
                }
                access$300.onWindowModeSwitched(i);
            }
        }

        public boolean onError(IVideo video, ISdkError error) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselPlayer.TAG, "onError: error=" + error + ", video = " + video);
            }
            LogUtils.m1571e(CarouselPlayer.TAG, "current screen mode is " + CarouselPlayer.this.mCurScreenMode);
            CarouselPlayer.this.mPlayerStatus = PlayStatus.ERROR;
            return false;
        }

        public void onVideoStarted(IVideo video) {
            LogUtils.m1568d(CarouselPlayer.TAG, "onVideoStarted current screen mode : " + CarouselPlayer.this.mCurScreenMode);
        }

        public void onAdStarted() {
            LogUtils.m1568d(CarouselPlayer.TAG, "onAdStarted");
        }

        public void onAdEnd() {
        }

        public void onPrepared() {
        }
    }

    public enum PlayStatus {
        IDLE,
        STARTED,
        PLAYING,
        ERROR,
        PAUSED,
        STOPPED
    }

    public interface onWindowModeSwitchListener {
        void onWindowModeSwitched(int i);
    }

    public CarouselPlayer(FrameLayout PlayContainer, View brotherView, Context context, LayoutParams params) {
        this.mPlayLayout = PlayContainer;
        this.mVideoLayoutParams = params;
        this.mContext = context;
        this.mPlayBrotherView = brotherView;
    }

    public void setOnWindowChangeListener(onWindowModeSwitchListener listener) {
        this.mListener = listener;
    }

    public void startPlay(int screenMode, ChannelCarousel playInfo) {
        this.mPlayLayout.setVisibility(0);
        Bundle bundle = new Bundle();
        PlayParams params = new PlayParams();
        Album album = new Album();
        album.live_channelId = String.valueOf(playInfo.id);
        album.chnName = playInfo.name;
        bundle.putSerializable("videoType", SourceType.CAROUSEL);
        params.sourceType = SourceType.CAROUSEL;
        params.from = "carousel";
        bundle.putSerializable("play_list_info", params);
        if (playInfo.id > 0) {
            TVChannelCarousel tvChannel = new TVChannelCarousel();
            tvChannel.sid = playInfo.tableNo;
            tvChannel.id = playInfo.id;
            tvChannel.name = playInfo.name;
            tvChannel.icon = playInfo.logo;
            bundle.putSerializable("carouselChannel", tvChannel);
        }
        bundle.putSerializable("albumInfo", album);
        bundle.putString("from", "carousel");
        bundle.putString("from", HomePingbackSender.getInstance().getTabName() + "_rec");
        bundle.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, "tab_" + HomePingbackSender.getInstance().getTabName());
        this.mPlayLayout.bringToFront();
        LogUtils.m1568d(TAG, "create carousel video player margin left :" + this.mVideoLayoutParams.leftMargin + " height = " + this.mVideoLayoutParams.width);
        this.mMultiEventHelper = CreateInterfaceTools.createMultiEventHelper();
        this.mPlayer = GetInterfaceTools.getGalaVideoPlayerGenerator().createVideoPlayer(this.mContext, this.mPlayLayout, bundle, this.mPlayerStateListener, ScreenMode.WINDOWED, this.mVideoLayoutParams, new WindowZoomRatio(true, WindowZoomRatio.WINDOW_ZOOM_RATIO_16_BY_9_SMALL), this.mMultiEventHelper, null);
        if (screenMode == 1) {
            this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
        }
        this.mPlayerStatus = PlayStatus.STARTED;
    }

    public boolean isPaused() {
        LogUtils.m1568d(TAG, "video player is paused");
        return this.mPlayerStatus == PlayStatus.PAUSED;
    }

    public void stopPlay(boolean removePreView) {
        LogUtils.m1568d(TAG, "stop carousel player mShouldHidePlayViewWhenPause = " + this.mShouldHidePlayViewWhenPause);
        if (this.mPlayer != null) {
            this.mPlayerStatus = PlayStatus.STOPPED;
            this.mPlayer.release();
            if (this.mShouldHidePlayViewWhenPause || removePreView) {
                this.mPlayLayout.removeAllViews();
            }
            this.mPlayer = null;
        }
    }

    public boolean onKeyEvent(KeyEvent event) {
        LogUtils.m1568d(TAG, "onKeyEvent() keycode=" + event.getKeyCode());
        if (this.mPlayer == null || this.mCurScreenMode == ScreenMode.WINDOWED || this.mPlayer == null) {
            return false;
        }
        boolean result = this.mPlayer.handleKeyEvent(event);
        if (event.getKeyCode() == 82) {
            return true;
        }
        LogUtils.m1568d(TAG, "video player handle key event result " + result);
        return result;
    }

    public void pause() {
        if (this.mPlayer != null && this.mPlayLayout.getChildCount() > 0) {
            LogUtils.m1568d(TAG, "carousel player paused,player is playing = " + this.mPlayer.isPlaying() + ",mShouldHidePlayViewWhenPause = " + this.mShouldHidePlayViewWhenPause);
            ViewGroup child = (ViewGroup) this.mPlayLayout.getChildAt(0);
            if (child != null && this.mShouldHidePlayViewWhenPause) {
                child.setVisibility(8);
            }
            if (this.mPlayer.isPlaying()) {
                this.mPlayer.onUserPause();
            }
            this.mPlayerStatus = PlayStatus.PAUSED;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.m1568d(TAG, "onKeyDown() -> keyCode" + keyCode + ", event" + event);
        if (this.mPlayer == null || !this.mPlayer.handleKeyEvent(event)) {
            return false;
        }
        LogUtils.m1568d(TAG, "key event is cosumed by player keycode=" + keyCode);
        return true;
    }

    public Notify onPhoneSync() {
        if (this.mPlayer == null) {
            return null;
        }
        return this.mMultiEventHelper.onPhoneSync();
    }

    public boolean onKeyChanged(int keycode) {
        if (this.mPlayer == null) {
            return false;
        }
        return this.mMultiEventHelper.onKeyChanged(keycode);
    }

    public void switchToFullScreen() {
        if (this.mPlayer != null) {
            this.mPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
        }
    }

    public void onActionScrollEvent(KeyKind keyKind) {
        if (this.mPlayer != null) {
            LogUtils.m1568d(TAG, "onActionScrollEvent keyKind " + keyKind);
            this.mMultiEventHelper.onDlnaKeyEvent(DlnaKeyEvent.SCROLL, keyKind);
        }
    }

    public void onActionFlingEvent(KeyKind keyKind) {
        if (this.mPlayer != null) {
            LogUtils.m1568d(TAG, "onActionFlingEvent kindKind " + keyKind);
            this.mMultiEventHelper.onDlnaKeyEvent(DlnaKeyEvent.SCROLL, keyKind);
        }
    }

    public boolean isPlayerError() {
        return this.mPlayerStatus == PlayStatus.ERROR;
    }

    public void onErrorClicked() {
        LogUtils.m1568d(TAG, "on player error clicked");
        this.mPlayer.onErrorClicked();
    }

    public boolean isPlay() {
        if (this.mPlayerStatus == PlayStatus.STARTED || this.mPlayerStatus == PlayStatus.PLAYING) {
            return true;
        }
        return false;
    }
}
