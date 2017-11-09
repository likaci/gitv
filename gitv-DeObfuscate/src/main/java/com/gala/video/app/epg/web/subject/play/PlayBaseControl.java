package com.gala.video.app.epg.web.subject.play;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.alibaba.fastjson.JSONObject;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.IPlayerProfile;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IVideo;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.web.function.IScreenCallback;
import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.app.epg.web.subject.api.IApi.IStatusCallback;
import com.gala.video.app.epg.web.subject.api.VipLiveApi;
import com.gala.video.app.epg.web.utils.WebDataUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.webview.event.WebBaseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class PlayBaseControl {
    private static final int MSG_BACKSUBJECT = 5;
    private static final int MSG_CHANGE_BUY = 4;
    private static final int MSG_CHANGE_SCREEN_MODE = 3;
    private static final int MSG_START_PLAYER = 1;
    private static final int MSG_SWITCH_PLAY = 2;
    private static final String TAG = "EPG/Web/PlayBaseControl";
    private Activity mActivity;
    private boolean mActivityPaused;
    protected Album mAlbum = null;
    protected String mBuySource = null;
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    protected String mEventId = null;
    protected ArrayList<Album> mFlowerList;
    protected String mFrom = null;
    private IGalaVideoPlayer mGalaVideoPlayer;
    protected String mH5PlayType;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PlayBaseControl.this.initVideoPlayer(msg.obj);
                    return;
                case 2:
                    PlayBaseControl.this.mIsVipError = false;
                    PlayBaseControl.this.switchPlayVideo(msg.obj);
                    return;
                case 3:
                    if (PlayBaseControl.this.mIsVipError && PlayBaseControl.this.mGalaVideoPlayer != null) {
                        PlayBaseControl.this.mIsVipError = false;
                        PlayBaseControl.this.mGalaVideoPlayer.replay();
                        PlayBaseControl.this.mGalaVideoPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
                        PlayBaseControl.this.updateScreenMode(ScreenMode.FULLSCREEN);
                        return;
                    } else if (!PlayBaseControl.this.mIsErrorState || PlayBaseControl.this.mGalaVideoPlayer == null) {
                        PlayBaseControl.this.changeScreenMode(msg.obj);
                        return;
                    } else {
                        PlayBaseControl.this.mGalaVideoPlayer.onErrorClicked();
                        return;
                    }
                case 4:
                    PlayBaseControl.this.mMethodBridge.refreshVipBuyButton(msg.arg1);
                    return;
                case 5:
                    PlayBaseControl.this.mMethodBridge.onBackSubject();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mHasPlayer;
    protected Intent mIntent;
    protected boolean mIsErrorState = false;
    private boolean mIsFisrtEnter = true;
    protected boolean mIsVipError = false;
    private IMethodBridge mMethodBridge;
    private IMultiEventHelper mMultiEventHelper = null;
    protected String mPlId = null;
    protected String mPlName = null;
    protected int mPlayIndex;
    protected List<Album> mPlayList;
    private LayoutParams mPlayWindowLayoutParams;
    private RelativeLayout mPlayerContainer;
    protected int mResultCode = -1;
    private IScreenCallback mScreenCallback;
    protected String mTabSource = null;
    private OnPlayerStateChangedListener mVideoStateListener = new C12633();
    private WebBaseEvent mWebBasicEvent;
    protected WebInfo mWebInfo;

    class C12622 implements IStatusCallback {
        C12622() {
        }

        public void onStatus(int flag) {
            if (PlayBaseControl.this.mHandler != null) {
                Message msg = PlayBaseControl.this.mHandler.obtainMessage(4);
                msg.arg1 = flag;
                PlayBaseControl.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C12633 implements OnPlayerStateChangedListener {
        C12633() {
        }

        public void onVideoSwitched(IVideo video, int type) {
            boolean z = false;
            PlayBaseControl.this.updateErrorState(false);
            String str = PlayBaseControl.TAG;
            StringBuilder append = new StringBuilder().append("onVideoSwitched video==null?>> ");
            if (video == null) {
                z = true;
            }
            LogUtils.m1568d(str, append.append(z).toString());
            if (video != null && video.getAlbum() != null) {
                PlayBaseControl.this.mPlayIndex = PlayBaseControl.this.findPlayIndex(video.getTvId());
                String albumJson = WebDataUtils.getAlbumInfo(video);
                PlayBaseControl.this.mMethodBridge.onVideoSwitched(albumJson);
                LogUtils.m1568d(PlayBaseControl.TAG, "onVideoPluginSwitched" + albumJson);
            }
        }

        public void onPlaybackFinished() {
            LogUtils.m1568d(PlayBaseControl.TAG, "onVideoPlayFinished");
            PlayBaseControl.this.mMethodBridge.onPlaybackFinished();
        }

        public void onScreenModeSwitched(ScreenMode newMode) {
            LogUtils.m1568d(PlayBaseControl.TAG, "onScreenModeSwitched newMode = " + newMode);
            PlayBaseControl.this.mCurScreenMode = newMode;
            PlayBaseControl.this.updateScreenMode(PlayBaseControl.this.mCurScreenMode);
        }

        public boolean onError(IVideo video, ISdkError error) {
            LogUtils.m1571e(PlayBaseControl.TAG, "onError: error=" + error + ", video=" + video + ", mCurScreenMode=" + PlayBaseControl.this.mCurScreenMode);
            PlayBaseControl.this.updateErrorState(true);
            PlayBaseControl.this.mCurScreenMode = ScreenMode.WINDOWED;
            PlayBaseControl.this.updateScreenMode(PlayBaseControl.this.mCurScreenMode);
            if (PlayerErrorUtils.handleNetWorkError(error)) {
                return false;
            }
            return PlayBaseControl.this.onErrorPlay(video, error);
        }

        public void onVideoStarted(IVideo video) {
            PlayBaseControl.this.updateErrorState(false);
            PlayBaseControl.this.mIsVipError = false;
        }

        public void onAdStarted() {
        }

        public void onAdEnd() {
        }

        public void onPrepared() {
        }
    }

    public abstract WebViewDataImpl generateJsonObject(WebViewDataImpl webViewDataImpl);

    public abstract void initPlay(JSONObject jSONObject);

    public abstract boolean onErrorPlay(IVideo iVideo, ISdkError iSdkError);

    public abstract void onResumePlay();

    public PlayBaseControl(WebInfo webInfo) {
        this.mWebInfo = webInfo;
    }

    private void init(Activity activity, WebBaseEvent webBasicEvent) {
        this.mActivity = activity;
        this.mWebBasicEvent = webBasicEvent;
        this.mMethodBridge = new MethodBridge(this.mWebBasicEvent);
        this.mTabSource = StringUtils.isEmpty(this.mWebInfo.getTabSrc()) ? PingBackUtils.getTabSrc() : this.mWebInfo.getTabSrc();
    }

    public void init(Activity activity, WebBaseEvent webBasicEvent, Intent intent) {
        init(activity, webBasicEvent);
        this.mIntent = intent;
    }

    public void setPlayerContainer(RelativeLayout playerContainer) {
        this.mPlayerContainer = playerContainer;
    }

    public WebViewDataImpl generateJsonParams(WebViewDataImpl jsonParam) {
        if (jsonParam == null) {
            jsonParam = new WebViewDataImpl();
        }
        this.mFrom = this.mWebInfo.getFrom();
        this.mBuySource = this.mWebInfo.getBuySource();
        this.mEventId = this.mWebInfo.getEventId();
        jsonParam.putFrom(this.mFrom);
        return generateJsonObject(jsonParam);
    }

    private void updateErrorState(boolean errorState) {
        this.mIsErrorState = errorState;
    }

    public int findPlayIndex(String tvQid) {
        int result = -1;
        if (ListUtils.isEmpty(this.mPlayList)) {
            LogUtils.m1571e(TAG, "mPlayList is empty");
            return -1;
        }
        int size = this.mPlayList.size();
        for (int i = 0; i < size; i++) {
            if (StringUtils.equals(((Album) this.mPlayList.get(i)).tvQid, tvQid)) {
                result = i;
                break;
            }
        }
        LogUtils.m1568d(TAG, "findPlayIndex result=" + result + ",tvQid=" + tvQid);
        return result;
    }

    public void createVideoPlayer(Bundle extras) {
        extras.putInt(PlayerIntentConfig2.INTENT_PARAM_RESULT_CODE, this.mResultCode);
        this.mMultiEventHelper = CreateInterfaceTools.createMultiEventHelper();
        this.mGalaVideoPlayer = GetInterfaceTools.getGalaVideoPlayerGenerator().createVideoPlayer(this.mActivity, this.mPlayerContainer, extras, this.mVideoStateListener, ScreenMode.WINDOWED, this.mPlayWindowLayoutParams, new WindowZoomRatio(true, WindowZoomRatio.WINDOW_ZOOM_RATIO_16_BY_9_SMALL), this.mMultiEventHelper, null);
        if (1 == this.mWebBasicEvent.getEventType()) {
            SurfaceView surfaceView = GetInterfaceTools.getGalaVideoPlayerGenerator().getSurfaceViewPlayerUsed(this.mGalaVideoPlayer);
            if (surfaceView != null) {
                surfaceView.setZOrderMediaOverlay(true);
            }
        }
        if (this.mCurScreenMode == ScreenMode.FULLSCREEN) {
            this.mCurScreenMode = ScreenMode.FULLSCREEN;
            this.mGalaVideoPlayer.changeScreenMode(this.mCurScreenMode);
        }
    }

    private void initVideoPlayer(String playInfo) {
        if (!this.mActivityPaused) {
            try {
                JSONObject playParams = DataUtils.parseToJsonObject(playInfo);
                if (playParams != null) {
                    int width = playParams.getIntValue("width");
                    int height = playParams.getIntValue("height");
                    int x = playParams.getIntValue(WebConstants.PARAM_KEY_X);
                    int y = playParams.getIntValue(WebConstants.PARAM_KEY_Y);
                    this.mH5PlayType = playParams.getString("play_h5_type");
                    this.mPlayWindowLayoutParams = new LayoutParams(width, height);
                    this.mPlayWindowLayoutParams.leftMargin = x;
                    this.mPlayWindowLayoutParams.topMargin = y;
                    LogUtils.m1571e(TAG, "initVideoPlayer() ->  w:" + width + ",h:" + height + "x:" + x + ",y" + y);
                    initPlay(playParams);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void switchPlayVideo(String playInfo) {
        LogUtils.m1568d(TAG, "switchPlayVideo");
        if (this.mGalaVideoPlayer != null) {
            JSONObject playParams = DataUtils.parseToJsonObject(playInfo);
            if (playParams == null) {
                LogUtils.m1571e(TAG, "switchPlayVideo() -> playParams is null");
                return;
            }
            Album album = DataUtils.parseToAlbum(playParams.getString("album"));
            if (album == null) {
                LogUtils.m1571e(TAG, "switchPlayVideo() -> album is null");
                return;
            }
            IVideo video = this.mGalaVideoPlayer.getVideo();
            if (video == null) {
                LogUtils.m1571e(TAG, "switchPlayVideo() -> video is null");
                return;
            }
            IPlayerProfile profile = GetInterfaceTools.getPlayerProfileCreator().createProfile();
            this.mGalaVideoPlayer.switchVideo(GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory().createVideoItem(video.getSourceType(), album, profile), this.mFrom);
        }
    }

    private void changeScreenMode(String mode) {
        if (this.mGalaVideoPlayer != null) {
            this.mGalaVideoPlayer.changeScreenMode("1".equals(mode) ? ScreenMode.WINDOWED : ScreenMode.FULLSCREEN);
        }
    }

    public void startWindowPlay(String playInfo) {
        if (!this.mIsErrorState || this.mGalaVideoPlayer == null) {
            LogUtils.m1568d(TAG, "startWindowPlay playInfo = " + playInfo);
            this.mHasPlayer = true;
            Message msg = this.mHandler.obtainMessage(1);
            msg.obj = playInfo;
            this.mHandler.sendMessage(msg);
            return;
        }
        LogUtils.m1571e(TAG, "startWindowPlay onErrorClicked");
        this.mGalaVideoPlayer.onErrorClicked();
    }

    public void switchScreenMode(String mode) {
        LogUtils.m1568d(TAG, "switchScreenMode mode = " + mode);
        Message msg = this.mHandler.obtainMessage(3);
        msg.obj = mode;
        this.mHandler.sendMessage(msg);
    }

    public void onAlbumSelected(String albumInfo) {
    }

    public void checkLiveInfo(String albumInfo) {
        new VipLiveApi().query(this.mAlbum, new C12622());
    }

    public void switchPlay(String playInfo) {
        LogUtils.m1568d(TAG, "switchPlay playInfo = " + playInfo);
        Message msg = this.mHandler.obtainMessage(2);
        msg.obj = playInfo;
        this.mHandler.sendMessage(msg);
    }

    public void onDestroy() {
        if (this.mVideoStateListener != null) {
            this.mVideoStateListener = null;
        }
        this.mWebInfo = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        if (this.mWebBasicEvent != null) {
            this.mWebBasicEvent.onDestroy();
            this.mWebBasicEvent = null;
        }
        if (this.mGalaVideoPlayer != null) {
            this.mGalaVideoPlayer.release();
            this.mGalaVideoPlayer = null;
            updateErrorState(false);
        }
    }

    public void onPause(boolean isFnishing) {
        if (isFnishing) {
            if (this.mGalaVideoPlayer != null) {
                this.mGalaVideoPlayer.release();
            }
            this.mGalaVideoPlayer = null;
        } else if (this.mGalaVideoPlayer != null) {
            this.mGalaVideoPlayer.sleep();
            LogUtils.m1568d(TAG, ">> onPause mGalaVideoPlayer ,sleep()");
        }
        this.mActivityPaused = true;
    }

    public void onStop() {
    }

    public void onResume() {
        LogUtils.m1568d(TAG, ">> onResume mResultCode:" + this.mResultCode);
        loadVipInfo();
        if (this.mActivityPaused) {
            this.mActivityPaused = false;
            if (this.mGalaVideoPlayer != null) {
                LogUtils.m1568d(TAG, ">> onResume mGalaVideoPlayer.isSleeping()" + this.mGalaVideoPlayer.isSleeping());
                if (!this.mGalaVideoPlayer.isSleeping() || this.mResultCode == 1 || this.mResultCode == 22) {
                    this.mGalaVideoPlayer.release();
                    onResumePlay();
                } else {
                    this.mGalaVideoPlayer.wakeUp();
                }
            } else {
                LogUtils.m1571e(TAG, ">> onResume mGalaVideoPlayer is null");
            }
        }
        onBackSubject();
    }

    private void onBackSubject() {
        if (!this.mIsFisrtEnter) {
            this.mHandler.sendEmptyMessageDelayed(5, 3000);
        }
        this.mIsFisrtEnter = false;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (this.mGalaVideoPlayer == null || !this.mGalaVideoPlayer.handleKeyEvent(event)) {
            return false;
        }
        return true;
    }

    public Notify onPhoneSync() {
        if (this.mMultiEventHelper == null) {
            return null;
        }
        return this.mMultiEventHelper.onPhoneSync();
    }

    public boolean onKeyChanged(int keycode) {
        return this.mMultiEventHelper == null ? false : this.mMultiEventHelper.onKeyChanged(keycode);
    }

    public void onActionScrollEvent(KeyKind keyKind) {
        if (this.mMultiEventHelper != null) {
            this.mMultiEventHelper.onDlnaKeyEvent(DlnaKeyEvent.SCROLL, keyKind);
        }
    }

    public List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> actions) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onGetSceneAction List<AbsVoiceAction>:" + actions);
        }
        if (this.mMultiEventHelper != null) {
            return this.mMultiEventHelper.getSupportedVoices(actions);
        }
        return actions;
    }

    public boolean onResolutionChanged(String newRes) {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.onResolutionChanged(newRes) : false;
    }

    public boolean onSeekChanged(long newPosition) {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.onSeekChanged(newPosition) : false;
    }

    public long getPlayPosition() {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.getPlayPosition() : 0;
    }

    public boolean isFullscreen() {
        return ScreenMode.FULLSCREEN == this.mCurScreenMode;
    }

    public void setScreenCallback(IScreenCallback screenCallback) {
        this.mScreenCallback = screenCallback;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mResultCode = resultCode;
    }

    private void updateScreenMode(ScreenMode mode) {
        if (this.mScreenCallback != null) {
            this.mScreenCallback.switchScreenMode(mode);
        }
        this.mMethodBridge.onScreenModeSwitched(mode == ScreenMode.WINDOWED ? 1 : 0);
    }

    public void loadVipInfo() {
    }
}
