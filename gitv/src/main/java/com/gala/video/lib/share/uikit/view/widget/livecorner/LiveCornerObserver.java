package com.gala.video.lib.share.uikit.view.widget.livecorner;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;

public class LiveCornerObserver {
    private static final boolean DEBUG_LOG = false;
    private String TAG = "LiveCornerFactory@";
    private Handler mHandler;
    private ItemInfoModel mItemInfoModel;
    private LiveCornerListener mLiveCornerListener;

    public void removeObserver() {
        this.mLiveCornerListener = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void addObserver(ItemInfoModel itemInfoModel, LiveCornerListener liveCornerListener) {
        if (itemInfoModel == null) {
            LogUtils.d(this.TAG, "addObserver itemInfoModel == null");
            return;
        }
        this.mItemInfoModel = itemInfoModel;
        removeObserver();
        this.mLiveCornerListener = liveCornerListener;
        String liveType = itemInfoModel.getCuteViewData("ID_CORNER_R_T", UIKitConfig.KEY_LIVE_PLAYING_TYPE);
        long startTime = 0;
        try {
            String startT = itemInfoModel.getCuteViewData("ID_CORNER_R_T", UIKitConfig.KEY_LIVE_START_TIME);
            if (TextUtils.isEmpty(startT)) {
                startTime = 0;
            } else {
                startTime = Long.parseLong(startT);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        long endTime = 0;
        try {
            String endT = itemInfoModel.getCuteViewData("ID_CORNER_R_T", UIKitConfig.KEY_LIVE_END_TIME);
            if (TextUtils.isEmpty(endT)) {
                endTime = 0;
            } else {
                endTime = Long.parseLong(endT);
            }
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        }
        LivePlayingType livePlayingType = LivePlayingType.valueOf(liveType);
        boolean before = LivePlayingType.BEFORE.equals(livePlayingType);
        boolean playing = LivePlayingType.PLAYING.equals(livePlayingType);
        boolean end = LivePlayingType.END.equals(livePlayingType);
        if (before) {
            sendBeforeMsg(startTime, endTime);
        } else if (playing) {
            sendPlayingMsg(endTime);
        } else if (end) {
            sendEndMsg();
        }
    }

    private void sendBeforeMsg(long startTime, long endTime) {
        long ms = startTime - TVApiBase.getTVApiProperty().getCurrentTime();
        if (ms <= 0) {
            sendPlayingMsg(endTime);
            return;
        }
        if (this.mLiveCornerListener != null) {
            this.mLiveCornerListener.showBefore();
        }
        LiveCornerModel liveModel = new LiveCornerModel();
        liveModel.endTime = endTime;
        liveModel.livePlayingType = LivePlayingType.BEFORE;
        Message msg = Message.obtain();
        msg.obj = liveModel;
        initHandler();
        this.mHandler.sendMessageDelayed(msg, ms);
    }

    private void sendPlayingMsg(long endTime) {
        long ms = endTime - TVApiBase.getTVApiProperty().getCurrentTime();
        if (ms <= 0) {
            sendEndMsg();
            return;
        }
        if (this.mLiveCornerListener != null) {
            this.mLiveCornerListener.showPlaying();
        }
        LiveCornerModel liveModel = new LiveCornerModel();
        liveModel.endTime = endTime;
        liveModel.livePlayingType = LivePlayingType.PLAYING;
        Message msg = Message.obtain();
        msg.obj = liveModel;
        initHandler();
        this.mHandler.sendMessageDelayed(msg, ms);
    }

    private void sendEndMsg() {
        if (this.mLiveCornerListener != null) {
            this.mLiveCornerListener.showEnd();
        }
    }

    private void initHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message msg) {
                    if (msg != null && LiveCornerObserver.this.mLiveCornerListener != null) {
                        LiveCornerModel lastLiveModel = msg.obj;
                        LivePlayingType lastType = lastLiveModel.livePlayingType;
                        if (LivePlayingType.BEFORE.equals(lastType)) {
                            if (LiveCornerObserver.this.mLiveCornerListener != null) {
                                LiveCornerObserver.this.mLiveCornerListener.showPlaying();
                            }
                            LiveCornerModel nextLiveModel = new LiveCornerModel();
                            nextLiveModel.livePlayingType = LivePlayingType.PLAYING;
                            Message message = Message.obtain();
                            message.obj = nextLiveModel;
                            LiveCornerObserver.this.mHandler.sendMessageDelayed(message, lastLiveModel.endTime - TVApiBase.getTVApiProperty().getCurrentTime());
                        } else if (LivePlayingType.PLAYING.equals(lastType) && LiveCornerObserver.this.mLiveCornerListener != null) {
                            LiveCornerObserver.this.mLiveCornerListener.showEnd();
                        }
                    }
                }
            };
        }
    }
}
