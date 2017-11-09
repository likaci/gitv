package com.gala.video.app.player.ui.carousel;

import android.content.res.Resources.NotFoundException;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.ITip;
import com.gala.sdk.player.OnFullScreenHintChangedListener;
import com.gala.sdk.player.OnUserChannelChangeListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.ui.OnPageAdvertiseStateChangeListener;
import com.gala.sdk.player.ui.OnRequestChannelInfoListener;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.overlay.AbsPlayerOverlay;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel.PlayerErrorPanelInfo;
import com.gala.video.app.player.ui.widget.CarouselFullScreenHint;
import com.gala.video.app.player.ui.widget.GalaPlayerView;
import com.gala.video.app.player.ui.widget.views.CarouselLoadingView;
import com.gala.video.app.player.utils.UiUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.IErrorHandler.ErrorType;
import java.util.Iterator;
import java.util.List;

public class CarouselPlayerOverlay extends AbsPlayerOverlay {
    private static final SparseArray<String> KEY_MAP = new SparseArray();
    private static final String TAG_S = "Player/Ui/CarouselPlayerOverlay";
    private final String TAG = ("Player/Ui/CarouselPlayerOverlay@" + Integer.toHexString(hashCode()));
    private CarouselMediaControllerOverlay mCarouselMediaController;
    private boolean mIsShowError;
    private CarouselLoadingView mLoadingView;
    private OnCarouselPanelHideListener mOnCarouselHideListener = new C14632();
    private onChannelChangeListener mOnChannelChangeListener = new C14621();
    private final PostShowBufferingRunnable mPostShowBufferingRunnable = new PostShowBufferingRunnable();
    private String mTempName;

    interface onChannelChangeListener {
        void onChannelChange(TVChannelCarousel tVChannelCarousel, boolean z);
    }

    class C14621 implements onChannelChangeListener {
        C14621() {
        }

        public void onChannelChange(TVChannelCarousel channelCarousel, boolean needRequest) {
            CarouselPlayerOverlay.this.mCarouselMediaController.updateChannelInfo(channelCarousel, needRequest);
        }
    }

    class C14632 implements OnCarouselPanelHideListener {
        C14632() {
        }

