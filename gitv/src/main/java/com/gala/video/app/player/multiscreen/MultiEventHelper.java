package com.gala.video.app.player.multiscreen;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.ISceneActionData;
import com.gala.sdk.player.ISceneActionData.SceneType;
import com.gala.sdk.player.ISceneActionProvider;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnMultiScreenStateChangeListener;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.ErrorConstants;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperEventInput;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperPlayerOverlay;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.KeyValue;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.SceneId;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.ErrorUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDataUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;
import java.util.ArrayList;
import java.util.List;

public class MultiEventHelper extends Wrapper {
    private static final int VOLUME_CHANGE_INTERVAL = 100;
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private final String TAG = ("Player/MultiEventHelper@" + Integer.toHexString(hashCode()));
    private List<BitStream> mBitStreams;
    private Context mContext;
    private BitStream mCurrentBitStream;
    private final Object mFetchMultiEventLock = new Object();
    private IGalaVideoPlayer mGalaVideoPlayer;
    private Instrumentation mInst = new Instrumentation();
    private ISuperEventInput mKeyEventHelper;
    private int mLastState;
    private long mLastVolumeChangeTime;
    private OnMultiScreenStateChangeListener mMultiScreenStateChangeListener = new OnMultiScreenStateChangeListener() {
        public void onSeekEnd() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onSeekEnd");
            }
            MultiScreen.get().onSeekFinish();
        }

        public void onScreenModeSwitched(ScreenMode screenMode) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onScreenModeSwitched(" + screenMode + ")");
            }
            MultiEventHelper.this.sendMessage(MultiEventHelper.this.getPlayState(MultiEventHelper.this.mGalaVideoPlayer));
        }

        public void onMovieSwitched(IVideo video) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onMovieSwitched(" + video.toStringBrief() + ")");
            }
            MultiEventHelper.this.sendMessage(5);
        }

        public void onMovieStop() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onMovieStop()");
            }
            if (MultiEventHelper.this.isPushVideoByTvPlatform() || !MultiEventHelper.this.isVideoPreview(MultiEventHelper.this.mGalaVideoPlayer.getVideo())) {
                MultiEventHelper.this.sendMessage(3);
            } else {
                MultiEventHelper.this.sendMessage(6);
            }
            MultiEventHelper.this.resetPlaybackState();
        }

        public void onMovieStart() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onMovieStart()");
            }
            MultiEventHelper.this.sendMessage(1);
        }

        public void onMoviePause() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onMoviePause()");
            }
            MultiEventHelper.this.sendMessage(2);
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "notify onSeekFinish in onMoviePause");
            }
            MultiScreen.get().onSeekFinish();
        }

        public void onMovieComplete() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onMovieComplete()");
            }
            MultiEventHelper.this.sendMessage(3);
            MultiEventHelper.this.resetPlaybackState();
        }

        public void onMoiveVipStateReady(boolean isVip) {
        }

        public void onError(IVideo video, ISdkError error) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onError(" + (video != null ? video.toStringBrief() : "NULL") + "," + error + ")");
            }
            if (error == null) {
                MultiEventHelper.this.sendMessage(3);
            } else if (MultiEventHelper.this.isPushVideoByTvPlatform() || !(MultiEventHelper.this.isUserCannotPreviewCode(error) || MultiEventHelper.this.isVideoPreview(video))) {
                MultiEventHelper.this.sendMessage(3);
            } else {
                MultiEventHelper.this.sendMessage(6);
            }
            MultiEventHelper.this.resetPlaybackState();
        }

        public void onBitStreamListReady(List<BitStream> bitStreams) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onBitStreamListReady(" + bitStreams + ")");
            }
            MultiEventHelper.this.mBitStreams = bitStreams;
            IVideo video = MultiEventHelper.this.mGalaVideoPlayer.getVideo();
            MultiEventHelper.this.mCurrentBitStream = video != null ? video.getCurrentBitStream() : null;
            MultiEventHelper.this.sendMessage(MultiEventHelper.this.getPlayState(MultiEventHelper.this.mGalaVideoPlayer));
        }

        public void onBitStreamChange(BitStream bitStream) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onBitStreamChange(" + bitStream + ")");
            }
            MultiEventHelper.this.mCurrentBitStream = bitStream;
            MultiEventHelper.this.sendMessage(MultiEventHelper.this.getPlayState(MultiEventHelper.this.mGalaVideoPlayer));
        }

        public void onAdStart() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MultiEventHelper.this.TAG, "onAdStart()");
            }
            MultiEventHelper.this.sendMessage(1);
            MultiScreen.get().getGalaCustomOperator().onAdStart();
        }
    };
    private ISuperPlayerOverlay mOverlay;
    private List<ISceneActionProvider> mProviderList = new ArrayList();

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setOverlay(ISuperPlayerOverlay overlay) {
        this.mOverlay = overlay;
    }

    public void setEventInput(ISuperEventInput eventHelper) {
        this.mKeyEventHelper = eventHelper;
    }

    public void registerPlayer(IGalaVideoPlayer videoplayer) {
        resetPlaybackState();
        this.mGalaVideoPlayer = videoplayer;
    }

    public void registerPlayStateListener(IGalaVideoPlayer galaVideoPlayer) {
        galaVideoPlayer.setOnMultiScreenStateChangeListener(this.mMultiScreenStateChangeListener);
    }

    public void addSceneActionProvider(ISceneActionProvider provider) {
        this.mProviderList.add(provider);
    }

    public void onGetSceneAction(KeyValue keyValue) {
        String string;
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onGetSceneAction(" + keyValue + ")");
        }
        keyValue.setAppendSeek(true);
        keyValue.setSceneId(SceneId.PLAY_PAGE);
        if (this.mContext != null) {
            string = this.mContext.getResources().getString(R.string.vc_exit_player);
        } else {
            string = "";
        }
        keyValue.addExactMatch(string, new Runnable() {
            public void run() {
                ((Activity) MultiEventHelper.this.mContext).finish();
            }
        });
        for (ISceneActionProvider provider : this.mProviderList) {
            List<ISceneActionData> dataList = provider.onGetSceneActionData();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onGetSceneAction: provider=" + provider + ", scene data=" + dataList + ")");
            }
            if (dataList != null) {
                for (int i = 0; i < dataList.size(); i++) {
                    handleSceneData((ISceneActionData) dataList.get(i), keyValue);
                }
            }
        }
        if (this.mKeyEventHelper != null) {
            this.mKeyEventHelper.onGetSceneAction(keyValue);
        }
    }

    public List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> actions) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onGetSceneAction(" + actions + ")");
        }
        actions.add(CreateInterfaceTools.createVoiceCommon().createAbsVoiceAction(this.mContext != null ? this.mContext.getResources().getString(R.string.vc_exit_player) : "", new Runnable() {
            public void run() {
                ((Activity) MultiEventHelper.this.mContext).finish();
            }
        }, KeyWordType.DEFAULT));
        for (ISceneActionProvider provider : this.mProviderList) {
            List<ISceneActionData> dataList = provider.onGetSceneActionData();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onGetSceneAction: provider=" + provider + ", scene data=" + dataList + ")");
            }
            if (dataList != null) {
                for (int i = 0; i < dataList.size(); i++) {
                    actions = getSupportedCommonKeyWordTypeVoices((ISceneActionData) dataList.get(i), actions);
                }
            }
        }
        if (this.mKeyEventHelper != null) {
            return this.mKeyEventHelper.getSupportedPlaybackVoices(actions);
        }
        return actions;
    }

    public List<AbsVoiceAction> getSupportedVoicesWithoutPreAndNext(List<AbsVoiceAction> actions) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onGetSceneAction(" + actions + ")");
        }
        actions.add(CreateInterfaceTools.createVoiceCommon().createAbsVoiceAction(this.mContext != null ? this.mContext.getResources().getString(R.string.vc_exit_player) : "", new Runnable() {
            public void run() {
                ((Activity) MultiEventHelper.this.mContext).finish();
            }
        }, KeyWordType.DEFAULT));
        for (ISceneActionProvider provider : this.mProviderList) {
            List<ISceneActionData> dataList = provider.onGetSceneActionData();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onGetSceneAction: provider=" + provider + ", scene data=" + dataList + ")");
            }
            if (dataList != null) {
                int i = 0;
                while (i < dataList.size()) {
                    if (((ISceneActionData) dataList.get(i)).getType() == SceneType.PRE_VIDEO || ((ISceneActionData) dataList.get(i)).getType() == SceneType.NEXT_VIDEO) {
                        LogUtils.d(this.TAG, "getSupportedVoicesWithoutPreAndNext: provider=" + provider + ", scene data=" + dataList + ")");
                    } else {
                        actions = getSupportedCommonKeyWordTypeVoices((ISceneActionData) dataList.get(i), actions);
                    }
                    i++;
                }
            }
        }
        if (this.mKeyEventHelper != null) {
            return this.mKeyEventHelper.getSupportedPlaybackVoices(actions);
        }
        return actions;
    }

    private List<AbsVoiceAction> getSupportedCommonKeyWordTypeVoices(ISceneActionData data, List<AbsVoiceAction> actions) {
        SceneType type = data.getType();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleSceneData>dataList.get(i) = " + type.toString());
        }
        if (!(type == null || this.mContext == null)) {
            IVoiceCommon provider = CreateInterfaceTools.createVoiceCommon();
            Resources res = this.mContext.getResources();
            switch (type) {
                case PRE_VIDEO:
                    actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_prev_episode), data.getActionRunnable(), KeyWordType.DEFAULT));
                    actions.add(provider.createAbsVoiceAction(15, "-1", data.getActionRunnable(), KeyWordType.DEFAULT));
                    break;
                case NEXT_VIDEO:
                    actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_next_episode), data.getActionRunnable(), KeyWordType.DEFAULT));
                    actions.add(provider.createAbsVoiceAction(15, "1", data.getActionRunnable(), KeyWordType.DEFAULT));
                    break;
                case LAST_VIDEO:
                    actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_last_episode), data.getActionRunnable(), KeyWordType.DEFAULT));
                    break;
                case OFF_SKIP_TAIL:
                    actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_disable_skip_ht_6), data.getActionRunnable(), KeyWordType.DEFAULT));
                    break;
                case ON_SKIP_TAIL:
                    actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_enable_skip_ht_4), data.getActionRunnable(), KeyWordType.DEFAULT));
                    break;
                case SELECT_EPISODE:
                    int episodeSelect = ((Integer) data.getKey().mParams[0]).intValue();
                    String nthEpisodeStr = res.getString(R.string.vc_nth_episode, new Object[]{Integer.valueOf(episodeSelect)});
                    if (!StringUtils.isEmpty(episodeSelect + "")) {
                        actions.add(provider.createAbsVoiceAction(nthEpisodeStr, data.getActionRunnable(), KeyWordType.RESERVED));
                        actions.add(provider.createAbsVoiceAction(10, String.valueOf(episodeSelect), data.getActionRunnable(), KeyWordType.DEFAULT));
                        break;
                    }
                    break;
                case CHANGE_BITSTREAM:
                    BitStream def = data.getKey().mParams[0];
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(this.TAG, "onGetSceneAction: provider=" + PlayerDataUtils.getBitStreamString(this.mContext, def));
                    }
                    actions.add(provider.createAbsVoiceAction(PlayerDataUtils.getBitStreamString(this.mContext, def), data.getActionRunnable(), KeyWordType.DEFAULT));
                    break;
            }
        }
        return actions;
    }

    private void handleSceneData(ISceneActionData data, KeyValue keyValue) {
        SceneType type = data.getType();
        if (type != null && this.mContext != null) {
            Resources res = this.mContext.getResources();
            switch (type) {
                case PRE_VIDEO:
                    keyValue.addExactMatch(res.getString(R.string.vc_prev_episode), data.getActionRunnable());
                    return;
                case NEXT_VIDEO:
                    keyValue.addExactMatch(res.getString(R.string.vc_next_episode), data.getActionRunnable());
                    return;
                case LAST_VIDEO:
                    keyValue.addExactMatch(res.getString(R.string.vc_last_episode), data.getActionRunnable());
                    return;
                case OFF_SKIP_TAIL:
                    keyValue.addExactMatch(res.getString(R.string.vc_disable_skip_ht_1), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_disable_skip_ht_2), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_disable_skip_ht_3), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_disable_skip_ht_4), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_disable_skip_ht_5), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_disable_skip_ht_6), data.getActionRunnable());
                    return;
                case ON_SKIP_TAIL:
                    keyValue.addExactMatch(res.getString(R.string.vc_enable_skip_ht_1), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_enable_skip_ht_2), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_enable_skip_ht_3), data.getActionRunnable());
                    keyValue.addExactMatch(res.getString(R.string.vc_enable_skip_ht_4), data.getActionRunnable());
                    return;
                case SELECT_EPISODE:
                    int episodeSelect = ((Integer) data.getKey().mParams[0]).intValue();
                    keyValue.addReservedMatch(res.getString(R.string.vc_nth_episode, new Object[]{Integer.valueOf(episodeSelect)}), data.getActionRunnable());
                    return;
                case CHANGE_BITSTREAM:
                    BitStream def = data.getKey().mParams[0];
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(this.TAG, "onGetSceneAction: provider=" + PlayerDataUtils.getBitStreamString(this.mContext, def));
                    }
                    keyValue.addExactMatch(PlayerDataUtils.getBitStreamString(this.mContext, def), data.getActionRunnable());
                    return;
                case RECOMMEND_LIST:
                    Object[] object = data.getKey().mParams;
                    IVideo video = object[0];
                    int videoSelect = ((Integer) object[1]).intValue();
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(this.TAG, "onGetSceneAction: provider=" + video + ", videoSelect =" + videoSelect + ")");
                    }
                    keyValue.addFuzzyMatch(video.getAlbumName(), data.getActionRunnable());
                    keyValue.addReservedMatch(res.getString(R.string.vc_nth_video, new Object[]{Integer.valueOf(videoSelect)}), data.getActionRunnable());
                    return;
                default:
                    return;
            }
        }
    }

    public boolean onDlnaKeyEvent(DlnaKeyEvent event, KeyKind key) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onDlnaKeyEvent() event=" + event + ", key=" + key);
        }
        if (this.mOverlay == null || this.mOverlay.onDlnaKeyEvent(event, key) || this.mOverlay.isMenuPanelShowing()) {
            return false;
        }
        switch (event) {
            case SCROLL:
                int keyCode = getCode(key);
                if (shouldInjectVolumKey(keyCode)) {
                    return updownToVolume(keyCode);
                }
                if (this.mKeyEventHelper != null) {
                    return this.mKeyEventHelper.onDlnaEvent(key);
                }
                return false;
            case FLING:
                boolean isHandled;
                if (this.mKeyEventHelper == null || !this.mKeyEventHelper.onDlnaEvent(key)) {
                    isHandled = false;
                } else {
                    isHandled = true;
                }
                return isHandled;
            default:
                return false;
        }
    }

    private boolean shouldInjectVolumKey(int code) {
        boolean isInKeyMap;
        if (19 == code || 20 == code) {
            isInKeyMap = true;
        } else {
            isInKeyMap = false;
        }
        boolean isFullScreen;
        if (this.mOverlay == null || !this.mOverlay.isInFullScreenMode()) {
            isFullScreen = false;
        } else {
            isFullScreen = true;
        }
        boolean isMenuShown;
        if (this.mOverlay == null || !this.mOverlay.isMenuPanelShowing()) {
            isMenuShown = false;
        } else {
            isMenuShown = true;
        }
        if (isInKeyMap && isFullScreen && !isMenuShown) {
            return true;
        }
        return false;
    }

    private boolean updownToVolume(int keycode) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "updownToVolume, keycode = " + keycode);
        }
        boolean ret = false;
        int targetKeyCode = -1;
        if (19 == keycode) {
            targetKeyCode = 24;
            ret = true;
        } else if (20 == keycode) {
            targetKeyCode = 25;
            ret = true;
        }
        final int keyCode = targetKeyCode;
        long now = SystemClock.uptimeMillis();
        long interval = now - this.mLastVolumeChangeTime;
        if (ret && interval >= 100) {
            this.mLastVolumeChangeTime = now;
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    try {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(MultiEventHelper.this.TAG, "sendKeyDownUpSync: " + keyCode);
                        }
                        MultiEventHelper.this.mInst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.e(MultiEventHelper.this.TAG, "mInstrument.sendKeyDownUpSync exception", e);
                        }
                    }
                }
            });
        }
        return ret;
    }

    private int getCode(KeyKind key) {
        if (KeyKind.UP == key) {
            return 19;
        }
        if (KeyKind.DOWN == key) {
            return 20;
        }
        return -1;
    }

    private boolean isInPlayMode() {
        if (this.mOverlay == null) {
            return false;
        }
        boolean isFullScreen = this.mOverlay.isInFullScreenMode();
        boolean isMenuShown = this.mOverlay.isMenuPanelShowing();
        if (!isFullScreen || isMenuShown) {
            return false;
        }
        if (this.mGalaVideoPlayer.isPaused() || this.mGalaVideoPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public boolean onKeyChanged(int keycode) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onKeyChanged() keycode=" + keycode);
        }
        return shouldInjectVolumKey(keycode) && updownToVolume(keycode);
    }

    public Notify onPhoneSync() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> onPhoneSync()");
        }
        final Notify notify = new Notify();
        synchronized (this.mFetchMultiEventLock) {
            mHandler.post(new Runnable() {
                public void run() {
                    MultiEventHelper.this.updateMultiEvent(notify);
                }
            });
            try {
                this.mFetchMultiEventLock.wait();
            } catch (InterruptedException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "onPhoneSync(), mFetchMultiEventLock.wait() throw exception ", e);
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<< onPhoneSync() ret = " + JSON.toJSONString(notify));
        }
        return notify;
    }

    private void updateMultiEvent(Notify notify) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> updateMultiEvent()");
        }
        synchronized (this.mFetchMultiEventLock) {
            try {
                if (this.mGalaVideoPlayer != null) {
                    IVideo video = this.mGalaVideoPlayer.getVideo();
                    if (video != null) {
                        int playerState = getPlayState(this.mGalaVideoPlayer);
                        notify.album_id = video.getAlbumId();
                        notify.video_id = video.getTvId();
                        notify.play_duration = this.mGalaVideoPlayer.getDuration();
                        notify.play_position = this.mGalaVideoPlayer.getCurrentPosition();
                        notify.play_state = playerState;
                        notify.title = video.getTvName();
                        if (this.mCurrentBitStream != null) {
                            notify.res = getResString(this.mCurrentBitStream);
                        }
                        if (this.mBitStreams != null) {
                            List<String> resList = new ArrayList();
                            for (BitStream bs : this.mBitStreams) {
                                resList.add(getResString(bs));
                            }
                            notify.res_list = resList;
                        }
                        notify.vip_purchase = video.isPreview();
                    }
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "<< updateMultiEvent(), mMultiEvent = " + JSON.toJSONString(notify));
                }
                this.mFetchMultiEventLock.notifyAll();
            } catch (Throwable th) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "<< updateMultiEvent(), mMultiEvent = " + JSON.toJSONString(notify));
                }
                this.mFetchMultiEventLock.notifyAll();
            }
        }
    }

    private int getPlayState(IGalaVideoPlayer player) {
        int i = 6;
        if (this.mLastState != 6) {
            i = -1;
            if (player != null) {
                if (player.isPlaying()) {
                    i = 1;
                } else if (player.isPaused()) {
                    i = 2;
                } else if (player.isCompleted()) {
                    i = 3;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "getPlayState return " + i);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getPlayState return STATE_PURCHASE");
        }
        return i;
    }

    private void sendMessage(int playState) {
        Notify notify = new Notify();
        notify.play_duration = this.mGalaVideoPlayer.getDuration();
        notify.play_position = this.mGalaVideoPlayer.getCurrentPosition();
        IVideo video = this.mGalaVideoPlayer.getVideo();
        notify.album_id = video != null ? video.getAlbumId() : "";
        notify.video_id = video != null ? video.getTvId() : "";
        notify.title = video != null ? video.getTvName() : "";
        if (this.mCurrentBitStream != null) {
            notify.res = getResString(this.mCurrentBitStream);
        }
        if (this.mBitStreams != null) {
            List<String> resList = new ArrayList();
            for (BitStream bs : this.mBitStreams) {
                resList.add(getResString(bs));
            }
            notify.res_list = resList;
        }
        boolean z = (video != null && video.isPreview()) || playState == 6;
        notify.vip_purchase = z;
        notify.play_state = playState;
        MultiScreen.get().sendMessage(notify);
        this.mLastState = playState;
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendMessage, notify.play_state=" + notify.play_state + ", notify.vip_purchase=" + notify.vip_purchase);
        }
    }

    private String getResString(BitStream bs) {
        return String.valueOf(BitStream.getBid(bs));
    }

    public boolean onResolutionChanged(String newRes) {
        final BitStream bs = BitStream.buildBitStreamFrom(StringUtils.parse(newRes, -1));
        bs.setBenefitType(0);
        if (bs.getDefinition() != 0) {
            mHandler.post(new Runnable() {
                public void run() {
                    MultiEventHelper.this.mGalaVideoPlayer.switchBitStream(bs);
                }
            });
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onResolutionChanged(" + newRes + "), invalid resolution! " + bs);
        }
        return false;
    }

    public boolean onSeekChanged(long newPosition) {
        if (this.mKeyEventHelper != null) {
            int currentPosition = this.mKeyEventHelper.getLastPosition();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onSeekChanged() newPosition=" + newPosition + ", currentPosition=" + currentPosition);
            }
            boolean isLoadingViewShown = this.mOverlay != null && this.mOverlay.isLoadingViewShown();
            if (isInPlayMode() && !isLoadingViewShown && newPosition >= 0) {
                this.mKeyEventHelper.onPhoneSeekEvent((int) (newPosition - ((long) currentPosition)));
            }
        }
        return true;
    }

    public long getPlayPosition() {
        return (long) this.mGalaVideoPlayer.getCurrentPosition();
    }

    private void resetPlaybackState() {
        this.mCurrentBitStream = null;
        this.mBitStreams = null;
    }

    public boolean isPushVideoByTvPlatform() {
        IDynamicResult dataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        boolean useTvPlatform = dataModel != null ? dataModel.getIsPushVideoByTvPlatform() : false;
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "isPushVideoByTvPlatform return " + useTvPlatform);
        }
        return useTvPlatform;
    }

    private boolean isUserCannotPreviewCode(ISdkError error) {
        boolean ret = false;
        if (error != null) {
            String errorCode = "";
            if (error.getModule() == 201) {
                errorCode = error.getServerCode();
            } else if (error.getModule() == 101) {
                errorCode = ErrorUtils.parseSecondCodeFromPumaError(error);
            }
            if (ErrorConstants.API_ERR_CODE_Q302.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q310.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q304.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q305.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q505.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q503.equals(errorCode)) {
                ret = true;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "isUserCannotPreviewCode(" + errorCode + ") returns " + ret);
            }
        }
        return ret;
    }

    private boolean isVideoPreview(IVideo video) {
        boolean isPreview = video != null && video.isPreview();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "isVideoPreview(" + video + ") returns " + isPreview);
        }
        return isPreview;
    }
}
