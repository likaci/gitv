package com.gala.video.app.player.ui.overlay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.Calendar;

@SuppressLint({"HandlerLeak"})
public class MediaControllerOverlay extends AbsMediaController {
    private static final int MSG_UPDATETIME = 3;
    private static final String TAG_S = "Player/Ui/MediaControllerOverlay";
    private static Calendar sCalendar;
    private String TAG = (TAG_S + hashCode());
    private String mBitStream;
    protected Context mContext;
    protected AbsMediaControllerStrategy mFullScreenState;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    MediaControllerOverlay.this.mMediaControllerStrategy.handleUpdateTimeMessage();
                    if (MediaControllerOverlay.this.mIsRegisterTimeTick) {
                        if (MediaControllerOverlay.sCalendar == null) {
                            MediaControllerOverlay.sCalendar = Calendar.getInstance();
                        }
                        MediaControllerOverlay.this.mHandler.sendEmptyMessageDelayed(3, (long) ((60 - MediaControllerOverlay.sCalendar.get(13)) * 1000));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mInitized;
    private boolean mIs3D;
    private boolean mIsFullScreen;
    private boolean mIsRegisterTimeTick = false;
    protected IMediaControllerStrategy mMediaControllerStrategy;
    private OnAdStateListener mOnAdStateListener;
    private float mRatio;
    protected View mRoot;
    private TipWrapper mTip;
    private IVideo mVideo;
    protected AbsMediaControllerStrategy mWindowState;

    public void onSeekBegin(View view, int i) {
        if (this.mInitized) {
            this.mMediaControllerStrategy.onSeekBegin(view, i);
        }
    }

    public void onProgressChanged(View view, int i) {
    }

    public void onSeekEnd(View view, int i) {
        if (this.mInitized) {
            this.mMediaControllerStrategy.onSeekEnd(view, i);
        }
    }

    public MediaControllerOverlay(Context context) {
        super(context);
        this.mContext = context;
        this.TAG = TAG_S + hashCode();
        initOverlay();
    }

    public MediaControllerOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.TAG = TAG_S + hashCode();
        initOverlay();
    }

    protected int getLayoutId() {
        return PlayerAppConfig.getMediaPlayerLayoutId();
    }

    protected void initOverlay() {
        this.mRoot = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(getLayoutId(), this);
        this.mFullScreenState = PlayerAppConfig.getFullScreenStrategy();
        this.mWindowState = PlayerAppConfig.getWindowStrategy();
        this.mMediaControllerStrategy = this.mFullScreenState;
    }

