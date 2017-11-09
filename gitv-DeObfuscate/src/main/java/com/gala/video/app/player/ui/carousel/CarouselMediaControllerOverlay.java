package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.gala.sdk.player.OnUserChannelChangeListener;
import com.gala.sdk.player.OnUserVideoChangeListener;
import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoProvider.AllChannelCallback;
import com.gala.sdk.player.data.IVideoProvider.AllChannelDetailCallback;
import com.gala.sdk.player.data.IVideoProvider.ProgramListCallback;
import com.gala.sdk.player.ui.OnRequestChannelInfoListener;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.overlay.OnAdStateListener;
import com.gala.video.app.player.ui.overlay.TipWrapper;
import com.gala.video.app.player.ui.widget.views.BufferingView;
import com.gala.video.app.player.ui.widget.views.TipView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CarouselMediaControllerOverlay extends RelativeLayout implements ICarouselMediaController, IScreenUISwitcher {
    protected static final int MSG_HIDE = 12345;
    private static final int NOTIFY_CHANNEL_CHANGE_INTERVAL = 400;
    protected static final int SHOW_DURATION = 10000;
    protected static final int STATE_PLAYING = 1;
    private static final String TAG = "Player/UI/CarouselMediaControllerOverlay";
    private AllChannelCallback mAllChannelCallback = new C14594();
    private AllChannelDetailCallback mAllChannelDetailCallback = new C14605();
    private CopyOnWriteArrayList<CarouselChannelDetail> mAllDetailInfo = new CopyOnWriteArrayList();
    private BufferingView mBufferView;
    protected boolean mBuffering;
    private ImageView mCarouselArrow;
    protected CarouselChannelListPanel mCarouselChannelListPanel;
    protected CarouselLabelPanel mCarouselLabelPanel;
    protected CarouselWindowOverlay mCarouselWindowOverlay;
    protected CarouselChannelInfoOverlay mChannelInfoOverlay;
    private Context mContext;
    private Handler mHandler = new C14561();
    boolean mIsFullScreen;
    private boolean mNeedRequsetAllChannelInfo = true;
    private Runnable mNotifyChannelChangeRunnable = new C14583();
    private OnCarouselPanelHideListener mOnCarouselHideListener;
    PlayerListListener mPlayerlistListener = new C14572();
    private ProgramListCallback mProgramListCallback = new C14616();
    protected CarouselProgrammeListPanel mProgrammListPanel;
    private View mRoot;
    protected int mState;
    private TipView mTipView;

    class C14561 extends Handler {
        C14561() {
        }

        public void handleMessage(Message msg) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "handleMessage(" + msg + ")");
            }
            switch (msg.what) {
                case CarouselMediaControllerOverlay.MSG_HIDE /*12345*/:
                    CarouselMediaControllerOverlay.this.hide();
                    if (CarouselMediaControllerOverlay.this.mOnCarouselHideListener != null) {
                        CarouselMediaControllerOverlay.this.mOnCarouselHideListener.hide();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    class C14572 implements PlayerListListener {
        C14572() {
        }

        public void onItemRecycled(ViewHolder holder) {
        }

        public void onItemFocusChanged(ViewHolder holder, boolean isSelected, TVChannelCarouselTag label, int tag) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "onItemFocusChanged() tag=" + tag);
            }
            if (holder != null && tag == 1 && label != null && isSelected && CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.show(label);
                CarouselMediaControllerOverlay.this.mAllDetailInfo.clear();
            }
        }

        public void onItemClick(ViewHolder holder, int tag) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "onItemClick() tag=" + tag);
            }
            if (tag == 1 && CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselLabelPanel.updateSelectedPosition(true);
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.requestFocus();
            } else if (tag == 2 && CarouselMediaControllerOverlay.this.mCarouselLabelPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselLabelPanel.updateDefaultLabel();
                CarouselMediaControllerOverlay.this.hide();
            } else if (tag == 3 && CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.notifyUserClickProgramme();
                CarouselMediaControllerOverlay.this.hide();
            }
        }

        public void onListShow(TVChannelCarouselTag label, int tag, boolean isShow) {
            if (tag == 1) {
                if (CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null && label != null) {
                    CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.show(label);
                    CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.setFocus();
                    CarouselMediaControllerOverlay.this.showArrow(true);
                }
            } else if (tag == 3 && !isShow && CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.updateSpreadPosition(-1);
            }
        }
    }

    class C14583 implements Runnable {
        C14583() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "mNotifyChannelChangeRunnable.run");
            }
            CarouselMediaControllerOverlay.this.mNeedRequsetAllChannelInfo = true;
            CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.notifyChannelInfoChange(0, false, true);
        }
    }

    class C14594 implements AllChannelCallback {
        C14594() {
        }

        public void onDataReady(TVChannelCarouselTag tag, List<TVChannelCarousel> allChannelList) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "AllChannelCallback onDataReady");
            }
            if (CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.setAllChannelList(allChannelList, tag);
            }
        }

        public void onException(IVideo arg0, String arg1, String arg2) {
        }
    }

    class C14605 implements AllChannelDetailCallback {
        C14605() {
        }

        public void onCacheReady(TVChannelCarouselTag tag, List<CarouselChannelDetail> allDetailInfo) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "AllChannelDetailCallback onCacheReady = " + allDetailInfo);
            }
            if (CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.setChannelProgramName(allDetailInfo, tag);
            }
            if (CarouselMediaControllerOverlay.this.mAllDetailInfo != null) {
                CarouselMediaControllerOverlay.this.mAllDetailInfo.clear();
                CarouselMediaControllerOverlay.this.mAllDetailInfo.addAll(allDetailInfo);
            }
        }

        public void onDataReady(TVChannelCarouselTag tag, List<CarouselChannelDetail> allDetailInfo) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "AllChannelDetailCallback onDataReady = " + allDetailInfo);
            }
            if (CarouselMediaControllerOverlay.this.mCarouselChannelListPanel != null) {
                CarouselMediaControllerOverlay.this.mCarouselChannelListPanel.setChannelProgramName(allDetailInfo, tag);
            }
            if (CarouselMediaControllerOverlay.this.mAllDetailInfo != null) {
                CarouselMediaControllerOverlay.this.mAllDetailInfo.clear();
                CarouselMediaControllerOverlay.this.mAllDetailInfo.addAll(allDetailInfo);
            }
        }

        public void onException(IVideo arg0, String arg1, String arg2) {
        }
    }

    class C14616 implements ProgramListCallback {
        C14616() {
        }

        public void onCacheReady(IVideo video) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "ProgramListCallback onCacheReady()");
            }
            if (CarouselMediaControllerOverlay.this.mProgrammListPanel != null && video != null && !ListUtils.isEmpty(video.getCarouseProgramList())) {
                CarouselMediaControllerOverlay.this.mProgrammListPanel.notifyDataFilled();
            }
        }

        public void onDataReady(IVideo arg0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CarouselMediaControllerOverlay.TAG, "ProgramListCallback onDataReady()");
            }
            if (CarouselMediaControllerOverlay.this.mProgrammListPanel != null) {
                CarouselMediaControllerOverlay.this.mProgrammListPanel.notifyDataFilled();
            }
        }

        public void onException(IVideo arg0, String arg1, String arg2) {
        }
    }

    public CarouselMediaControllerOverlay(Context context) {
        super(context);
        this.mContext = context;
        initCarouselOverlay();
    }

    public CarouselMediaControllerOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initCarouselOverlay();
    }

    public CarouselMediaControllerOverlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initCarouselOverlay();
    }

    private void initCarouselOverlay() {
        this.mRoot = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C1291R.layout.player_carousel_layout_control, this);
        this.mCarouselLabelPanel = new CarouselLabelPanel(this.mRoot);
        this.mCarouselLabelPanel.setPlayerListListener(this.mPlayerlistListener);
        this.mChannelInfoOverlay = new CarouselChannelInfoOverlay(this.mRoot);
        this.mCarouselChannelListPanel = new CarouselChannelListPanel(this.mRoot);
        this.mCarouselChannelListPanel.setPlayerListListener(this.mPlayerlistListener);
        this.mCarouselChannelListPanel.setAllChannnelCallBack(this.mAllChannelCallback);
        this.mCarouselChannelListPanel.setAllChannelDetailCallback(this.mAllChannelDetailCallback);
        this.mCarouselChannelListPanel.setProgramListCallback(this.mProgramListCallback);
        this.mProgrammListPanel = new CarouselProgrammeListPanel(this.mRoot);
        this.mCarouselWindowOverlay = new CarouselWindowOverlay(this.mRoot);
        this.mProgrammListPanel.setPlayerListListener(this.mPlayerlistListener);
        this.mBufferView = (BufferingView) this.mRoot.findViewById(C1291R.id.playbuffering);
        this.mTipView = (TipView) this.mRoot.findViewById(C1291R.id.tip_message);
        this.mCarouselArrow = (ImageView) this.mRoot.findViewById(C1291R.id.carousel_arrow);
    }

    public void showPlaying(boolean simple) {
        this.mState = 1;
        if (this.mTipView != null) {
            this.mTipView.hide(false);
        }
    }

    public void showAdPlaying(int countDownTime) {
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hide()");
        }
        if (this.mCarouselLabelPanel != null) {
            this.mCarouselLabelPanel.hide();
        }
        if (this.mCarouselChannelListPanel != null) {
            this.mProgrammListPanel.hide();
        }
        if (this.mCarouselChannelListPanel != null) {
            this.mCarouselChannelListPanel.hide();
        }
        if (this.mChannelInfoOverlay != null) {
            this.mChannelInfoOverlay.hide();
        }
        if (this.mCarouselArrow != null) {
            showArrow(false);
        }
        removeHandlerMessage();
    }

    public void showBuffering() {
        this.mBuffering = true;
        this.mBufferView.setVisibility(0);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showBuffering()");
        }
    }

    public void hideBuffering() {
        this.mBuffering = false;
        this.mBufferView.setVisibility(8);
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showTip(" + tip + ")");
        }
        if (!this.mChannelInfoOverlay.isShow() && this.mIsFullScreen) {
            this.mTipView.showTip(tip);
        }
    }

    public void hideTip() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideTip()");
        }
        if (this.mTipView != null && this.mIsFullScreen) {
            this.mTipView.hide(true);
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "switchScreen() isFullScreen = " + isFullScreen);
        }
        this.mIsFullScreen = isFullScreen;
        this.mBufferView.switchScreen(isFullScreen, zoomRatio);
        this.mCarouselWindowOverlay.switchScreen(isFullScreen, zoomRatio);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "switchScreen isFullScreen=" + isFullScreen);
        }
        if (!isFullScreen) {
            hide();
            this.mTipView.hide(false);
        } else if (this.mCarouselLabelPanel != null) {
            onUserInteraction();
            this.mCarouselLabelPanel.switchScreen(isFullScreen, zoomRatio);
            this.mCarouselLabelPanel.show();
        }
    }

    public void setBufferPercent(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setBufferPercent(" + percent + ")");
        }
        this.mBufferView.setBufferPercent(percent);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "dispatchKeyEvent(" + event + "), fullscreen=" + this.mIsFullScreen);
        }
        if (this.mIsFullScreen) {
            int code = event.getKeyCode();
            boolean isFirstDownEvent;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                isFirstDownEvent = true;
            } else {
                isFirstDownEvent = false;
            }
            switch (code) {
                case 4:
                    if ((this.mCarouselLabelPanel.isShow() || this.mChannelInfoOverlay.isShow()) && isFirstDownEvent) {
                        hide();
                        return true;
                    }
                case 19:
                    if (this.mCarouselLabelPanel.isShow() && !this.mCarouselLabelPanel.isListShow()) {
                        LogUtils.m1568d(TAG, "especial case that panel need to consume");
                        onUserInteraction();
                        return true;
                    } else if (this.mCarouselLabelPanel.isShow() || event.getAction() != 0) {
                        onUserInteraction();
                        if (this.mCarouselChannelListPanel.isShow() && this.mProgrammListPanel.isShow() && !this.mProgrammListPanel.isListViewShow()) {
                            this.mProgrammListPanel.hide();
                            showArrow(true);
                        }
                        this.mCarouselLabelPanel.updateSelectedPosition(false);
                        break;
                    } else {
                        handleChannelChangeEvent(true);
                        return true;
                    }
                    break;
                case 20:
                    if (this.mCarouselLabelPanel.isShow() && !this.mCarouselLabelPanel.isListShow()) {
                        LogUtils.m1568d(TAG, "especial case that panel need to consume");
                        onUserInteraction();
                        return true;
                    } else if (this.mCarouselLabelPanel.isShow() || event.getAction() != 0) {
                        onUserInteraction();
                        if (this.mCarouselChannelListPanel.isShow() && this.mProgrammListPanel.isShow() && !this.mProgrammListPanel.isListViewShow()) {
                            this.mProgrammListPanel.hide();
                            showArrow(true);
                        }
                        this.mCarouselLabelPanel.updateSelectedPosition(false);
                        break;
                    } else {
                        handleChannelChangeEvent(false);
                        return true;
                    }
                case 21:
                    if (this.mProgrammListPanel.isShow() && isFirstDownEvent) {
                        this.mCarouselChannelListPanel.requestFocus();
                        this.mProgrammListPanel.hide();
                        showArrow(true);
                        onUserInteraction();
                        return true;
                    } else if (this.mCarouselLabelPanel.isShow() && !this.mCarouselChannelListPanel.isShow() && isFirstDownEvent) {
                        hide();
                        return true;
                    } else if (this.mCarouselLabelPanel.hasFocus() && isFirstDownEvent) {
                        hide();
                        return true;
                    } else if (!this.mCarouselChannelListPanel.hasFocus() || !isFirstDownEvent) {
                        return true;
                    } else {
                        onUserInteraction();
                        this.mCarouselLabelPanel.requestFocus();
                        return true;
                    }
                case 22:
                    if (!this.mCarouselLabelPanel.isShow() && isFirstDownEvent && this.mCarouselChannelListPanel.isEnableShow()) {
                        this.mCarouselLabelPanel.show();
                        this.mChannelInfoOverlay.hide();
                        onUserInteraction();
                        return true;
                    } else if (this.mCarouselChannelListPanel.hasFocus() && !this.mProgrammListPanel.isShow() && isFirstDownEvent) {
                        notifyShowProgramme();
                        showArrow(false);
                        onUserInteraction();
                        return true;
                    } else if (this.mCarouselLabelPanel.hasFocus() && isFirstDownEvent) {
                        this.mCarouselLabelPanel.updateSelectedPosition(true);
                        this.mCarouselChannelListPanel.requestFocus();
                        onUserInteraction();
                        return true;
                    } else if (!this.mProgrammListPanel.isShow()) {
                        return true;
                    } else {
                        onUserInteraction();
                        return true;
                    }
                case 23:
                case 66:
                    if (this.mCarouselLabelPanel.isShow() && !this.mCarouselLabelPanel.isListShow()) {
                        LogUtils.m1568d(TAG, "especial case that panel need to consume");
                        onUserInteraction();
                        return true;
                    } else if (!this.mCarouselLabelPanel.isShow() && isFirstDownEvent) {
                        if (!this.mCarouselChannelListPanel.isEnableShow()) {
                            return true;
                        }
                        this.mCarouselChannelListPanel.hide();
                        this.mChannelInfoOverlay.hide();
                        this.mProgrammListPanel.hide();
                        this.mCarouselLabelPanel.show();
                        onUserInteraction();
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean isShown() {
        if (this.mCarouselLabelPanel != null) {
            return this.mCarouselLabelPanel.isShow();
        }
        return false;
    }

    public void showArrow(boolean visible) {
        if (visible) {
            this.mCarouselArrow.setVisibility(0);
        } else {
            this.mCarouselArrow.setVisibility(8);
        }
    }

    public void setAllChannelDetail(List<CarouselChannelDetail> allDetailInfo, TVChannelCarouselTag tag) {
        this.mCarouselChannelListPanel.setChannelProgramName(allDetailInfo, tag);
        this.mAllDetailInfo.clear();
        this.mAllDetailInfo.addAll(allDetailInfo);
    }

    public void setAllTagList(List<TVChannelCarouselTag> list) {
        if (this.mCarouselLabelPanel != null) {
            this.mCarouselLabelPanel.setAllTagList(list);
        }
    }

    public void setOnRequestChannelInfoListener(OnRequestChannelInfoListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setOnRequestChannelInfoListener() listener=" + listener);
        }
        this.mChannelInfoOverlay.setOnRequestChannelInfoListener(listener);
        this.mCarouselChannelListPanel.setOnRequestChannelInfoListener(listener);
    }

    public void setOnUserChannelChangeListener(OnUserChannelChangeListener listener) {
        this.mCarouselChannelListPanel.setOnUserChannelChangeListener(listener);
    }

    public void setCurrentChannel(TVChannelCarousel channelCarousel) {
        this.mChannelInfoOverlay.setCurrentChannel(channelCarousel);
        this.mCarouselChannelListPanel.setCurrentChannel(channelCarousel);
        this.mCarouselWindowOverlay.setCurrentChannel(channelCarousel);
    }

    public void setOnChannelChangeListener(onChannelChangeListener onChannelChangeListener) {
        this.mCarouselChannelListPanel.setOnChannelChangeListener(onChannelChangeListener);
    }

    public void updateChannelInfo(TVChannelCarousel channelCarousel, boolean needRequest) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateChannelInfo() channelCarousel=" + channelCarousel + ", mAllDetailInfo=" + this.mAllDetailInfo);
        }
        CarouselChannelDetail currentCarouselDetail = null;
        onUserInteraction();
        if (!(channelCarousel == null || ListUtils.isEmpty(this.mAllDetailInfo))) {
            long id = channelCarousel.id;
            Iterator it = this.mAllDetailInfo.iterator();
            while (it.hasNext()) {
                CarouselChannelDetail detailInfo = (CarouselChannelDetail) it.next();
                if (detailInfo != null && String.valueOf(detailInfo.getChannelId()).equals(String.valueOf(id))) {
                    currentCarouselDetail = detailInfo;
                }
            }
        }
        this.mChannelInfoOverlay.updateChannel(channelCarousel, needRequest, currentCarouselDetail);
    }

    public void notifyChannelChangeByIndex(int index) {
        this.mCarouselChannelListPanel.setChannelChangeByIndex(index);
    }

    public void setNetSpeed(long speed) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setNetSpeed(" + speed + ")");
        }
        this.mBufferView.setNetSpeed(speed);
    }

    public void setVideo(IVideo video) {
        this.mProgrammListPanel.setVideo(video);
        this.mCarouselChannelListPanel.setVideo(video);
        this.mChannelInfoOverlay.setVideo(video);
    }

    public void setOnUserVideoChangeListener(OnUserVideoChangeListener videoChangeListener) {
        this.mProgrammListPanel.OnUserVideoChangeListener(videoChangeListener);
    }

    public boolean isProgrammeListShown() {
        if (this.mProgrammListPanel != null) {
            return this.mProgrammListPanel.isListViewShow();
        }
        return false;
    }

    public boolean isChannelListShown() {
        if (this.mCarouselChannelListPanel != null) {
            return this.mCarouselChannelListPanel.isShow();
        }
        return false;
    }

    public boolean isChannelInfoShown() {
        if (this.mChannelInfoOverlay != null) {
            return this.mChannelInfoOverlay.isShow();
        }
        return false;
    }

    public void setOnCarosuelPanelHideListener(OnCarouselPanelHideListener onCarouselHideListener) {
        this.mOnCarouselHideListener = onCarouselHideListener;
    }

    public void removeHandlerMessage() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "removeHandlerMessage()");
        }
        this.mHandler.removeMessages(MSG_HIDE);
    }

    public void onUserInteraction() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onUserInteraction()");
        }
        this.mHandler.removeMessages(MSG_HIDE);
        this.mHandler.sendEmptyMessageDelayed(MSG_HIDE, 10000);
    }

    public void notifyShowProgramme() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "notifyShowProgramme()");
        }
        this.mProgrammListPanel.showProgrameList(this.mCarouselChannelListPanel.notifyRequestProgramme());
    }

    private void handleChannelChangeEvent(boolean isIncrease) {
        int channelChangeOffset;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "handleChannelChangeEvent(isIncrease:" + isIncrease + ")");
        }
        boolean needRequestAllChannelInfo = this.mNeedRequsetAllChannelInfo;
        this.mNeedRequsetAllChannelInfo = false;
        if (isIncrease) {
            channelChangeOffset = 1;
        } else {
            channelChangeOffset = -1;
        }
        this.mCarouselChannelListPanel.notifyChannelInfoChange(channelChangeOffset, needRequestAllChannelInfo, false);
        this.mHandler.removeCallbacks(this.mNotifyChannelChangeRunnable);
        int channelChangeInterval = PlayerDebugUtils.getChannelChangeInterval() != -1 ? PlayerDebugUtils.getChannelChangeInterval() : 400;
        this.mHandler.postDelayed(this.mNotifyChannelChangeRunnable, (long) channelChangeInterval);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "handleChannelChangeEvent channelChangeInterval=" + channelChangeInterval);
        }
    }

    public void clearAd() {
        if (this.mTipView != null) {
            this.mTipView.clearAd();
        }
    }

    public void showVolumePanel(int currentCount) {
    }

    public void hideVolumePanel() {
    }

    public void showBrightnessPanel(int lightCount) {
    }

    public void hideBrightnessPanel() {
    }

    public void showPlayOverFlow(boolean forward, long distance) {
    }

    public void hidePlayOverFlow(boolean forward, long distance) {
    }

    public void clearMediaControllerState() {
    }

    public void setAdStateListener(OnAdStateListener listener) {
        if (this.mTipView != null) {
            this.mTipView.setOnAdStateListener(listener);
        }
    }
}
