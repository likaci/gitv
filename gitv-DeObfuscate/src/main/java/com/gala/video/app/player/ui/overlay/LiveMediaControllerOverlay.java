package com.gala.video.app.player.ui.overlay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C0165R;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_C2;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RPAGETYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.pingback.PingbackStore.STATE;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.widget.views.BufferingView;
import com.gala.video.app.player.ui.widget.views.EnhancedTextView;
import com.gala.video.app.player.ui.widget.views.LiveCountdownView;
import com.gala.video.app.player.ui.widget.views.TipView;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;

public class LiveMediaControllerOverlay extends AbsMediaController {
    private static final String BLOCK_LIVE_INTERACTION = "liveinteract";
    private static final int LIVE_INFOPANEL_SHOW_DURATION = 10000;
    private static final int MSG_HIDE = 4;
    private static final int STATE_AD_PLAYING = 1;
    private static final int STATE_HIDE = 0;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_REAL_PLAYING = 2;
    private static final String TAG = "Player/Ui/LiveMediaControllerOverlay";
    private int VIEWID_BITSTREAM;
    private int VIEWID_BUFFER;
    private int VIEWID_COUNTDOWN;
    private int VIEWID_HINT;
    private int VIEWID_INFO_PANEL;
    private int VIEWID_LIVETAG;
    private int VIEWID_NAME;
    private int VIEWID_TIP;
    private TextView mAdTip;
    private String mBitStreamDefinition;
    private BufferingView mBufferView;
    private boolean mBuffering;
    private Context mContext;
    private LiveCountdownView mCountDownView;
    private onCountDowntimeListener mCountDowntimeListener = new C14752();
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new C14741();
    private boolean mHasSeekBarShown = false;
    private LinearLayout mInfoPanel;
    private boolean mInitized;
    private boolean mIsFullScreen = false;
    private boolean mIsNeedVip;
    private String mLiveName = null;
    private long mLiveStartTime = -1;
    private EnhancedTextView mLiveTag;
    private OnAdStateListener mOnAdStateListener;
    private View mRoot;
    private int mState;
    private TipView mTipView;
    private EnhancedTextView mTxtBitstream;
    private TextView mTxtHint;
    private EnhancedTextView mTxtName;
    private IVideo mVideo;
    private float mZoomRatio;

    class C14741 extends Handler {
        C14741() {
        }