    private void initView() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "initView()");
        }
        this.mInitized = true;
        this.mFullScreenState.initView(this.mContext, this.mRoot);
        this.mFullScreenState.setOnAdStateListener(this.mOnAdStateListener);
        this.mWindowState.initView(this.mContext, this.mRoot);
        this.mMediaControllerStrategy.setThreeDimensional(this.mIs3D);
        this.mMediaControllerStrategy.updateBitStreamDefinition(this.mBitStream);
        this.mMediaControllerStrategy.setVideo(this.mVideo);
        if (this.mTip != null) {
            this.mMediaControllerStrategy.showTip(this.mTip);
        }
        switchScreen(this.mIsFullScreen, this.mRatio);
    }

    protected void onAttachedToWindow() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onAttachedToWindow");
        }
        super.onAttachedToWindow();
        this.mIsRegisterTimeTick = true;
        this.mHandler.sendEmptyMessage(3);
    }

    protected void onDetachedFromWindow() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onDetachedFromWindow");
        }
        super.onDetachedFromWindow();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mIsRegisterTimeTick = false;
        this.mFullScreenState.clearAllMessage();
        this.mWindowState.clearAllMessage();
    }

    public void setSubtitle(String subtitle) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setSubtitle:" + subtitle);
        }
        if (this.mInitized) {
            this.mMediaControllerStrategy.setSubtitle(subtitle);
        }
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setMaxProgress maxProgress=" + maxProgress + ",maxSeekableProgress=" + maxSeekableProgress);
        }
        this.mMediaControllerStrategy.setMaxProgress(maxProgress, maxSeekableProgress);
    }

    public void setHeadAndTailProgress(int headProgress, int tailProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setHeadAndTailProgress=" + headProgress + "/" + tailProgress);
        }
        this.mMediaControllerStrategy.setHeadAndTailProgress(headProgress, tailProgress);
    }

    public void setProgress(int progress) {
        this.mMediaControllerStrategy.setProgress(progress);
    }

    public void setSecondaryProgress(int percent) {
        this.mMediaControllerStrategy.setSecondaryProgress(percent);
    }

    public int getProgress() {
        return this.mMediaControllerStrategy.getProgress();
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showPlaying(" + simple + ")");
        }
        if (!this.mInitized) {
            initView();
        }
        this.mMediaControllerStrategy.showPlaying(simple);
    }

    public void showPaused() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showPaused()");
        }
        this.mMediaControllerStrategy.showPaused();
    }

    public void showAdPlaying(int countDownTime) {
        if (!this.mInitized) {
            initView();
        }
        this.mMediaControllerStrategy.showAdPlaying(countDownTime);
    }

    public void showMiddleAdEnd() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showMiddleAdEnd()");
        }
        this.mMediaControllerStrategy.showMiddleAdEnd();
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hide()");
        }
        if (this.mInitized) {
            this.mMediaControllerStrategy.hide();
        }
        this.mTip = null;
        sCalendar = null;
    }

    public void showBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showBuffering()");
        }
        this.mMediaControllerStrategy.showBuffering();
    }

    public void hideBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hideBuffering()");
        }
        this.mMediaControllerStrategy.hideBuffering();
    }

    public void setBufferPercent(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setBufferPercent(" + percent + ")");
        }
        this.mMediaControllerStrategy.setBufferPercent(percent);
    }

    public void setNetSpeed(long bytePerSecond) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setNetSpeed:" + bytePerSecond);
        }
        this.mMediaControllerStrategy.setNetSpeed(bytePerSecond);
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showTip:" + tip);
        }
        this.mTip = tip;
        if (this.mInitized) {
            this.mMediaControllerStrategy.showTip(tip);
        }
    }

    public void hideTip() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hideTip");
        }
        this.mTip = null;
        if (this.mInitized) {
            this.mMediaControllerStrategy.hideTip();
        }
    }

    public void showPanel(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showPanel:" + duration);
        }
    }

    public void setThreeDimensional(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setThreeDimensional:" + enable);
        }
        if (enable) {
            this.mIs3D = enable;
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "switchScreen() isFullScreen = " + isFullScreen);
        }
        this.mIsFullScreen = isFullScreen;
        this.mRatio = zoomRatio;
        if (this.mInitized) {
            switchStrategy(isFullScreen ? ScreenMode.FULLSCREEN : ScreenMode.WINDOWED);
            this.mMediaControllerStrategy.switchScreen(isFullScreen, zoomRatio);
        }
    }

    private void switchStrategy(ScreenMode mode) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "switchStrategy" + mode);
        }
        MediaControllerStateBean stateBean = this.mMediaControllerStrategy.getMediaControllerBean();
        if (this.mMediaControllerStrategy == this.mWindowState) {
            this.mMediaControllerStrategy.clearHideViewMessageQueue();
        }
        this.mMediaControllerStrategy = mode == ScreenMode.FULLSCREEN ? this.mFullScreenState : this.mWindowState;
        if (mode == ScreenMode.WINDOWED) {
            stateBean.setTipShown(false);
        }
        this.mMediaControllerStrategy.setMediaControllerBean(stateBean);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mInitized) {
            return this.mMediaControllerStrategy.dispatchKeyEvent(event);
        }
        return false;
    }

    public void updateBitStreamDefinition(String bitStream) {
        this.mBitStream = bitStream;
        if (this.mInitized) {
            this.mMediaControllerStrategy.updateBitStreamDefinition(bitStream);
        }
    }

    public void clearMediaControllerState() {
        this.mMediaControllerStrategy.clearMediaControllerState();
    }

    public void showBottomAndTop(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showBottomAndTop(" + duration + ")");
        }
        this.mMediaControllerStrategy.showBottomAndTop(duration);
    }

    public void hideBottomAndTop(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hideBottomAndTop(" + duration + ")");
        }
        this.mMediaControllerStrategy.hideBottomAndTop(duration);
    }

    public void showVolumePanel(int currentCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showVolumePanel() currentCount " + currentCount);
        }
        this.mMediaControllerStrategy.showVolumePanel(currentCount);
    }

    public void hideVolumePanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hideVolumePanel()");
        }
        this.mMediaControllerStrategy.hideVolumePanel();
    }

    public void showBrightnessPanel(int lightCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showBrightnessPanel() lightCount = " + lightCount);
        }
        this.mMediaControllerStrategy.showBrightnessPanel(lightCount);
    }

    public void hideBrightnessPanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hideBrightnessPanel()");
        }
        this.mMediaControllerStrategy.hideBrightnessPanel();
    }

    public void showPlayOverFlow(boolean forward, long distance) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showPlayOverFlow() forward (" + forward + ",distance " + distance + ")");
        }
        this.mMediaControllerStrategy.showPlayOverFlow(forward, distance);
    }

    public void hidePlayOverFlow(boolean forward, long distance) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hidePlayOverFlow() forward (" + forward + ",distance " + distance + ")");
        }
        this.mMediaControllerStrategy.hidePlayOverFlow(forward, distance);
    }

    public boolean showSeekBar() {
        boolean isShow = this.mMediaControllerStrategy.showSeekBar();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showSeekBar() (" + isShow + ")");
        }
        return isShow;
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setVideo()");
        }
        this.mVideo = video;
        if (this.mInitized) {
            this.mMediaControllerStrategy.setVideo(video);
        }
    }

    public void clearAd() {
        this.mMediaControllerStrategy.clearAd();
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
        this.mOnAdStateListener = listener;
    }

    public void setPrimary(boolean b) {
        this.mFullScreenState.setPrimary(b);
        this.mWindowState.setPrimary(b);
    }

    public void hideGuideTip() {
        this.mMediaControllerStrategy.hideGuideTip();
    }
}
