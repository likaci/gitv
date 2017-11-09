package com.gala.video.app.player.controller;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.sdk.player.IEventInput.OnUserSeekListener;
import com.gala.sdk.player.IMediaPlayer;
import com.gala.sdk.player.OnUserPlayPauseListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.ui.IPlayerOverlay;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.video.app.player.R;
import com.gala.video.app.player.utils.PlayerToastHelper;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperEventInput;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.KeyValue;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;
import com.gala.video.lib.share.project.Project;
import java.lang.ref.WeakReference;
import java.util.List;
import org.cybergarage.upnp.Device;
import org.xbill.DNS.WKSRecord.Service;

public class EventInput implements ISuperEventInput {
    private static final int MSG_PHONE_SEEKTO = 1;
    private static final int NOTIFY_CHANNEL_CHANGE_INTERVAL = 400;
    private static final int SEEKTO_PROGRESS = 2;
    private static final int SEEK_DELAY_TIME = 400;
    private static final int SEEK_STEP_LONG = 20000;
    private static final int SEEK_STEP_MAX = 180000;
    private static final int SEEK_STEP_MIN = 5000;
    private static final int SEEK_STEP_SHORT = 5000;
    private static final int SEEK_STEP_THRESHOLD = 300000;
    private static final int[] SEEK_STEP_VALUES = new int[]{1, 2, 5, 10, 15, 20, 25, 50, 70, 100, Service.CISCO_FNA};
    private static final String TAG = "Player/App/EventInput";
    private static final int[] TIME_PERIODS = new int[]{10, 30, 60, IMediaPlayer.AD_INFO_VIP_NO_AD, 1200, Device.DLNA_SEARCH_LEASE_TIME, 2400, 3600, 5400, 7200, 8000};
    private Context mContext;
    private EventMode mCurrentMode;
    private MyHandler mHandler;
    private boolean mIsPaused = false;
    private boolean mIsSeeking = false;
    private int mLastSeekTo = -1;
    private int mMaxProgress;
    private int mMaxSeekableProgress;
    private int mMultiSeekNum = 0;
    private IPlayerOverlay mOverlay;
    private int mProgress;
    private boolean mSeekEnabled;
    private int mSeekProgressValue = 10;
    private OnUserPlayPauseListener mUserPlayPauseListener;
    private OnUserSeekListener mUserSeekListener;

    private enum EventMode {
        MODE_NORMAL,
        MODE_LIVE,
        MODE_CAROUSEL
    }

    private static class MyHandler extends Handler {
        WeakReference<EventInput> mWeakEventInput;

        public MyHandler(EventInput eventInput) {
            super(Looper.getMainLooper());
            this.mWeakEventInput = new WeakReference(eventInput);
        }

        public void handleMessage(Message msg) {
            EventInput eventInput = (EventInput) this.mWeakEventInput.get();
            if (eventInput != null) {
                switch (msg.what) {
                    case 1:
                        eventInput.seekOffset(msg.arg1);
                        return;
                    case 2:
                        eventInput.notifyListener();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public EventInput(Context context, IPlayerOverlay overlay) {
        this.mContext = context;
        this.mOverlay = overlay;
        this.mHandler = new MyHandler(this);
    }

    public void setSourceType(SourceType sourceType) {
        switch (sourceType) {
            case LIVE:
                this.mCurrentMode = EventMode.MODE_LIVE;
                return;
            case CAROUSEL:
                this.mCurrentMode = EventMode.MODE_CAROUSEL;
                return;
            default:
                this.mCurrentMode = EventMode.MODE_NORMAL;
                return;
        }
    }

    private void seekToRight(boolean forward, boolean isContinuousPressing) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> seekToRight(" + forward + ")");
        }
        int last = getLastPosition() + getSeekStep(forward, isContinuousPressing);
        delaySeekTo(last);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< seekToRight() seek to " + last);
        }
    }

