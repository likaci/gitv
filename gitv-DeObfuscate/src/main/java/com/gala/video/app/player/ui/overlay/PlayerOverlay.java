package com.gala.video.app.player.ui.overlay;

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
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel.PlayerErrorPanelInfo;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel.PlayerErrorPanelUIConfig;
import com.gala.video.app.player.ui.widget.GalaPlayerView;
import com.gala.video.app.player.ui.widget.views.LoadingView;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.app.player.utils.UiUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.IErrorHandler.ErrorType;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDataUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PlayerOverlay extends AbsPlayerOverlay {
    private static final HashMap<Integer, String> BANDWIDTH_HINT_MAP = new HashMap();
    private static final SparseArray<String> KEY_MAP = new SparseArray();
    private static final String TAG_S = "Player/Ui/PlayerOverlay";
    private final String TAG = ("Player/Ui/PlayerOverlay@" + Integer.toHexString(hashCode()));
    private boolean mInitied;
    private LoadingView mLoadingView;
    private MediaControllerContainer mMediaController;
    private int mMenuType;
    private final PostLoadingRunnable mPostLoadingRunnable = new PostLoadingRunnable();
    private final PostShowBufferingRunnable mPostShowBufferingRunnable = new PostShowBufferingRunnable();
    private SourceType mSourceType;

    private class PostLoadingRunnable implements Runnable {
        private String mMessage;

        public void setInfo(String message) {
            this.mMessage = message;
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerOverlay.this.TAG, "mPostLoadingRunnable.run()");
            }
            PlayerOverlay.this.showLoading(this.mMessage);
        }
    }

    private class PostShowBufferingRunnable implements Runnable {
        private PostShowBufferingRunnable() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerOverlay.this.TAG, "mPostShowBufferingRunnable.run()");
            }
            PlayerOverlay.this.mMediaController.showBuffering();
        }
    }

    public PlayerOverlay(GalaPlayerView playerView, boolean initByWindowMode, float zoomRatio) {
        LogUtils.m1568d(this.TAG, " PlayerOverlay.<init>: initByWindowMode=" + initByWindowMode);
        this.mPlayerView = playerView;
        initOverlay(this.mPlayerView);
        switchScreenMode(!initByWindowMode, zoomRatio);
    }

    protected void initOverlay(GalaPlayerView playerView) {
        super.initOverlay(playerView);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initOverlay()");
        }
        this.mMediaController = this.mPlayerView.getMediaController();
        this.mLoadingView = this.mPlayerView.getLoadingView();
        this.mMediaController.setAdStateListener(this.mOnAdStateListener);
        this.mScreenSwitchers.add(this.mLoadingView);
        this.mScreenSwitchers.add(this.mMediaController);
    }

    public void setSourceType(SourceType type) {
        this.mSourceType = type;
    }

    private void switchScreenMode(boolean isFullScreen, float zoomRatio) {
        this.mIsFullScreenMode = isFullScreen;
        Iterator it = this.mScreenSwitchers.iterator();
        while (it.hasNext()) {
            ((IScreenUISwitcher) it.next()).switchScreen(isFullScreen, zoomRatio);
        }
    }

    public void setCurrentVideo(IVideo movie) {
        super.setCurrentVideo(movie);
        this.mMediaController.setVideo(movie);
    }

    protected void clearAd() {
        this.mMediaController.clearAd();
    }

    public void changeScreenMode(boolean isFullScreen, float zoomRatio) {
        switchScreenMode(isFullScreen, zoomRatio);
        if (!isFullScreen && this.mVideo != null) {
            this.mMenuPanel.hide();
            setEnableShowAd(true);
        }
    }

    public void setIsShowGallery(boolean isShow) {
        this.mMenuPanel.setIsShowAssociatives(isShow);
    }

    public void onDestroy() {
        super.onDestroy();
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(this.TAG, "onDestroy()");
        }
        this.mLoadingView.onActivityDestroyed();
        this.mMenuPanel.onActivityDestroyed();
        this.mState = 1002;
    }

    private boolean isPauseAdShown() {
        if (this.mAdController != null) {
            List<Integer> adTypes = this.mAdController.getShownAdType();
            if (adTypes != null && adTypes.contains(Integer.valueOf(4))) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.m1568d(this.TAG, ">> dispatchKeyEvent: " + keyEventToString(event) + "mSourceType=" + this.mSourceType);
        sendKeyEventPingback(event);
        if (this.mFullScreenHint.isShown()) {
            LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: fullscreen hint consumed");
            return this.mFullScreenHint.dispatchKeyEvent(event);
        }
        boolean isFirstDownEvent;
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            isFirstDownEvent = true;
        } else {
            isFirstDownEvent = false;
        }
        int keyCode = event.getKeyCode();
        if (this.mLoadingView.isShown() && keyCode != 4 && keyCode != 24 && keyCode != 25) {
            LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: loading view blocks keys");
            return true;
        } else if (event.getAction() == 0 && this.mState == 1004 && handleJsKeyEvent(event)) {
            return true;
        } else {
            if (isFirstDownEvent) {
                switch (keyCode) {
                    case 4:
                        if (this.mState == 1004 && this.mAdController != null && (getClickThroughAdType() == 101 || getClickThroughAdType() == 102 || getClickThroughAdType() == 100)) {
                            this.mAdController.hideAd(100);
                            return true;
                        } else if (this.mMenuPanel.isShown()) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: hide menu panel");
                            }
                            this.mMenuPanel.hide();
                            setEnableShowAd(true);
                            return true;
                        }
                        break;
                    case 20:
                        if (this.mAdController != null && this.mAdController.isEnabledSkipAd()) {
                            this.mAdController.skipAd(1);
                        }
                        if (this.mAdController != null && isPauseAdShown()) {
                            this.mAdController.hideAd(4);
                            this.mAdController.hideAd(6);
                            return true;
                        } else if ((this.mVideo == null || this.mVideo.getProvider() == null || (this.mVideo.getProvider().getPlaylistSource() == 0 && this.mVideo.getSourceType() != SourceType.PUSH)) && this.mState != 1004) {
                            if (!LogUtils.mIsDebug) {
                                return true;
                            }
                            LogUtils.m1568d(this.TAG, "block KEYCODE_DPAD_DOWN while data is not ready.");
                            return true;
                        } else if (!(!isMenuEnableShow() || this.mMenuPanel.isShown() || this.mState == 1004)) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: show selection menu panel");
                            }
                            setAdData();
                            this.mMenuPanel.show(20);
                            this.mMediaController.hideGuideTip();
                            setEnableShowAd(false);
                            this.mMenuType = 20;
                            if (this.mAdController == null) {
                                return true;
                            }
                            this.mAdController.hideAd(4);
                            this.mAdController.hideAd(6);
                            this.mAdController.hideAd(3);
                            return true;
                        }
                        break;
                    case 22:
                        if (!(this.mAdController == null || !this.mAdController.isEnabledClickThroughAd() || getClickThroughAdType() == 101 || getClickThroughAdType() == 102)) {
                            this.mAdController.showAd(100);
                            break;
                        }
                    case 23:
                    case 66:
                        LogUtils.m1568d(this.TAG, ">> mState=" + this.mState + ", mMenuPanel.isShown() " + this.mMenuPanel.isShown());
                        if (this.mAdController == null || this.mMenuPanel.isShown() || this.mState == 1001 || !this.mAdController.clickInteractionAd()) {
                            if (!(this.mAdController == null || this.mMenuPanel.isShown() || this.mState != 1004)) {
                                this.mAdController.startPurchasePage();
                                break;
                            }
                        }
                        return true;
                        break;
                    case 82:
                        if (this.mVideo == null || this.mVideo.getProvider() == null || (this.mVideo.getProvider().getPlaylistSource() == 0 && this.mVideo.getSourceType() != SourceType.PUSH)) {
                            if (!LogUtils.mIsDebug) {
                                return true;
                            }
                            LogUtils.m1568d(this.TAG, "block KEYCODE_MENU while data is not ready.");
                            return true;
                        } else if (this.mMenuPanel.isShown()) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: hide menu panel");
                            }
                            this.mMenuPanel.hide();
                            setEnableShowAd(true);
                            if (this.mMenuType != 20) {
                                return true;
                            }
                            setAdData();
                            this.mMenuPanel.show(82);
                            this.mMenuType = 82;
                            setEnableShowAd(false);
                            return true;
                        } else if (!(!isMenuEnableShow() || this.mMenuPanel.isShown() || this.mState == 1004)) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: show menu panel");
                            }
                            setAdData();
                            this.mMenuPanel.show(82);
                            this.mMediaController.hideGuideTip();
                            setEnableShowAd(false);
                            this.mMenuType = 82;
                            if (this.mAdController != null) {
                                this.mAdController.hideAd(6);
                                this.mAdController.hideAd(3);
                                this.mAdController.hideAd(4);
                            }
                            this.mMediaController.dispatchKeyEvent(event);
                            return true;
                        }
                        break;
                }
            }
            if (!this.mMenuPanel.isShown() && isFirstDownEvent) {
                LogUtils.m1568d(this.TAG, "dispatchKeyEvent: event goes to media controller");
                if (this.mMediaController.dispatchKeyEvent(event)) {
                    LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: media controller consumed");
                    return true;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "<< dispatchKeyEvent: not handled");
            }
            return false;
        }
    }

    private void setEnableShowAd(boolean enable) {
        if (this.mAdController != null) {
            this.mAdController.setEnableShow(enable);
        }
    }

    private boolean isMenuEnableShow() {
        return PlayerAppConfig.isShowMenuPanel(this.mSourceType) && this.mIsNetworkConnected;
    }

    static {
        BANDWIDTH_HINT_MAP.put(Integer.valueOf(5), "8M");
        BANDWIDTH_HINT_MAP.put(Integer.valueOf(10), "20M");
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

    public void setOnFullScreenHintChangedListener(OnFullScreenHintChangedListener listener) {
        this.mFullScreenHint.setHintListener(listener);
    }

    public void showFullScreenHint(FullScreenHintType hintType) {
        LogUtils.m1568d(this.TAG, "showFullScreenHint");
        if (hintType == FullScreenHintType.LIVE && this.mLoadingView != null) {
            this.mLoadingView.hide();
        }
        this.mFullScreenHint.show(hintType);
    }

    public void setAlbumId(String albumId) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setAlbumId(" + albumId + ")");
        }
        if (!StringUtils.isEmpty((CharSequence) albumId)) {
            this.mLoadingView.setAlbumId(albumId);
        }
    }

    public void showLoading(String message) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showLoading(" + message + ")");
        }
        if (!this.mLoadingView.isShown()) {
            this.mLoadingView.initViews();
            this.mLoadingView.show();
            this.mLoadingView.startLoadingAnimation();
            this.mLoadingView.setLoadingText(message);
            LogUtils.m1568d(this.TAG, " showLoadingView: shown");
        } else if (message == null) {
            this.mLoadingView.setLoadingText("");
        } else {
            this.mLoadingView.setLoadingText(message);
            this.mLoadingView.startLoadingAnimation();
        }
    }

    public void showLoading(String message, long delayMs) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showLoading(" + message + ", " + delayMs + ")");
        }
        if (delayMs > 0) {
            this.mPostLoadingRunnable.setInfo(message);
            this.mHandler.postDelayed(this.mPostLoadingRunnable, delayMs);
            return;
        }
        showLoading(message);
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showPlaying(" + simple + ")");
        }
        this.mState = 1000;
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
        this.mLoadingView.showPlaying();
        if (!this.mInitied) {
            updateMediaController();
        }
        if (this.mVideo != null) {
            BitStream bitStream = this.mVideo.getCurrentBitStream();
            if (!(bitStream == null || bitStream.getDefinition() == 0)) {
                this.mMediaController.updateBitStreamDefinition(PlayerDataUtils.getBitStreamString(this.mContext, bitStream));
            }
        }
        this.mMediaController.showPlaying(simple);
    }

    public void hideLoadingView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hideLoadingView()");
        }
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
        this.mLoadingView.hide();
    }

    public void clearMediaControllerState() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "clearMediaControllerState");
        }
        this.mMediaController.clearMediaControllerState();
    }

    private void updateMediaController() {
        this.mMediaController.updateView(false, this.mSourceType);
        this.mInitied = true;
    }

    public void showPaused() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showPaused()");
        }
        this.mState = 1001;
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
        this.mMediaController.showPaused();
    }

    public void updateBitStream(List<BitStream> list, BitStream bitStream) {
        LogRecordUtils.logd(this.TAG, "updateBitStream(" + bitStream + ", list " + list + ")");
        super.updateBitStream(list, bitStream);
        this.mMediaController.updateBitStreamDefinition(PlayerDataUtils.getBitStreamString(this.mContext, bitStream));
    }

    public void showAdPlaying(int countDownTime) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showAdPlaying(" + countDownTime + ")");
        }
        this.mState = 1004;
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
        this.mMenuPanel.hide();
        setEnableShowAd(true);
        this.mLoadingView.hide();
        if (!this.mInitied) {
            updateMediaController();
        }
        this.mMediaController.showAdPlaying(countDownTime);
    }

    public void showCompleted() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showCompleted() isFullScreenMode=" + this.mIsFullScreenMode);
        }
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
    }

    public void showStopped() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showStoped()");
        }
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
        this.mMenuPanel.hide();
        setEnableShowAd(true);
    }

    private void showErrorPanel(ErrorType errorType, String msg) {
        PlayerErrorPanelInfo info;
        PlayerErrorPanelUIConfig config;
        View errorView = this.mErrorPanel.getView();
        if (errorView.getParent() == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "add errorView to mPlayerView");
            }
            this.mPlayerView.addView(errorView, -1, -1);
        }
        if (errorType == ErrorType.VIP) {
            info = UiUtils.getPlayerErrorPanelInfoForVIP();
            config = UiUtils.getPlayerErrorPanelUIConfigForVIP();
        } else {
            info = UiUtils.getPlayerErrorPanelInfoForCommon(AlbumTextHelper.formatPlayerErrorMessage(msg));
            config = UiUtils.getPlayerErrorPanelUIConfigForCommon();
        }
        this.mErrorPanel.show(config, info);
    }

    public void showBuffering() {
        this.mMediaController.showBuffering();
    }

    public void hideBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hideBuffering()");
        }
        this.mHandler.removeCallbacks(this.mPostShowBufferingRunnable);
        this.mMediaController.hideBuffering();
    }

    public void showError(ErrorType errorType, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showError(" + msg + ")");
        }
        this.mHandler.removeCallbacks(this.mPostLoadingRunnable);
        this.mMenuPanel.hide();
        this.mLoadingView.hide();
        this.mLoadingView.onError();
        this.mMediaController.hide();
        setEnableShowAd(true);
        showErrorPanel(errorType, msg);
    }

    public void onSeekBegin(View view, int i) {
        this.mMediaController.onSeekBegin(view, i);
    }

    public void onProgressChanged(View view, int i) {
        this.mMediaController.onProgressChanged(view, i);
    }

    public void onSeekEnd(View view, int i) {
        this.mMediaController.onSeekEnd(view, i);
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

    private String keyEventToString(KeyEvent event) {
        String keyAction;
        StringBuilder builder = new StringBuilder();
        String keyName = (String) KEY_MAP.get(event.getKeyCode());
        if (keyName == null) {
            keyName = "[" + event.getKeyCode() + AlbumEnterFactory.SIGN_STR;
        }
        builder.append("key event: (").append(keyName).append(", ");
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
        builder.append(keyAction).append(", ");
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

    public void setThreeDimensional(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setThreeDimensional=" + enable);
        }
        this.mMediaController.setThreeDimensional(enable);
        this.mInitied = true;
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hide()");
        }
        this.mMenuPanel.hide();
        this.mMediaController.hide();
        setEnableShowAd(true);
        this.mFullScreenHint.dismissHint(null);
    }

    public void showTip(CharSequence tip) {
    }

    public void showTip(ITip tip) {
        super.showTip(tip);
        if (this.mTip != null) {
            if (this.mTip.getTipType().isSupportPersistent()) {
                this.mMediaController.showTip(this.mTip);
            } else {
                this.mMediaController.showTip(this.mTip);
            }
        }
    }

    public void hideTip() {
        this.mMediaController.hideTip();
    }

    public void setSubtitle(String subtitle) {
        this.mMediaController.setSubtitle(subtitle);
    }

    public void setProgress(int progress) {
        this.mMediaController.setProgress(progress);
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
        this.mMediaController.setMaxProgress(maxProgress, maxProgress);
    }

    public void setHeadAndTailProgress(int headProgress, int tailProgress) {
        this.mMediaController.setHeadAndTailProgress(headProgress, tailProgress);
    }

    public void setSecondaryProgress(int percent) {
        this.mMediaController.setSecondaryProgress(percent);
    }

    public int getProgress() {
        return this.mMediaController.getProgress();
    }

    public void setBufferPercent(int percent) {
        this.mMediaController.setBufferPercent(percent);
    }

    public void setNetSpeed(long bytePerSecond) {
        this.mMediaController.setNetSpeed(bytePerSecond);
    }

    public void updateBitStreamDefinition(BitStream bitStream) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "updateBitStreamDefinition=" + bitStream + ", " + MultiScreen.get().isPhoneKey());
        }
        this.mMediaController.updateBitStreamDefinition(PlayerDataUtils.getBitStreamString(this.mContext, bitStream));
    }

    public void notifyVideoDataChanged(int dataType) {
    }

    public View getContentView() {
        return this.mPlayerView;
    }

    public boolean isLoadingViewShown() {
        return this.mLoadingView.isShown();
    }

    public void onStop() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(this.TAG, "onStop()");
        }
        this.mLoadingView.onActivityStop();
        this.mState = 1003;
    }

    public void setMaxVolume(int arg0) {
    }

    public void setVolume(int arg0) {
    }

    public void updateAdCountDown(int countDownTime) {
        this.mMediaController.showAdPlaying(countDownTime);
    }

    public void setOnRequestChannelInfoListener(OnRequestChannelInfoListener arg0) {
    }

    public void setOnUserChannelChangeListener(OnUserChannelChangeListener arg0) {
    }

    public void setCurrentChannel(TVChannelCarousel arg0) {
    }

    public void notifyChannelInfoChange(int chnChangeOffset, boolean needRequestAll, boolean realChannelChange) {
    }

    public void notifyChannelChangeByIndex(int arg0) {
    }

    public void hideFullScreenHint() {
    }

    protected String getBlock(boolean isUgcThrow) {
        String block = "";
        if (this.mState == 1002 || this.mState == 1004) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "<<getBlock not started return null");
            }
            return block;
        } else if (this.mMenuPanel.isShown()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "<<getBlock menupanel show return null");
            }
            return block;
        } else {
            if (this.mState == 1000) {
                block = isUgcThrow ? "ugcplaying" : "playing";
            } else if (this.mState == 1001 && isUgcThrow) {
                block = "ugcpause";
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "getBlock():" + block + ", mState=" + this.mState);
            }
            return block;
        }
    }

    protected String getSubRSeatByKeyEvent(KeyEvent event) {
        int code = event.getKeyCode();
        if (this.mMenuPanel.isShown()) {
            if (!(code == 4 || code == 82)) {
                return "";
            }
        } else if (!(this.mState == 1002 || this.mState == 1004 || code != 4)) {
            return "";
        }
        return null;
    }

    public void showMiddleAdEnd() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showMiddleAdEnd()");
        }
        this.mMediaController.showMiddleAdEnd();
    }

    public void showMiddleAdPlaying(int time) {
        showAdPlaying(time);
    }

    public void setAllTagList(List<TVChannelCarouselTag> list) {
    }

    public void setOnPageAdvertiseStateChangeListener(OnPageAdvertiseStateChangeListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setOnPageAdvertiseStateChangeListener()");
        }
        super.setOnPageAdvertiseStateChangeListener(listener);
    }
}