        public void hide() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselPlayerOverlay.this.TAG, "OnCarouselPanelHideListener hide()");
            }
            if (CarouselPlayerOverlay.this.mFullScreenHint instanceof CarouselFullScreenHint) {
                ((CarouselFullScreenHint) CarouselPlayerOverlay.this.mFullScreenHint).dismissHint(null);
            }
        }
    }

    private class PostShowBufferingRunnable implements Runnable {
        private PostShowBufferingRunnable() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselPlayerOverlay.this.TAG, "mPostShowBufferingRunnable.run()");
            }
            CarouselPlayerOverlay.this.mCarouselMediaController.showBuffering();
        }
    }

    public CarouselPlayerOverlay(GalaPlayerView playerView, boolean initByWindowMode, float zoomRatio) {
        LogUtils.m1568d(this.TAG, " CarouselPlayerOverlay.<init>: initByWindowMode=" + initByWindowMode);
        this.mPlayerView = playerView;
        initOverlay(playerView);
        switchScreenMode(!initByWindowMode, zoomRatio);
    }

    protected void initOverlay(GalaPlayerView playerView) {
        super.initOverlay(playerView);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initOverlay()");
        }
        this.mCarouselMediaController = this.mPlayerView.getCarouselMediaController();
        this.mLoadingView = this.mPlayerView.getCarouselLoadingView();
        this.mCarouselMediaController.setOnChannelChangeListener(this.mOnChannelChangeListener);
        this.mCarouselMediaController.setOnUserVideoChangeListener(this.mVideoChangeListener);
        this.mCarouselMediaController.setOnCarosuelPanelHideListener(this.mOnCarouselHideListener);
        this.mCarouselMediaController.setAdStateListener(this.mOnAdStateListener);
        this.mScreenSwitchers.add(this.mLoadingView);
        this.mScreenSwitchers.add(this.mCarouselMediaController);
        this.mScreenSwitchers.add(this.mFullScreenHint);
    }

    public void notifyVideoDataChanged(int data) {
    }

    public void setCurrentVideo(IVideo movie) {
        super.setCurrentVideo(movie);
        this.mCarouselMediaController.setVideo(this.mVideo);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setMovie() mVideo=" + this.mVideo);
        }
        this.mCarouselMediaController.clearAd();
    }

    public void changeScreenMode(boolean isFullScreen, float zoomRatio) {
        switchScreenMode(isFullScreen, zoomRatio);
        if (!isFullScreen && this.mVideo != null) {
            this.mMenuPanel.hide();
        }
    }

    private void switchScreenMode(boolean isFullScreen, float zoomRatio) {
        LogUtils.m1568d(this.TAG, "switchScreenMode isFullScreen=" + isFullScreen);
        this.mIsFullScreenMode = isFullScreen;
        Iterator it = this.mScreenSwitchers.iterator();
        while (it.hasNext()) {
            ((IScreenUISwitcher) it.next()).switchScreen(isFullScreen, zoomRatio);
        }
        showErrorPanel();
    }

    public boolean isLoadingViewShown() {
        return false;
    }

    public void clearError() {
        super.clearError();
        this.mIsShowError = false;
    }

    public void showMiddleAdPlaying(int arg0) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showMiddleAdPlaying()");
        }
        this.mState = 1004;
    }

    public void setAllTagList(List<TVChannelCarouselTag> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setAllTagList() list=" + (list == null ? -1 : list.size()));
        }
        this.mCarouselMediaController.setAllTagList(list);
    }

    public void hide() {
        LogUtils.m1568d(this.TAG, "hide()");
        this.mMenuPanel.hide();
        this.mLoadingView.hide();
        this.mCarouselMediaController.hide();
    }

    public void showFullScreenHint(FullScreenHintType hintType) {
        LogUtils.m1568d(this.TAG, "showFullScreenHint");
        this.mFullScreenHint.show(hintType);
    }

    public void hideLoadingView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hideLoadingView()");
        }
        this.mLoadingView.hide();
    }

    public View getContentView() {
        return null;
    }

    public void setOnFullScreenHintChangedListener(OnFullScreenHintChangedListener listener) {
        LogUtils.m1568d(this.TAG, "showFullScreenHintIfNeeded carousel");
        this.mFullScreenHint.setHintListener(listener);
    }

    public void showAdPlaying(int countDownTime) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showAdPlaying()");
        }
        this.mLoadingView.hide();
        this.mState = 1004;
    }

    private void showErrorPanel() {
        if (this.mIsShowError) {
            View errorView = this.mErrorPanel.getView();
            if (errorView.getParent() == null) {
                this.mPlayerView.addView(errorView, -1, -1);
            }
            PlayerErrorPanelInfo info = UiUtils.getPlayerErrorPanelInfoForCarousel();
            this.mErrorPanel.show(UiUtils.getPlayerErrorPanelUIConfigForCarousel(), info);
        }
    }

    public void showLoading(String str) {
        this.mLoadingView.show();
        this.mLoadingView.startLoadingAnim();
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showPlaying(" + simple + ")");
        }
        this.mLoadingView.showPlaying();
        this.mCarouselMediaController.showPlaying(simple);
        this.mState = 1000;
    }

    public void showStopped() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showStoped()");
        }
        this.mMenuPanel.hide();
        this.mState = 1003;
    }

    public int getProgress() {
        return 0;
    }

    public void hideBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hideBuffering()");
        }
        this.mHandler.removeCallbacks(this.mPostShowBufferingRunnable);
        this.mCarouselMediaController.hideBuffering();
    }

    public void showError(ErrorType errorType, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showError(" + msg + ")");
        }
        this.mMenuPanel.hide();
        this.mLoadingView.hide();
        this.mIsShowError = true;
        showErrorPanel();
    }

    public void updateBufferProgress(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "updateBufferProgress(" + percent + ")");
        }
        setBufferPercent(percent);
    }

    public void updateNetSpeed(long bytePerSecond) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "updateNetSpeed(" + bytePerSecond + ")");
        }
        setNetSpeed(bytePerSecond);
    }

    public void setBufferPercent(int percent) {
        this.mCarouselMediaController.setBufferPercent(percent);
    }

    public void setNetSpeed(long speed) {
        this.mCarouselMediaController.setNetSpeed(speed);
    }

    public void showBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showBuffering()");
        }
        if (!this.mLoadingView.isShown()) {
            this.mCarouselMediaController.showBuffering();
        }
    }

    public void hideTip() {
        this.mCarouselMediaController.hideTip();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.m1568d(this.TAG, "dispatchKeyEvent: " + keyEventToString(event));
        sendKeyEventPingback(event);
        if (isInFullScreenMode()) {
            int thrtype;
            if (this.mFullScreenHint.isShown()) {
                LogUtils.m1568d(this.TAG, "dispatchKeyEvent: fullscreen hint consumed");
                this.mFullScreenHint.dispatchKeyEvent(event);
            }
            int keyCode = event.getKeyCode();
            boolean isFirstDownEvent;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                isFirstDownEvent = true;
            } else {
                isFirstDownEvent = false;
            }
            switch (keyCode) {
                case 4:
                    if (this.mState == 1004 && this.mAdController != null) {
                        thrtype = getClickThroughAdType();
                        if (thrtype == 102 || thrtype == 101 || getClickThroughAdType() == 100) {
                            this.mAdController.hideAd(100);
                            return true;
                        }
                    }
                    if (this.mMenuPanel.isShown() && isFirstDownEvent) {
                        this.mMenuPanel.hide();
                        return true;
                    }
                case 22:
                    if (!(this.mCarouselMediaController.isShown() || this.mMenuPanel.isShown() || this.mAdController == null || !this.mAdController.isEnabledClickThroughAd() || getClickThroughAdType() == 101 || getClickThroughAdType() == 102 || getClickThroughAdType() == 100)) {
                        this.mAdController.showAd(100);
                        this.mCarouselMediaController.hideTip();
                        this.mCarouselMediaController.hide();
                        return true;
                    }
                case 82:
                    if (this.mMenuPanel.isShown() && isFirstDownEvent) {
                        this.mMenuPanel.hide();
                        return true;
                    } else if (!(this.mLoadingView.isShown() || this.mIsShowError || !isFirstDownEvent || this.mState == 1004)) {
                        setAdData();
                        this.mMenuPanel.show(82);
                        this.mCarouselMediaController.hide();
                        return true;
                    }
                    break;
            }
            if (this.mState == 1004) {
                thrtype = getClickThroughAdType();
                if (thrtype == 101 || thrtype == 100) {
                    if (getShowThroughState() == 201) {
                        return true;
                    }
                    if (getShowThroughState() == 202 && thrtype == 100) {
                        if (keyCode == 24 || keyCode == 25 || keyCode == 164) {
                            return false;
                        }
                        return true;
                    } else if (keyCode == 23 || keyCode == 66) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (thrtype == 102) {
                    if (keyCode == 24 || keyCode == 25 || keyCode == 164) {
                        return false;
                    }
                    return true;
                }
            }
            if (!this.mMenuPanel.isShown()) {
                return this.mCarouselMediaController.dispatchKeyEvent(event);
            }
            if (!this.mLoadingView.isShown()) {
                return false;
            }
            LogUtils.m1568d(this.TAG, "dispatchKeyEvent: loading view blocks keys");
            if (keyCode == 4 || keyCode == 24 || keyCode == 25) {
                return false;
            }
            return true;
        }
        LogUtils.m1577w(this.TAG, "dispatchKeyEvent: not in fullscreen mode, not handled");
        return false;
    }

    private int getShowThroughState() {
        if (this.mAdController != null) {
            List state = this.mAdController.getShowThroughState();
            if (!ListUtils.isEmpty(state)) {
                return ((Integer) state.get(0)).intValue();
            }
        }
        return -1;
    }

    private String keyEventToString(KeyEvent event) {
        String keyAction;
        StringBuilder builder = new StringBuilder();
        String keyName = (String) KEY_MAP.get(event.getKeyCode());
        if (keyName == null) {
            keyName = "[" + event.getKeyCode() + AlbumEnterFactory.SIGN_STR;
        }
        builder.append("key event: (").append(keyName + ", ");
        switch (event.getAction()) {
            case 0:
                keyAction = ScreenSaverPingBack.SEAT_KEY_DOWN;
                break;
            case 1:
                keyAction = ScreenSaverPingBack.SEAT_KEY_UP;
                break;
            case 2:
                keyAction = "multiple";
                break;
            default:
                keyAction = "<unknown>";
                break;
        }
        builder.append(keyAction + ", ");
        builder.append("focused view={");
        View focusView = this.mPlayerView.findFocus();
        if (focusView != null) {
            String resName;
            try {
                resName = this.mContext.getResources().getResourceEntryName(focusView.getId());
            } catch (NotFoundException e) {
                resName = "[" + focusView.getId() + AlbumEnterFactory.SIGN_STR;
            }
            builder.append(focusView.getClass().toString()).append(", ").append(resName);
        } else {
            builder.append("NULL");
        }
        builder.append("}");
        return builder.toString();
    }

    static {
        KEY_MAP.put(82, "MENU");
        KEY_MAP.put(4, "BACK");
        KEY_MAP.put(3, "HOME");
        KEY_MAP.put(23, "DPAD_CENTER");
        KEY_MAP.put(21, "DPAD_LEFT");
        KEY_MAP.put(22, "DPAD_RIGHT");
        KEY_MAP.put(19, "DPAD_UP");
        KEY_MAP.put(20, "DPAD_DOWN");
        KEY_MAP.put(25, "VOLUME_DOWN");
        KEY_MAP.put(24, "VOLUME_UP");
    }

    public void setOnRequestChannelInfoListener(OnRequestChannelInfoListener listener) {
        this.mCarouselMediaController.setOnRequestChannelInfoListener(listener);
    }

    public void setOnUserChannelChangeListener(OnUserChannelChangeListener listener) {
        this.mCarouselMediaController.setOnUserChannelChangeListener(listener);
    }

    public void setCurrentChannel(TVChannelCarousel channelCarousel) {
        this.mCarouselMediaController.setCurrentChannel(channelCarousel);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setCurrentChannel double??=" + channelCarousel);
        }
        if (channelCarousel != null) {
            CharSequence name = channelCarousel.name;
            if (!StringUtils.isEmpty(name) && !name.equals(this.mTempName)) {
                this.mLoadingView.setCurrentChannel(channelCarousel);
                this.mTempName = name;
            }
        }
    }

    public void notifyChannelInfoChange(int chnChangeOffset, boolean needRequestAll, boolean realChannelChange) {
    }

    public void notifyChannelChangeByIndex(int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyChannelChangeByIndex() index=" + index);
        }
        this.mCarouselMediaController.notifyChannelChangeByIndex(index);
    }

    public void hideFullScreenHint() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hideFullScreenHint()");
        }
        if (this.mFullScreenHint instanceof CarouselFullScreenHint) {
            ((CarouselFullScreenHint) this.mFullScreenHint).dismissHint(null);
        }
    }

    public void showTip(ITip tip) {
        super.showTip(tip);
        if (this.mTip != null) {
            this.mCarouselMediaController.showTip(this.mTip);
        }
    }

    public void setOnPageAdvertiseStateChangeListener(OnPageAdvertiseStateChangeListener listener) {
        super.setOnPageAdvertiseStateChangeListener(listener);
    }

    protected String getBlock(boolean isUgcThrow) {
        String block = "";
        if (this.mCarouselMediaController.isChannelInfoShown()) {
            return "playing_infopanel";
        }
        if (this.mState == 1000) {
            return "playing";
        }
        return block;
    }

    protected String getSubRSeatByKeyEvent(KeyEvent event) {
        int code = event.getKeyCode();
        if (this.mCarouselMediaController.isChannelInfoShown() || this.mCarouselMediaController.isChannelListShown() || this.mMenuPanel.isShown()) {
            if (!(code == 4 || code == 82)) {
                return "";
            }
        } else if (this.mState == 1000 && code == 4) {
            return "";
        }
        return null;
    }

    public void showMiddleAdEnd() {
    }

    public void showLoading(String message, long delayMs) {
    }

    public void updateAdCountDown(int countDownTime) {
    }

    public void setAlbumId(String albumId) {
    }

    public void updateBitStreamDefinition(BitStream bitStream) {
    }

    public void clearMediaControllerState() {
    }

    public void setIsShowGallery(boolean b) {
    }

    public void setSourceType(SourceType type) {
    }

    public void showPaused() {
    }

    public void setThreeDimensional(boolean arg0) {
    }

    public void showCompleted() {
    }

    public void setHeadAndTailProgress(int arg0, int arg1) {
    }

    public void setMaxProgress(int arg0, int arg1) {
    }

    public void setProgress(int arg0) {
    }

    public void setSecondaryProgress(int arg0) {
    }

    public void onSeekBegin(View view, int i) {
    }

    public void onProgressChanged(View view, int i) {
    }

    public void onSeekEnd(View view, int i) {
    }

    public void onStop() {
    }

    public void setMaxVolume(int maxVolume) {
    }

    public void setVolume(int volume) {
    }

    public void showTip(CharSequence arg0) {
    }

    public void setSubtitle(String arg0) {
    }
}