    public int getLastPosition() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> getLastPosition() begin. mIsSeeking=" + this.mIsSeeking + ", mLastSeekTo=" + this.mLastSeekTo);
        }
        if (this.mLastSeekTo < 0) {
            this.mLastSeekTo = this.mProgress;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< getLastPosition() end. mIsSeeking=" + this.mIsSeeking + ", mLastSeekTo=" + this.mLastSeekTo);
        }
        return this.mLastSeekTo;
    }

    private void delaySeekTo(int seekTo) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "delaySeekTo(" + seekTo + ")");
        }
        int seekMax = this.mMaxProgress;
        if (this.mMaxSeekableProgress > 0) {
            seekMax = this.mMaxSeekableProgress;
        }
        if (seekTo > seekMax) {
            seekTo = seekMax;
        }
        if (seekTo < 0) {
            seekTo = 0;
        }
        this.mMultiSeekNum++;
        this.mLastSeekTo = seekTo;
        if (this.mUserSeekListener != null) {
            this.mUserSeekListener.onProgressChanged(null, this.mLastSeekTo);
        }
        notifyListenerSeekTo();
    }

    private int getSeekStep(boolean forward, boolean isContinuousPressing) {
        int shortStep;
        int singleStep;
        int result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getSeekStep(" + forward + ") mMultiSeekNum=" + this.mMultiSeekNum);
        }
        int threshold = PlayerDebugUtils.getSeekThreshold() >= 0 ? PlayerDebugUtils.getSeekThreshold() : 300000;
        if (PlayerDebugUtils.getSeekStepShort() >= 0) {
            shortStep = PlayerDebugUtils.getSeekStepShort();
        } else {
            shortStep = 5000;
        }
        int longStep = PlayerDebugUtils.getSeekStepLong() >= 0 ? PlayerDebugUtils.getSeekStepLong() : 20000;
        if (this.mMaxProgress >= threshold) {
            singleStep = longStep;
        } else {
            singleStep = shortStep;
        }
        int multiStep = this.mMaxProgress / 100;
        if (!isContinuousPressing) {
            result = singleStep;
        } else if (this.mMultiSeekNum < 5) {
            result = multiStep;
        } else if (this.mMultiSeekNum < 10) {
            result = multiStep * 2;
        } else {
            result = multiStep * 4;
        }
        result = Math.min(Math.max(result, 5000), SEEK_STEP_MAX);
        int step = forward ? result : -result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getSeekStep() return " + step);
        }
        return step;
    }

    private void notifyListenerSeekTo() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyListenerSeekTo() mLastSeekTo=" + this.mLastSeekTo);
        }
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessageDelayed(2, (long) (PlayerDebugUtils.getSeekStepDelay() >= 0 ? PlayerDebugUtils.getSeekStepDelay() : 400));
    }

    private void notifyListener() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyListener, mLastSeekTo=" + this.mLastSeekTo + ", mIsSeeking=" + this.mIsSeeking + ", mMultiSeekNum=" + this.mMultiSeekNum + ", mSeekEnabled=" + this.mSeekEnabled);
        }
        if (this.mUserSeekListener != null && this.mSeekEnabled) {
            this.mUserSeekListener.onSeekEnd(null, this.mLastSeekTo);
        }
        this.mIsSeeking = false;
        this.mLastSeekTo = -1;
        this.mMultiSeekNum = 0;
    }

    private void seekOffset(int offset) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "seekOffset(" + offset + ") mIsSeeking=" + this.mIsSeeking + ", mSeekEnabled=" + this.mSeekEnabled);
        }
        if (Math.abs(offset) >= 1000) {
            if (!this.mIsSeeking && this.mSeekEnabled) {
                this.mIsSeeking = true;
                if (this.mUserSeekListener != null) {
                    this.mUserSeekListener.onSeekBegin(null, getLastPosition());
                }
            }
            int last = getLastPosition() + offset;
            delaySeekTo(last);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "seekOffset() last=" + last);
            }
        }
    }

    private void seekTo(int pos) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "seekTo(" + pos + ") mIsSeeking=" + this.mIsSeeking);
        }
        getLastPosition();
        delaySeekTo(pos);
    }

    private boolean doSeekEvent(KeyEvent event) {
        boolean z = true;
        int keycode = event.getKeyCode();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "doSeekEvent(" + event + ") mMaxProgress=" + this.mMaxProgress + ", mSeekEnabled=" + this.mSeekEnabled + ", mIsSeeking=" + this.mIsSeeking);
        }
        if (this.mMaxProgress <= 0) {
            return true;
        }
        if (!this.mIsSeeking && this.mSeekEnabled) {
            this.mIsSeeking = true;
            if (this.mUserSeekListener != null) {
                this.mUserSeekListener.onSeekBegin(null, getLastPosition());
            }
        }
        LogUtils.d(TAG, "MultiScreen.mIsPhoneKey " + MultiScreen.get().isPhoneKey());
        if (keycode == 21 || keycode == 89) {
            if (event.getRepeatCount() == 0) {
                z = false;
            }
            seekToRight(false, z);
        } else if (keycode == 22 || keycode == 90) {
            seekToRight(true, event.getRepeatCount() != 0);
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean z = false;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchKeyEvent(" + event + ") mSeekEnabled=" + this.mSeekEnabled);
        }
        int keycode = event.getKeyCode();
        if (event.getAction() != 0) {
            if (event.getAction() == 1) {
                switch (keycode) {
                    case 19:
                    case 20:
                    case 24:
                    case 25:
                    case Service.MIT_DOV /*91*/:
                        if (Project.getInstance().getBuild().shouldShowVolume()) {
                            return true;
                        }
                        break;
                    case 21:
                    case 22:
                        if (this.mSeekEnabled) {
                            return true;
                        }
                        return false;
                    case 23:
                    case 66:
                    case 85:
                        if (this.mSeekEnabled) {
                            return true;
                        }
                        return false;
                    default:
                        break;
                }
            }
        }
        switch (keycode) {
            case 21:
            case 22:
            case 89:
            case 90:
                if (!this.mSeekEnabled) {
                    return false;
                }
                if (this.mCurrentMode != EventMode.MODE_NORMAL) {
                    return true;
                }
                doSeekEvent(event);
                return true;
            case 23:
            case 66:
            case 85:
                if (!this.mSeekEnabled && this.mCurrentMode != EventMode.MODE_NORMAL) {
                    return false;
                }
                if (event.getRepeatCount() != 0 || this.mUserPlayPauseListener == null || this.mCurrentMode != EventMode.MODE_NORMAL) {
                    return true;
                }
                this.mUserPlayPauseListener.onPlayPause(null);
                if (!this.mIsPaused) {
                    z = true;
                }
                this.mIsPaused = z;
                return true;
        }
        return false;
    }

    public void onGetSceneAction(KeyValue keyValue) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onGetSceneAction(" + keyValue + ")");
        }
        if (this.mOverlay.isInFullScreenMode()) {
            addCommonPlaybackAction(keyValue);
        }
    }

    public List<AbsVoiceAction> getSupportedPlaybackVoices(List<AbsVoiceAction> actions) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getSupportedPlaybackVoices()");
        }
        if (this.mOverlay.isInFullScreenMode()) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "addCommonPlaybackAction(actions)");
            }
            actions = addKeywordTypePlaybacVoicekAction(actions);
        }
        return addSeekTypeVoices(actions);
    }

    private List<AbsVoiceAction> addKeywordTypePlaybacVoicekAction(List<AbsVoiceAction> actions) {
        Resources res = this.mContext.getResources();
        Runnable playRunnable = new Runnable() {
            public void run() {
                if (EventInput.this.mUserPlayPauseListener != null) {
                    EventInput.this.mUserPlayPauseListener.onPlay(null);
                }
            }
        };
        IVoiceCommon provider = CreateInterfaceTools.createVoiceCommon();
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_play), playRunnable, KeyWordType.DEFAULT));
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_resumeplay), playRunnable, KeyWordType.DEFAULT));
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_pause), new Runnable() {
            public void run() {
                if (EventInput.this.mUserPlayPauseListener != null) {
                    EventInput.this.mUserPlayPauseListener.onPause(null);
                }
            }
        }, KeyWordType.DEFAULT));
        Runnable fastForward = new Runnable() {
            public void run() {
                EventInput.this.dispatchKeyEvent(new KeyEvent(0, 22));
                EventInput.this.dispatchKeyEvent(new KeyEvent(1, 22));
            }
        };
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_ff_1), fastForward, KeyWordType.DEFAULT));
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_ff_2), fastForward, KeyWordType.DEFAULT));
        Runnable rewind = new Runnable() {
            public void run() {
                EventInput.this.dispatchKeyEvent(new KeyEvent(0, 21));
                EventInput.this.dispatchKeyEvent(new KeyEvent(1, 21));
            }
        };
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_rewind_1), rewind, KeyWordType.DEFAULT));
        actions.add(provider.createAbsVoiceAction(res.getString(R.string.vc_rewind_2), rewind, KeyWordType.DEFAULT));
        return actions;
    }

    private void addCommonPlaybackAction(KeyValue keyValue) {
        Resources res = this.mContext.getResources();
        Runnable playRunnable = new Runnable() {
            public void run() {
                if (EventInput.this.mUserPlayPauseListener != null) {
                    EventInput.this.mUserPlayPauseListener.onPlay(null);
                }
            }
        };
        keyValue.addExactMatch(res.getString(R.string.vc_play), playRunnable);
        keyValue.addExactMatch(res.getString(R.string.vc_resumeplay), playRunnable);
        keyValue.addExactMatch(res.getString(R.string.vc_pause), new Runnable() {
            public void run() {
                if (EventInput.this.mUserPlayPauseListener != null) {
                    EventInput.this.mUserPlayPauseListener.onPause(null);
                }
            }
        });
        Runnable fastForward = new Runnable() {
            public void run() {
                EventInput.this.dispatchKeyEvent(new KeyEvent(0, 22));
                EventInput.this.dispatchKeyEvent(new KeyEvent(1, 22));
            }
        };
        keyValue.addExactMatch(res.getString(R.string.vc_ff_1), fastForward);
        keyValue.addExactMatch(res.getString(R.string.vc_ff_2), fastForward);
        Runnable rewind = new Runnable() {
            public void run() {
                EventInput.this.dispatchKeyEvent(new KeyEvent(0, 21));
                EventInput.this.dispatchKeyEvent(new KeyEvent(1, 21));
            }
        };
        keyValue.addExactMatch(res.getString(R.string.vc_rewind_1), rewind);
        keyValue.addExactMatch(res.getString(R.string.vc_rewind_2), rewind);
    }

    private List<AbsVoiceAction> addSeekTypeVoices(List<AbsVoiceAction> actions) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "EventInput>getSupportedSeekVoices/TYPE_SEEK_TO、TYPE_SEEK_OFFSET");
        }
        actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(1, "")) {
            protected boolean dispatchVoiceEvent(VoiceEvent event) {
                PingBackUtils.setTabSrc("其他");
                if (LogUtils.mIsDebug) {
                    LogUtils.d(EventInput.TAG, "dispatchVoiceEvent(event=" + event + ")");
                }
                if (event == null || event.getType() != 1 || StringUtils.isTrimEmpty(event.getKeyword())) {
                    return false;
                }
                final int seekToPos = Integer.parseInt(event.getKeyword());
                if (LogUtils.mIsDebug) {
                    LogUtils.d(EventInput.TAG, "VoiceEvent.TYPE_SEEK_TO ：seekToPos = " + seekToPos + " ;mMaxProgress=" + EventInput.this.mMaxProgress);
                }
                if (seekToPos > EventInput.this.mMaxProgress) {
                    EventInput.this.mHandler.post(new Runnable() {
                        public void run() {
                            PlayerToastHelper.showToast(EventInput.this.mContext, R.string.voice_seekto_exceeds_max, (int) QToast.LENGTH_LONG);
                        }
                    });
                }
                EventInput.this.mHandler.post(new Runnable() {
                    public void run() {
                        EventInput.this.seekTo(seekToPos);
                    }
                });
                return true;
            }
        });
        actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(2, "")) {
            protected boolean dispatchVoiceEvent(VoiceEvent event) {
                PingBackUtils.setTabSrc("其他");
                if (LogUtils.mIsDebug) {
                    LogUtils.d(EventInput.TAG, "dispatchVoiceEvent(event=" + event + ")");
                }
                if (event == null || event.getType() != 2 || StringUtils.isTrimEmpty(event.getKeyword())) {
                    return false;
                }
                final int seekFFDelta = Integer.valueOf(event.getKeyword()).intValue();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(EventInput.TAG, "VoiceEvent.TYPE_SEEK_OFFSET ：seekFFDelta = " + seekFFDelta + " ;mProgress = " + EventInput.this.mProgress + " ;mMaxProgress=" + EventInput.this.mMaxProgress);
                }
                if (seekFFDelta >= 0) {
                    if (EventInput.this.mProgress + seekFFDelta > EventInput.this.mMaxProgress) {
                        EventInput.this.mHandler.post(new Runnable() {
                            public void run() {
                                PlayerToastHelper.showToast(EventInput.this.mContext, R.string.voice_seekto_exceeds_max, (int) QToast.LENGTH_LONG);
                            }
                        });
                    }
                    EventInput.this.mHandler.post(new Runnable() {
                        public void run() {
                            EventInput.this.seekOffset(seekFFDelta);
                        }
                    });
                } else if (seekFFDelta < 0) {
                    if (EventInput.this.mProgress + seekFFDelta < 0) {
                        EventInput.this.mHandler.post(new Runnable() {
                            public void run() {
                                PlayerToastHelper.showToast(EventInput.this.mContext, R.string.voice_seekto_exceeds_min, (int) QToast.LENGTH_LONG);
                            }
                        });
                    }
                    EventInput.this.mHandler.post(new Runnable() {
                        public void run() {
                            EventInput.this.seekOffset(seekFFDelta);
                        }
                    });
                }
                return true;
            }
        });
        return actions;
    }

    public void onPhoneSeekEvent(int offset) {
        LogUtils.d(TAG, "onPhoneSeekEvent() offset = " + offset);
        this.mIsSeeking = false;
        this.mHandler.removeMessages(1);
        Message msg = this.mHandler.obtainMessage();
        msg.what = 1;
        msg.arg1 = offset;
        this.mHandler.sendMessage(msg);
    }

    public boolean onDlnaEvent(KeyKind key) {
        LogUtils.d(TAG, "onDlnaEvent(" + key + ")");
        int offset;
        switch (key) {
            case LEFT:
                offset = (-this.mSeekProgressValue) * 1000;
                seekOffset(offset);
                LogUtils.d(TAG, "onDlnaEvent(LEFT): offset=" + offset);
                return true;
            case RIGHT:
                offset = this.mSeekProgressValue * 1000;
                seekOffset(offset);
                LogUtils.d(TAG, "onDlnaEvent(RIGHT): offset=" + offset);
                return true;
            case UP:
                LogUtils.d(TAG, "onDlnaEvent(TOP):");
                return true;
            case DOWN:
                LogUtils.d(TAG, "onDlnaEvent(BOTTOM):");
                return true;
            default:
                return false;
        }
    }

    private void initSeekProgress() {
        int videoLen = this.mMaxProgress / 1000;
        LogUtils.d(TAG, "initSeekProgress: videoLen=" + videoLen);
        if (videoLen != 0) {
            for (int i = 0; i < TIME_PERIODS.length; i++) {
                if (videoLen < TIME_PERIODS[i]) {
                    this.mSeekProgressValue = SEEK_STEP_VALUES[i];
                    break;
                }
            }
            if (videoLen >= TIME_PERIODS[TIME_PERIODS.length - 1]) {
                this.mSeekProgressValue = SEEK_STEP_VALUES[SEEK_STEP_VALUES.length - 1];
            }
        }
        LogUtils.d(TAG, "initSeekProgress: calculated progress interval=" + this.mSeekProgressValue);
    }

    public void setOnUserPlayPauseListener(OnUserPlayPauseListener playPauseListener) {
        this.mUserPlayPauseListener = playPauseListener;
    }

    public void setOnUserSeekListener(OnUserSeekListener seekListener) {
        this.mUserSeekListener = seekListener;
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setMaxProgress(" + maxProgress + ", " + maxSeekableProgress + ") mMaxProgress=" + this.mMaxProgress + ", mMaxSeekableProgress=" + this.mMaxSeekableProgress);
        }
        if (this.mMaxProgress != maxProgress || this.mMaxSeekableProgress != maxSeekableProgress) {
            this.mLastSeekTo = -1;
            this.mMultiSeekNum = 0;
            this.mIsSeeking = false;
            this.mMaxProgress = maxProgress;
            this.mMaxSeekableProgress = maxSeekableProgress;
            initSeekProgress();
        }
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public void setSeekEnabled(boolean enabled) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setSeekEnabled(" + enabled + ")");
        }
        this.mSeekEnabled = enabled;
        if (!this.mSeekEnabled) {
            this.mIsSeeking = false;
            this.mLastSeekTo = -1;
            this.mMultiSeekNum = 0;
        }
    }

    public int getProgress() {
        return this.mProgress;
    }
}
