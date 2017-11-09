package com.gala.video.app.epg.home.component.item.corner;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.HomeDebug;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.LiveCornerListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.Wrapper;

public class LiveCornerFactory extends Wrapper {
    private String TAG = InterfaceKey.EPG_LCF;
    private ChannelLabel label;
    private Handler mHandler;
    private LiveCornerListener mLiveCornerListener;

    public LiveCornerFactory() {
        this.TAG += "@" + Integer.toHexString(hashCode());
        if (HomeDebug.DEBUG_LOG) {
            LogUtils.e(this.TAG, "build factory, init handler");
        }
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                if (HomeDebug.DEBUG_LOG) {
                    LogUtils.e(LiveCornerFactory.this.TAG, "handleMessage," + LiveCornerFactory.this.label.toString());
                }
                if (msg != null && LiveCornerFactory.this.mLiveCornerListener != null) {
                    LiveCornerModel lastLiveModel = msg.obj;
                    LivePlayingType lastType = lastLiveModel.livePlayingType;
                    if (HomeDebug.DEBUG_LOG) {
                        LogUtils.e(LiveCornerFactory.this.TAG, "handleMessage, lastType=" + lastType);
                    }
                    if (LivePlayingType.BEFORE.equals(lastType)) {
                        if (LiveCornerFactory.this.mLiveCornerListener != null) {
                            LiveCornerFactory.this.mLiveCornerListener.showPlaying();
                        }
                        LiveCornerModel nextLiveModel = new LiveCornerModel();
                        nextLiveModel.livePlayingType = LivePlayingType.PLAYING;
                        Message message = Message.obtain();
                        message.obj = nextLiveModel;
                        long delayMillis = lastLiveModel.endTime - TVApiBase.getTVApiProperty().getCurrentTime();
                        LiveCornerFactory.this.mHandler.sendMessageDelayed(message, delayMillis);
                        if (HomeDebug.DEBUG_LOG) {
                            LogUtils.e(LiveCornerFactory.this.TAG, "handleMessage,sendDelay:" + delayMillis + ",nextLiveModel:" + nextLiveModel);
                        }
                    } else if (LivePlayingType.PLAYING.equals(lastType) && LiveCornerFactory.this.mLiveCornerListener != null) {
                        LiveCornerFactory.this.mLiveCornerListener.showEnd();
                    }
                }
            }
        };
    }

    public void end() {
        if (HomeDebug.DEBUG_LOG) {
            LogUtils.e(this.TAG, "clearStutus," + this.label.toString());
        }
        this.mLiveCornerListener = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void start(ChannelLabel label, LiveCornerListener liveCornerListener) {
        if (label == null) {
            LogUtils.e(this.TAG, "start label == null");
            return;
        }
        this.label = label;
        if (HomeDebug.DEBUG_LOG) {
            LogUtils.e(this.TAG, "refreshStatus," + label.toString());
        }
        this.mHandler.removeCallbacksAndMessages(null);
        this.mLiveCornerListener = liveCornerListener;
        LivePlayingType livePlayingType = label.getLivePlayingType();
        boolean before = LivePlayingType.BEFORE.equals(livePlayingType);
        boolean playing = LivePlayingType.PLAYING.equals(livePlayingType);
        boolean end = LivePlayingType.END.equals(livePlayingType);
        LiveCornerModel liveModel;
        Message msg;
        long ms;
        if (before) {
            if (liveCornerListener != null) {
                liveCornerListener.showBefore();
            }
            liveModel = new LiveCornerModel();
            liveModel.endTime = LiveCornerUtils.formatTime(label.itemKvs.LiveEpisode_EndTime);
            liveModel.livePlayingType = LivePlayingType.BEFORE;
            msg = Message.obtain();
            msg.obj = liveModel;
            ms = LiveCornerUtils.formatTime(label.itemKvs.LiveEpisode_StartTime) - TVApiBase.getTVApiProperty().getCurrentTime();
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.e(this.TAG, "refreshStatus,before,delay=" + ms + ",liveModel=" + liveModel);
            }
            this.mHandler.sendMessageDelayed(msg, ms);
        } else if (playing) {
            if (liveCornerListener != null) {
                liveCornerListener.showPlaying();
            }
            liveModel = new LiveCornerModel();
            liveModel.endTime = LiveCornerUtils.formatTime(label.itemKvs.LiveEpisode_EndTime);
            liveModel.livePlayingType = LivePlayingType.PLAYING;
            msg = Message.obtain();
            msg.obj = liveModel;
            ms = LiveCornerUtils.formatTime(label.itemKvs.LiveEpisode_EndTime) - TVApiBase.getTVApiProperty().getCurrentTime();
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.e(this.TAG, "refreshStatus,playing,delay=" + ms + ",liveModel=" + liveModel);
            }
            this.mHandler.sendMessageDelayed(msg, ms);
        } else if (end) {
            if (liveCornerListener != null) {
                liveCornerListener.showEnd();
            }
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.e(this.TAG, "refreshStatus,end");
            }
        }
    }
}