        public void handleMessage(Message msg) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(LiveMediaControllerOverlay.TAG, "handleMessage(" + msg + ")");
            }
            switch (msg.what) {
                case 4:
                    LiveMediaControllerOverlay.this.hideView(LiveMediaControllerOverlay.this.VIEWID_INFO_PANEL, LiveMediaControllerOverlay.this.VIEWID_HINT, LiveMediaControllerOverlay.this.VIEWID_NAME, LiveMediaControllerOverlay.this.VIEWID_BITSTREAM);
                    LiveMediaControllerOverlay.this.mHandler.removeMessages(4);
                    return;
                default:
                    return;
            }
        }
    }

    public interface onCountDowntimeListener {
        void onFinished();
    }

    class C14752 implements onCountDowntimeListener {
        C14752() {
        }

        public void onFinished() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(LiveMediaControllerOverlay.TAG, "onFinished() name=" + LiveMediaControllerOverlay.this.mLiveName);
            }
            LiveMediaControllerOverlay.this.setVideoName(LiveMediaControllerOverlay.this.mLiveName);
        }
    }

    protected enum LiveViewMode {
        FULL_SLICE_NON_VIP,
        FULL_SLICE_VIP,
        FULL_PLAYING_NON_VIP,
        FULL_PLAYING_VIP,
        WINDOW_SLICE_NON_VIP,
        WINDOW_SLICE_VIP,
        WINDOW_PLAYING_NON_VIP,
        WINDOW_PLAYING_VIP
    }

    public LiveMediaControllerOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public LiveMediaControllerOverlay(Context context) {
        super(context);
        this.mContext = context;
    }

    protected int getLayoutId() {
        return PlayerAppConfig.getLiveMediaPlayerLayoutId();
    }

    private void initView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initView()");
        }
        this.mInitized = true;
        this.mRoot = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(getLayoutId(), this);
        this.VIEWID_BUFFER = C1291R.id.playbuffering;
        this.VIEWID_COUNTDOWN = C1291R.id.live_countdown_time;
        this.VIEWID_LIVETAG = C1291R.id.image_live_tag;
        this.VIEWID_INFO_PANEL = C1291R.id.video_info_panel;
        this.VIEWID_HINT = C1291R.id.live_hint_tag;
        this.VIEWID_NAME = C1291R.id.video_name;
        this.VIEWID_BITSTREAM = C1291R.id.bitstream;
        this.VIEWID_TIP = C1291R.id.tip_message;
        this.mBufferView = (BufferingView) this.mRoot.findViewById(this.VIEWID_BUFFER);
        this.mLiveTag = (EnhancedTextView) this.mRoot.findViewById(this.VIEWID_LIVETAG);
        this.mCountDownView = (LiveCountdownView) this.mRoot.findViewById(this.VIEWID_COUNTDOWN);
        this.mInfoPanel = (LinearLayout) this.mRoot.findViewById(this.VIEWID_INFO_PANEL);
        this.mTxtHint = (TextView) this.mRoot.findViewById(this.VIEWID_HINT);
        this.mTxtName = (EnhancedTextView) this.mRoot.findViewById(this.VIEWID_NAME);
        this.mTxtBitstream = (EnhancedTextView) this.mRoot.findViewById(this.VIEWID_BITSTREAM);
        this.mTipView = (TipView) this.mRoot.findViewById(this.VIEWID_TIP);
        this.mTipView.setOnAdStateListener(this.mOnAdStateListener);
        this.mAdTip = (TextView) this.mRoot.findViewById(C1291R.id.txt_tip);
        this.mCountDownView.setCountDowntimeListener(this.mCountDowntimeListener);
        this.mTxtHint.setShadowLayer(8.0f, 0.0f, 3.0f, 1711276032);
        this.mTxtName.setShadowLayer(8.0f, 0.0f, 3.0f, 1711276032);
        int size = this.mCountDownView.getChildCount();
        for (int i = 0; i < size; i++) {
            this.mCountDownView.getChildAt(i).setLayerType(1, null);
        }
        switchScreen(this.mIsFullScreen, this.mZoomRatio);
    }

    protected void sendLiveInteractionPingback(int clickCount) {
        if (this.mVideo != null) {
            String resouceId = this.mVideo.getTvId();
            String c1 = "101221";
            String c2 = "";
            String now_c2 = "";
            String now_c1 = "";
            String state = "";
            if (this.mVideo.getSourceType() == SourceType.LIVE) {
                if (this.mVideo.isTrailer()) {
                    state = WebConstants.STATE_COMING;
                    c2 = String.valueOf(this.mVideo.getAlbum().chnId);
                } else {
                    state = WebConstants.STATE_ONAIR;
                    c2 = this.mVideo.getLiveChannelId();
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendLiveInteractionPingback id=" + this.mVideo.getLiveChannelId());
            }
            PingbackFactory.instance().createPingback(21).addItem(C0165R.R_TYPE(resouceId)).addItem(BLOCK.BLOCK_TYPE(BLOCK_LIVE_INTERACTION)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(clickCount))).addItem(RPAGETYPE.PLAYER).addItem(C1.C1_TYPE(c1)).addItem(C2.C2_TYPE(c2)).addItem(NOW_C1.NOW_C1_TYPE(c1)).addItem(NOW_QPID.NOW_QPID_TYPE(resouceId)).addItem(NOW_C2.NOW_C2_TYPE(c2)).addItem(STATE.STATE_TYPE(state)).post();
        }
    }

    public void onSeekBegin(View view, int i) {
    }

    public void onProgressChanged(View view, int i) {
    }

    public void onSeekEnd(View view, int i) {
    }

    private void changeShowStyle(LiveViewMode mode) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "changeShowStyle mode=" + mode);
        }
        switch (mode) {
            case FULL_SLICE_NON_VIP:
                showView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME);
                this.mCountDownView.show();
                hideView(this.VIEWID_LIVETAG, this.VIEWID_BITSTREAM);
                return;
            case FULL_SLICE_VIP:
                showView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME);
                this.mCountDownView.show();
                hideView(this.VIEWID_LIVETAG, this.VIEWID_BITSTREAM);
                return;
            case FULL_PLAYING_NON_VIP:
                showView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME);
                hideView(this.VIEWID_LIVETAG, this.VIEWID_BITSTREAM);
                return;
            case FULL_PLAYING_VIP:
                showView(this.VIEWID_LIVETAG);
                hideView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_BITSTREAM);
                this.mState = 2;
                return;
            case WINDOW_SLICE_NON_VIP:
            case WINDOW_SLICE_VIP:
                this.mTipView.hide(false);
                hideView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_BITSTREAM, this.VIEWID_LIVETAG);
                this.mCountDownView.show();
                return;
            case WINDOW_PLAYING_NON_VIP:
                this.mTipView.hide(false);
                hideView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_BITSTREAM, this.VIEWID_LIVETAG);
                return;
            case WINDOW_PLAYING_VIP:
                this.mTipView.hide(false);
                hideView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_BITSTREAM);
                showView(this.VIEWID_LIVETAG);
                return;
            default:
                return;
        }
    }

    private synchronized void clearHideViewMessageQueue() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clearHideViewMessageQueue()");
        }
        this.mHandler.removeMessages(4);
    }

    private void updateVideoInfo() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateVideoInfo()");
        }
        if (this.mVideo != null) {
            this.mLiveStartTime = this.mVideo.getProvider().getLiveVideo().getLiveStartTime();
            this.mLiveName = this.mVideo.getProvider().getLiveVideo().getAlbumName();
            this.mIsNeedVip = this.mVideo.getProvider().getLiveVideo().isLiveVipShowTrailer();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "setVideoInfo mIsNeedVip=" + this.mIsNeedVip);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "setVideoInfo mLiveStartTime=" + this.mLiveStartTime);
            }
            if (!StringUtils.isEmpty(this.mBitStreamDefinition)) {
                this.mTxtBitstream.setText(this.mBitStreamDefinition);
            }
            this.mTipView.setVideo(this.mVideo);
            setVideoName(this.mLiveName);
            if (this.mLiveStartTime > 0) {
                this.mCountDownView.setLiveStartTime(this.mLiveStartTime);
            }
        }
    }

    private void setVideoName(String name) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setVideoName name=" + name + ", mLiveStartTime=" + this.mLiveStartTime);
        }
        long currentTime = DeviceUtils.getServerTimeMillis();
        if (this.mLiveStartTime > 0) {
            if (currentTime >= this.mLiveStartTime) {
                this.mTxtHint.setText(this.mContext.getString(C1291R.string.current_play));
            } else {
                this.mTxtHint.setText(this.mContext.getString(C1291R.string.upcoming_live));
            }
        }
        if (!StringUtils.isEmpty((CharSequence) name)) {
            this.mTxtName.setText(name);
        }
        if (this.mState == 3 || this.mState == 2) {
            ensureShowStyle();
            return;
        }
        hideView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_BITSTREAM, this.VIEWID_LIVETAG);
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showTip(" + tip.getContent() + ")" + dumpState());
        }
        if (!this.mInitized) {
            return;
        }
        if (this.mState == 1) {
            if (StringUtils.isEmpty(tip.getContent())) {
                this.mAdTip.setVisibility(8);
            } else if (PlayerAppConfig.isShowTipWhenPlayingAd()) {
                this.mAdTip.setText(tip.getContent());
                this.mAdTip.setVisibility(0);
            }
        } else if (this.mIsFullScreen) {
            this.mTipView.showTip(tip);
        }
    }

    public void hideTip() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideTip()" + dumpState());
        }
        this.mAdTip.setVisibility(8);
        if (this.mInitized && this.mState != 1) {
            this.mTipView.hide(true);
        }
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showPlaying(" + simple + ")" + dumpState());
        }
        if (!this.mInitized) {
            initView();
        }
        if (this.mState == 1) {
            this.mAdTip.setVisibility(8);
        }
        updateVideoInfo();
        showPlaying();
    }

    private void showPlaying() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showPlaying()");
        }
        this.mState = 3;
        if (this.mBuffering) {
            showView(this.VIEWID_BUFFER);
        } else {
            hideView(this.VIEWID_BUFFER);
        }
        ensureShowStyle();
    }

    private void ensureShowStyle() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "ensureShowStyle mLiveStartTime=" + this.mLiveStartTime + ", mIsFullScreen=" + this.mIsFullScreen + ", mIsVip=" + this.mIsNeedVip);
        }
        long currentTime = DeviceUtils.getServerTimeMillis();
        if (this.mIsFullScreen) {
            if (this.mLiveStartTime <= 0) {
                return;
            }
            if (currentTime >= this.mLiveStartTime) {
                if (this.mIsNeedVip) {
                    changeShowStyle(LiveViewMode.FULL_PLAYING_NON_VIP);
                } else {
                    changeShowStyle(LiveViewMode.FULL_PLAYING_VIP);
                }
            } else if (this.mIsNeedVip) {
                changeShowStyle(LiveViewMode.FULL_SLICE_NON_VIP);
            } else {
                changeShowStyle(LiveViewMode.FULL_SLICE_VIP);
            }
        } else if (currentTime >= this.mLiveStartTime) {
            if (this.mIsNeedVip) {
                changeShowStyle(LiveViewMode.WINDOW_PLAYING_NON_VIP);
            } else {
                changeShowStyle(LiveViewMode.WINDOW_PLAYING_VIP);
            }
        } else if (this.mIsNeedVip) {
            changeShowStyle(LiveViewMode.WINDOW_SLICE_NON_VIP);
        } else {
            changeShowStyle(LiveViewMode.WINDOW_SLICE_VIP);
        }
    }

    public void showAdPlaying(int countDownTime) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showAdPlaying(" + countDownTime + ")" + dumpState());
        }
        if (!this.mInitized) {
            initView();
        }
        this.mState = 1;
        if (this.mBuffering) {
            showView(this.VIEWID_BUFFER);
            return;
        }
        hideView(this.VIEWID_BUFFER);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hide()" + dumpState());
        }
        this.mState = 0;
        if (this.mInitized) {
            clearHideViewMessageQueue();
            hideView(this.VIEWID_INFO_PANEL, this.VIEWID_BITSTREAM, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_LIVETAG, this.VIEWID_BUFFER);
            this.mCountDownView.hide();
            this.mAdTip.setVisibility(8);
            this.mTipView.hide(false);
            this.mLiveStartTime = -1;
            this.mLiveName = null;
            this.mIsNeedVip = false;
        }
    }

    private boolean handleHide(int viewId) {
        if (viewId != this.VIEWID_COUNTDOWN) {
            return false;
        }
        this.mCountDownView.hide();
        return true;
    }

    public void showBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showBuffering() mBuffering=" + this.mBuffering);
        }
        this.mBuffering = true;
        showView(this.VIEWID_BUFFER);
    }

    public void hideBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideBuffering() mBuffering=" + this.mBuffering);
        }
        hideView(this.VIEWID_BUFFER);
        this.mBuffering = false;
    }

    public void setBufferPercent(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setBufferPercent(" + percent + ")");
        }
        if (this.mInitized) {
            this.mBufferView.setBufferPercent(percent);
        }
    }

    public void setNetSpeed(long netSpeed) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setNetSpeed(" + netSpeed + ")");
        }
        if (this.mInitized) {
            this.mBufferView.setNetSpeed(netSpeed);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "dispatchKeyEvent(" + event + ")");
        }
        if (!this.mInitized) {
            return false;
        }
        boolean isFirstDownEvent;
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            isFirstDownEvent = true;
        } else {
            isFirstDownEvent = false;
        }
        int key = event.getKeyCode();
        if (!isFirstDownEvent) {
            return false;
        }
        switch (key) {
            case 4:
                return false;
            case 19:
            case 20:
                return (this.mInfoPanel != null && this.mInfoPanel.isShown() && this.mState == 2) ? false : false;
            case 21:
            case 22:
                if (this.mInfoPanel == null || this.mState != 2) {
                    return false;
                }
                if (this.mInfoPanel.isShown()) {
                    onUserInteraction();
                    return false;
                }
                showView(this.VIEWID_INFO_PANEL, this.VIEWID_HINT, this.VIEWID_NAME, this.VIEWID_BITSTREAM);
                onUserInteraction();
                return true;
            case 23:
            case 66:
                if (this.mState == 3 || this.mState == 2) {
                    return this.mTipView.dispatchKeyEvent(event);
                }
                return false;
            case 24:
            case 25:
            case 164:
                if (!LogUtils.mIsDebug) {
                    return false;
                }
                LogUtils.m1571e(TAG, "volume is invalid");
                return false;
            default:
                return false;
        }
    }

    protected void onUserInteraction() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "startAction");
        }
        this.mHandler.removeMessages(4);
        this.mHandler.sendEmptyMessageDelayed(4, 10000);
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "switchScreen(isFullScreen=" + isFullScreen + ", zoomratio=" + zoomRatio);
        }
        this.mIsFullScreen = isFullScreen;
        this.mZoomRatio = zoomRatio;
        if (this.mInitized) {
            if (this.mBufferView != null) {
                this.mBufferView.switchScreen(isFullScreen, zoomRatio);
            }
            if (this.mLiveTag != null) {
                updateLiveTagSize(isFullScreen, zoomRatio);
            }
            if (this.mCountDownView != null) {
                updateLiveCountDownView(isFullScreen, zoomRatio);
            }
            if (this.mTipView != null) {
                this.mTipView.switchScreen(isFullScreen, zoomRatio);
                if (!this.mIsFullScreen) {
                    this.mTipView.hide(false);
                }
            }
            if (this.mAdTip == null) {
                return;
            }
            LayoutParams tipParams;
            if (isFullScreen) {
                tipParams = (LayoutParams) this.mAdTip.getLayoutParams();
                tipParams.height = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_53dp);
                this.mAdTip.setLayoutParams(tipParams);
                this.mAdTip.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_27dp));
                return;
            }
            tipParams = (LayoutParams) this.mAdTip.getLayoutParams();
            tipParams.height = (int) (((float) this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_53dp)) * zoomRatio);
            this.mAdTip.setLayoutParams(tipParams);
            this.mAdTip.setTextSize(0, ((float) this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_27dp)) * zoomRatio);
        }
    }

    private void updateLiveTagSize(boolean isFullScreen, float zoomRatio) {
        float ratio = zoomRatio;
        if (isFullScreen) {
            ratio = 1.0f;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateLiveTagSize(isFullScreen=" + isFullScreen + ", ratio=" + ratio + ")");
        }
        int height = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_height)) * ratio);
        int marginR = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_margin_right)) * ratio);
        int marginT = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_margin_top)) * ratio);
        float textSize = ((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_text_size)) * ratio;
        LayoutParams layoutParams = (LayoutParams) this.mLiveTag.getLayoutParams();
        layoutParams.width = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_width)) * ratio);
        layoutParams.height = height;
        layoutParams.rightMargin = marginR;
        layoutParams.topMargin = marginT;
        this.mLiveTag.setLayoutParams(layoutParams);
        this.mLiveTag.setTextSize(0, textSize);
    }

    private void updateLiveCountDownView(boolean isFullScreen, float zoomRatio) {
        float ratio = zoomRatio;
        if (isFullScreen) {
            ratio = 1.0f;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateLiveCountDownView(isFullScreen=" + isFullScreen + ", ratio=" + ratio + ")");
        }
        int marginTop = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_margin_top)) * ratio);
        int marginRight = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_tag_margin_right)) * ratio);
        LayoutParams layoutParams = (LayoutParams) this.mCountDownView.getLayoutParams();
        layoutParams.width = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_countdown_view_width)) * ratio);
        layoutParams.topMargin = marginTop;
        layoutParams.rightMargin = marginRight;
        this.mCountDownView.setLayoutParams(layoutParams);
        this.mCountDownView.switchScreen(isFullScreen, zoomRatio);
    }

    public void clearAd() {
        if (this.mTipView != null) {
            this.mTipView.clearAd();
        }
    }

    private void showView(int... viewIds) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showView()" + dumpViewId(viewIds));
        }
        if (this.mRoot != null) {
            for (int viewId : viewIds) {
                View view = this.mRoot.findViewById(viewId);
                if (view != null) {
                    view.setVisibility(0);
                }
                if (viewId == this.VIEWID_BITSTREAM && StringUtils.isEmpty(this.mBitStreamDefinition)) {
                    view.setVisibility(8);
                }
            }
        }
    }

    private void hideView(int... viewIds) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideView() " + dumpViewId(viewIds));
        }
        if (this.mRoot != null) {
            for (int viewId : viewIds) {
                View view = this.mRoot.findViewById(viewId);
                if (!(view == null || handleHide(viewId))) {
                    view.setVisibility(8);
                }
            }
        }
    }

    private String dumpViewId(int... viewIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("Views[");
        for (int id : viewIds) {
            sb.append(getViewName(id) + ",");
        }
        sb.append(AlbumEnterFactory.SIGN_STR);
        return sb.toString();
    }

    private String getViewName(int viewId) {
        if (viewId == this.VIEWID_COUNTDOWN) {
            return "LiveCountdown";
        }
        if (viewId == this.VIEWID_BUFFER) {
            return "Buffer";
        }
        if (viewId == this.VIEWID_LIVETAG) {
            return "LiveTag";
        }
        if (viewId == this.VIEWID_INFO_PANEL) {
            return "InfoPanel";
        }
        if (viewId == this.VIEWID_HINT) {
            return "Hint";
        }
        if (viewId == this.VIEWID_NAME) {
            return "Name";
        }
        if (viewId == this.VIEWID_BITSTREAM) {
            return "Bitstream";
        }
        return "unknown";
    }

    public String dumpState() {
        return " " + super.toString() + "[mState=" + this.mState + ", mBuffering=" + this.mBuffering + ", mHasSeekBarShown=" + this.mHasSeekBarShown + AlbumEnterFactory.SIGN_STR;
    }

    public void updateBitStreamDefinition(String bitStream) {
        this.mBitStreamDefinition = bitStream;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateBitStreamDefinition(" + bitStream + ")");
        }
        if (this.mInitized && !StringUtils.isEmpty((CharSequence) bitStream)) {
            this.mTxtBitstream.setText(bitStream);
        }
    }

    public void clearMediaControllerState() {
    }

    public void showBottomAndTop(int duration) {
    }

    public void hideBottomAndTop(int duration) {
    }

    public void hideVolumePanel() {
    }

    public void showVolumePanel(int currentCount) {
    }

    public void hideBrightnessPanel() {
    }

    public void showBrightnessPanel(int lightCount) {
    }

    public void showPlayOverFlow(boolean forward, long distance) {
    }

    public void hidePlayOverFlow(boolean forward, long distance) {
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
    }

    public void setHeadAndTailProgress(int headProgress, int tailProgress) {
    }

    public void setProgress(int progress) {
    }

    public void setSecondaryProgress(int progress) {
    }

    public void showPaused() {
    }

    public void setThreeDimensional(boolean enable) {
    }

    public void showPanel(int duration) {
    }

    public void setSubtitle(String subtitle) {
    }

    public boolean showSeekBar() {
        return false;
    }

    public int getProgress() {
        return 0;
    }

    public void setPrimary(boolean b) {
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setVideo=" + video);
        }
        this.mVideo = video;
    }

    public void showMiddleAdEnd() {
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
        this.mOnAdStateListener = listener;
    }

    public void hideGuideTip() {
    }
}
