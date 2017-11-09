package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.sdk.player.TipType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.widget.views.BufferingView;
import com.gala.video.app.player.ui.widget.views.EnhancedImageView;
import com.gala.video.app.player.ui.widget.views.EnhancedTextView;
import com.gala.video.app.player.ui.widget.views.SubtitleView;
import com.gala.video.app.player.ui.widget.views.TimedSeekBar;
import com.gala.video.app.player.ui.widget.views.TipView;
import com.gala.video.app.player.utils.ImageViewUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.project.Project;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class AbsMediaControllerStrategy implements IMediaControllerStrategy {
    protected static final int HEADER_SHOW_DURATION = 2000;
    protected static final int MSG_HEADER_HIDE = 3;
    protected static final int MSG_HIDE = 1;
    protected static final int MSG_SHOW_CLOCK = 2;
    protected static final int MSG_TIME_HIDE = 4;
    protected static final int SHOW_DURATION = 5000;
    protected static final int STATE_AD_PLAYING = 11;
    protected static final int STATE_HIDE = 10;
    protected static final int STATE_MIDDLEAD_END = 14;
    protected static final int STATE_PAUSED = 13;
    protected static final int STATE_PLAYING = 12;
    private static final String TAG = "Player/ui/AbsMediaControllerStrategy";
    private static Calendar sCalendar;
    protected int VIEWID_ADTIP;
    protected int VIEWID_BITSTREAM;
    protected int VIEWID_BUFFER;
    protected int VIEWID_HDR;
    protected int VIEWID_LOGO;
    protected int VIEWID_SEEKBAR;
    protected int VIEWID_SUBTITLE;
    protected int VIEWID_SYSTIME;
    protected int VIEWID_TIP;
    protected int VIEWID_VIDEONAME;
    protected TextView mAdTip;
    protected EnhancedTextView mBitStream;
    private String mBitStreamDefinition;
    protected BufferingView mBufferView;
    protected boolean mBuffering;
    protected Context mContext;
    private IVideo mCurrentVideo;
    protected final SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    protected EnhancedTextView mHDR;
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AbsMediaControllerStrategy.TAG, "handleMessage(" + msg + ")");
            }
            switch (msg.what) {
                case 1:
                    AbsMediaControllerStrategy.this.doHide();
                    return;
                case 2:
                    if (AbsMediaControllerStrategy.this.mMediaStateBean.getPlayerState() != 11 && AbsMediaControllerStrategy.this.mMediaStateBean.getPlayerState() != 10) {
                        int hideDelay = msg.arg1;
                        if (hideDelay < 1) {
                            hideDelay = 5000;
                        }
                        AbsMediaControllerStrategy.this.hideView(AbsMediaControllerStrategy.this.VIEWID_LOGO);
                        AbsMediaControllerStrategy.this.showView(0, AbsMediaControllerStrategy.this.VIEWID_SYSTIME);
                        AbsMediaControllerStrategy.this.mHandler.sendMessageDelayed(AbsMediaControllerStrategy.this.mHandler.obtainMessage(4), (long) hideDelay);
                        return;
                    }
                    return;
                case 3:
                    AbsMediaControllerStrategy.this.hideHeader();
                    return;
                case 4:
                    AbsMediaControllerStrategy.this.hideSystemTime();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mIsPrimary;
    protected int mLayoutId = R.layout.player_minitv_layout_control;
    ITipStateListener mListener = new ITipStateListener() {
        public void setTipState(boolean show) {
            if (!show && AbsMediaControllerStrategy.this.mMediaStateBean != null) {
                AbsMediaControllerStrategy.this.mMediaStateBean.setTipShown(show);
            }
        }
    };
    protected EnhancedImageView mLogo;
    protected float mLogoViewHeightFullSize;
    protected float mLogoViewHeightWindowSize;
    protected float mLogoViewWidthFullSize;
    protected float mLogoViewWidthWindowSize;
    protected MediaControllerStateBean mMediaStateBean = new MediaControllerStateBean();
    protected ImageView mPause;
    protected View mRoot;
    protected TimedSeekBar mSeekBar;
    protected boolean mShowHeader;
    protected int mState;
    private SubtitleView mSubtitleView;
    protected EnhancedTextView mSysTime;
    protected float mSysTimeFullSize;
    protected float mSysTimeWindowSize;
    private ImageView mTextBg;
    protected boolean mTipShown;
    protected TipView mTipView;
    protected EnhancedTextView mVideoName;

    public interface ITipStateListener {
        void setTipState(boolean z);
    }

    public void initView(Context context, View root) {
        this.mContext = context;
        this.mRoot = root;
        this.VIEWID_SEEKBAR = R.id.seekbar;
        this.VIEWID_VIDEONAME = R.id.video_name;
        this.VIEWID_SYSTIME = R.id.play_sys_time_text;
        this.VIEWID_LOGO = R.id.play_logo;
        this.VIEWID_BUFFER = R.id.playbuffering;
        this.VIEWID_SUBTITLE = R.id.play_subtitle;
        this.VIEWID_TIP = R.id.tip_message;
        this.VIEWID_BITSTREAM = R.id.bitstream;
        this.VIEWID_HDR = R.id.hdr;
        this.VIEWID_ADTIP = R.id.txt_tip;
        this.mTipView = (TipView) this.mRoot.findViewById(R.id.tip_message);
        this.mTipView.setTipStateListener(this.mListener);
        this.mTipView.setPrimary(this.mIsPrimary);
        this.mSeekBar = (TimedSeekBar) this.mRoot.findViewById(R.id.seekbar);
        this.mVideoName = (EnhancedTextView) this.mRoot.findViewById(R.id.video_name);
        this.mSysTime = (EnhancedTextView) this.mRoot.findViewById(R.id.play_sys_time_text);
        this.mBitStream = (EnhancedTextView) this.mRoot.findViewById(R.id.bitstream);
        this.mHDR = (EnhancedTextView) this.mRoot.findViewById(R.id.hdr);
        this.mBufferView = (BufferingView) this.mRoot.findViewById(R.id.playbuffering);
        this.mPause = (ImageView) this.mRoot.findViewById(R.id.img_pause_button);
        this.mTextBg = (ImageView) this.mRoot.findViewById(R.id.text_bg_id);
        this.mSubtitleView = (SubtitleView) this.mRoot.findViewById(R.id.play_subtitle);
        this.mLogo = (EnhancedImageView) this.mRoot.findViewById(R.id.play_logo);
        this.mAdTip = (TextView) this.mRoot.findViewById(R.id.txt_tip);
        this.mLogoViewWidthFullSize = (float) context.getResources().getDimensionPixelSize(R.dimen.gala_logo_width);
        this.mLogoViewHeightFullSize = (float) context.getResources().getDimensionPixelSize(R.dimen.gala_logo_height);
        this.mSysTimeFullSize = (float) context.getResources().getDimensionPixelSize(R.dimen.video_name_text_size);
        requestWaterLogo();
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPlaying(" + simple + ")" + dumpState());
        }
        this.mState = 12;
        this.mMediaStateBean.setPlayerState(this.mState);
        hideView(this.VIEWID_ADTIP);
    }

    public void showAdPlaying(int countDownTime) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showAdPlaying(" + countDownTime + ")" + dumpState());
        }
        this.mState = 11;
        this.mMediaStateBean.setPlayerState(this.mState);
        hidePlayOverlay();
        clearHideViewMessageQueue();
    }

    private void hidePlayOverlay() {
        if (this.mTipView != null) {
            this.mTipShown = false;
            this.mMediaStateBean.setTipShown(this.mTipShown);
            this.mTipView.hide(false);
        }
        if (this.mLogo != null) {
            hideView(this.VIEWID_LOGO);
        }
        if (this.mSysTime != null) {
            hideView(this.VIEWID_SYSTIME);
        }
        if (this.mSubtitleView != null) {
            hideView(this.VIEWID_SUBTITLE);
        }
        if (this.mVideoName != null && this.mVideoName.isShown()) {
            hideView(this.VIEWID_VIDEONAME, this.VIEWID_BITSTREAM);
        }
        if (this.mHDR != null) {
            hideView(this.VIEWID_HDR);
        }
        if (this.mSeekBar != null && this.mSeekBar.isShown()) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "mSeekBar here()");
            }
            this.mSeekBar.hide();
        }
    }

    public void showMiddleAdEnd() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showMiddleAdEnd()");
        }
        this.mState = 14;
        this.mMediaStateBean.setPlayerState(this.mState);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hide()" + dumpState());
        }
        this.mState = 10;
        this.mMediaStateBean.setPlayerState(this.mState);
        sCalendar = null;
        doHide();
    }

    protected void doHide() {
    }

    public boolean isShown() {
        if (this.mRoot != null) {
            return this.mRoot.isShown();
        }
        return false;
    }

    public void showHeader() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showHeader() mShowHeader=" + this.mShowHeader);
        }
        this.mShowHeader = true;
        this.mMediaStateBean.setShowHeader(this.mShowHeader);
    }

    public void hideHeader() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideHeader() mShowHeader=" + this.mShowHeader);
        }
        this.mShowHeader = false;
        this.mMediaStateBean.setShowHeader(this.mShowHeader);
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setVideo()");
        }
        if (video != null) {
            CharSequence name;
            this.mCurrentVideo = video;
            this.mTipView.setVideo(video);
            String albumName = video.getAlbumName();
            CharSequence tvName = video.getTvName();
            if (StringUtils.isEmpty(tvName)) {
                Object name2 = albumName;
            } else {
                name = tvName;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setVideo(" + name + ")");
            }
            if (!StringUtils.isEmpty(name)) {
                this.mVideoName.setShadowLayer(8.0f, 0.0f, 3.0f, 1711276032);
                this.mVideoName.setText(name);
            }
        }
    }

    public void setSubtitle(String subtitle) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setSubtitle(" + subtitle + ")");
        }
        showView(0, this.VIEWID_SUBTITLE);
        this.mSubtitleView.setSubtitle(subtitle);
    }

    public void showBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showBuffering() mBuffering=" + this.mBuffering);
        }
        this.mBuffering = true;
        this.mMediaStateBean.setBuffering(this.mBuffering);
        showView(0, this.VIEWID_BUFFER);
    }

    public void hideBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideBuffering() mBuffering=" + this.mBuffering);
        }
        hideView(this.VIEWID_BUFFER);
        this.mBuffering = false;
        this.mMediaStateBean.setBuffering(this.mBuffering);
    }

    public void showBottomAndTop(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showBottomAndTop(" + duration + ")" + dumpState());
        }
    }

    public void hideBottomAndTop(int duration) {
    }

    public void setBufferPercent(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setBufferPercent(" + percent + ")");
        }
        if (this.mBufferView != null) {
            this.mBufferView.setBufferPercent(percent);
        }
    }

    public void setNetSpeed(long bytePerSecond) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setNetSpeed(" + bytePerSecond + ")");
        }
        if (this.mBufferView != null) {
            this.mBufferView.setNetSpeed(bytePerSecond);
        }
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showTip(" + tip + ")" + dumpState());
        }
        if (this.mMediaStateBean.getPlayerState() == 11) {
            if (tip != null && tip.getTipType().isSupportPersistent()) {
                return;
            }
            if (tip != null && tip.getTipType().getConcreteTipType() == TipType.CONCRETE_TYPE_AD_END) {
                hideView(this.VIEWID_ADTIP);
            } else if (PlayerAppConfig.isShowTipWhenPlayingAd() && this.mAdTip != null && tip != null) {
                this.mAdTip.setText(tip.getContent());
                showView(0, this.VIEWID_ADTIP);
            }
        } else if (tip == null || tip.getTipType().getConcreteTipType() != TipType.CONCRETE_TYPE_AD_END) {
            this.mTipShown = true;
            this.mMediaStateBean.setTipShown(this.mTipShown);
            if (this.mTipView != null) {
                this.mTipView.showTip(tip);
            }
            if (this.mMediaStateBean.getPlayerState() != 13) {
                hideView(this.VIEWID_SEEKBAR);
            }
        } else {
            hideView(this.VIEWID_ADTIP);
        }
    }

    public void hideTip() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideTip()" + dumpState());
        }
        this.mTipShown = false;
        this.mMediaStateBean.setTipShown(this.mTipShown);
        hideView(this.VIEWID_ADTIP);
        if (!(this.mTipView == null || this.mState == 11)) {
            this.mTipView.hide(true);
        }
        if (this.mMediaStateBean.getPlayerState() == 13) {
            showView(0, this.VIEWID_SEEKBAR);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isTipHandled = this.mTipView.dispatchKeyEvent(event);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchKeyEvent() isTipHandled=" + isTipHandled);
        }
        return isTipHandled;
    }

    public void setThreeDimensional(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setThreeDimensional(" + enable + ")");
        }
        this.mSeekBar.setThreeDimensional(enable);
        this.mVideoName.setThreeDimensional(enable);
        this.mSysTime.setThreeDimensional(enable);
        this.mBufferView.setThreeDimensional(enable);
        this.mTipView.setThreeDimensional(enable);
        this.mSubtitleView.setThreeDimensional(enable);
        this.mLogo.setThreeDimensional(enable);
        this.mBitStream.setThreeDimensional(enable);
        this.mHDR.setThreeDimensional(enable);
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setMaxProgress(" + maxProgress + ", " + maxSeekableProgress + ")");
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.setMaxProgress(maxProgress, maxSeekableProgress);
        }
    }

    public void setHeadAndTailProgress(int headProgress, int tailProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setHeadAndTailProgress(" + headProgress + ", " + tailProgress + ")");
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.setHeadAndTailProgress(headProgress, tailProgress);
        }
    }

    public void setProgress(int progress) {
        if (this.mSeekBar != null) {
            this.mSeekBar.setProgress(progress);
        }
    }

    public void setSecondaryProgress(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setSecondaryProgress(" + percent + ")");
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.setSecondaryProgress(percent);
        }
    }

    public int getProgress() {
        int progress = 0;
        if (this.mSeekBar != null) {
            progress = this.mSeekBar.getProgress();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getProgress() return " + progress);
        }
        return progress;
    }

    public void handleUpdateTimeMessage() {
        if (this.mState != 11 && isShown()) {
            checkSysTime();
        }
    }

    protected void showPlaying(boolean simple, int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPlaying(" + simple + ", " + duration + ")" + dumpState());
        }
        if (this.mMediaStateBean.isBuffering()) {
            showView(0, this.VIEWID_BUFFER);
            return;
        }
        hideView(this.VIEWID_BUFFER);
    }

    public void showPanel(int duration) {
    }

    public void showPanelWithTip(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPanelWithTip(" + duration + ")" + dumpState());
        }
        clearHideViewMessageQueue();
        checkSysTime();
        showView(duration, this.VIEWID_TIP);
    }

    public void showPanelWithoutTip(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPanelWithoutTip(" + duration + ")" + dumpState());
        }
        clearHideViewMessageQueue();
        hideView(this.VIEWID_LOGO, this.VIEWID_TIP);
        checkSysTime();
        if (this.mSeekBar != null) {
            this.mSeekBar.stopTipMode();
        }
        if (this.mPause != null) {
            this.mPause.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.player_pause_button));
        }
    }

    public void updateBitStreamDefinition(String bitStream) {
        this.mBitStreamDefinition = bitStream;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateBitStreamDefinition(" + bitStream + ")");
        }
        if (!StringUtils.isEmpty((CharSequence) bitStream)) {
            this.mBitStream.setText(bitStream);
            this.mHDR.setText(this.mContext.getResources().getString(R.string.hdr_panel_text));
        }
        if (this.mVideoName == null || !this.mVideoName.isShown()) {
            hideView(this.VIEWID_BITSTREAM, this.VIEWID_HDR);
            return;
        }
        showView(0, this.VIEWID_BITSTREAM, this.VIEWID_HDR);
    }

    public void clearMediaControllerState() {
        this.mMediaStateBean.setBuffering(false);
        this.mMediaStateBean.setIsShown(false);
        this.mMediaStateBean.setSeeking(false);
        this.mMediaStateBean.setShowHeader(false);
        this.mMediaStateBean.setTipShown(false);
    }

    protected void showView(int duration, int... viewIds) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showView(" + duration + ") " + dumpViewId(viewIds));
        }
        if (this.mRoot != null) {
            for (int viewId : viewIds) {
                View view = this.mRoot.findViewById(viewId);
                if (view != null && extraCheck(viewId)) {
                    if (viewId == this.VIEWID_VIDEONAME || viewId == this.VIEWID_SYSTIME) {
                        this.mTextBg.setVisibility(0);
                    }
                    if (viewId != this.VIEWID_LOGO) {
                        view.setVisibility(0);
                    } else if (!(Project.getInstance().getBuild().isLitchi() || Project.getInstance().getBuild().isNoLogoUI())) {
                        view.setVisibility(0);
                    }
                    if (viewId == this.VIEWID_BITSTREAM && StringUtils.isEmpty(this.mBitStreamDefinition)) {
                        view.setVisibility(8);
                    }
                    if (!(this.mCurrentVideo == null || this.mCurrentVideo.getCurrentBitStream() == null)) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(TAG, "showView( getCurrentBitStream ) " + this.mCurrentVideo.getCurrentBitStream());
                        }
                        if (viewId == this.VIEWID_HDR && this.mCurrentVideo.getCurrentBitStream().getDynamicRangeType() == 0) {
                            view.setVisibility(8);
                        }
                    }
                }
            }
            if (duration > 0) {
                synchronized (this) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, viewIds), (long) duration);
                }
            }
        }
    }

    protected void hideView(int... viewIds) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideView() " + dumpViewId(viewIds));
        }
        if (this.mRoot != null) {
            for (int viewId : viewIds) {
                View view = this.mRoot.findViewById(viewId);
                if (view != null) {
                    if (viewId == this.VIEWID_VIDEONAME || viewId == this.VIEWID_SYSTIME) {
                        this.mTextBg.setVisibility(8);
                    }
                    view.setVisibility(8);
                }
            }
        }
    }

    public MediaControllerStateBean getMediaControllerBean() {
        return this.mMediaStateBean;
    }

    public void setMediaControllerBean(MediaControllerStateBean bean) {
        this.mMediaStateBean = bean;
    }

    private boolean extraCheck(int viewId) {
        if (this.VIEWID_LOGO == viewId && this.mSysTime.isShown()) {
            return false;
        }
        if (this.VIEWID_SYSTIME == viewId) {
            updateSysTime();
            return true;
        } else if (this.VIEWID_LOGO == viewId && Project.getInstance().getBuild().isNoLogoUI()) {
            return false;
        } else {
            return true;
        }
    }

    private void updateSysTime() {
        this.mSysTime.setText(this.mDateFormat.format(new Date(System.currentTimeMillis())));
        this.mSysTime.setShadowLayer(8.0f, 0.0f, 3.0f, 1711276032);
    }

    protected void checkSysTime() {
        if (sCalendar == null) {
            sCalendar = Calendar.getInstance();
        }
        sCalendar.setTime(new Date());
        int min = sCalendar.get(12);
        int sec = sCalendar.get(13);
        if (min == 59 || min == 0) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "checkSysTime min is ->" + min + " sec-> " + sec);
            }
            if (min == 59 && sec < 30) {
                int showDelay = (30 - sec) * 1000;
                if (!this.mHandler.hasMessages(2)) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2, (showDelay + 60) * 1000, 0), (long) showDelay);
                }
            } else if (min != 59 || sec < 30) {
                if (min == 0 && sec < 30 && !this.mHandler.hasMessages(2)) {
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(2, (30 - sec) * 1000, 0));
                }
            } else if (!this.mHandler.hasMessages(2)) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(2, ((60 - sec) + 30) * 1000, 0));
            }
        } else if (this.mMediaStateBean.getPlayerState() == 13) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        }
    }

    public synchronized void clearHideViewMessageQueue() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clearHideViewMessageQueue()");
        }
        this.mHandler.removeMessages(1);
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (this.mTipView != null) {
            this.mTipView.switchScreen(isFullScreen, zoomRatio);
        }
        if (this.mBufferView != null) {
            this.mBufferView.switchScreen(isFullScreen, zoomRatio);
        }
        if (this.mAdTip == null) {
            return;
        }
        if (isFullScreen) {
            LayoutParams tipParams = (LayoutParams) this.mAdTip.getLayoutParams();
            tipParams.height = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_53dp);
            this.mAdTip.setLayoutParams(tipParams);
            this.mAdTip.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_27dp));
            return;
        }
        tipParams = (LayoutParams) this.mAdTip.getLayoutParams();
        tipParams.height = (int) (((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_53dp)) * zoomRatio);
        this.mAdTip.setLayoutParams(tipParams);
        this.mAdTip.setTextSize(0, ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_27dp)) * zoomRatio);
    }

    public String dumpState() {
        return " " + super.toString() + "[mState=" + this.mMediaStateBean.getPlayerState() + ", mShowHeader=" + this.mMediaStateBean.isShowHeader() + ", mBuffering=" + this.mMediaStateBean.isBuffering() + ", mTipShown=" + this.mMediaStateBean.isTipShown() + ", mSeeking=" + this.mMediaStateBean.isSeeking() + ", mIsShown=" + this.mMediaStateBean.isShown() + AlbumEnterFactory.SIGN_STR;
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

    protected String getViewName(int viewId) {
        if (viewId == this.VIEWID_VIDEONAME) {
            return "VideoName";
        }
        if (viewId == this.VIEWID_LOGO) {
            return "Logo";
        }
        if (viewId == this.VIEWID_SYSTIME) {
            return "Systime";
        }
        if (viewId == this.VIEWID_SEEKBAR) {
            return "Seekbar";
        }
        if (viewId == this.VIEWID_BUFFER) {
            return "Buffer";
        }
        if (viewId == this.VIEWID_SUBTITLE) {
            return "Subtitle";
        }
        if (viewId == this.VIEWID_BITSTREAM) {
            return "Bitstream";
        }
        if (viewId == this.VIEWID_HDR) {
            return "hdr";
        }
        if (viewId == this.VIEWID_TIP) {
            return "Tip";
        }
        return "unknown";
    }

    public void clearAllMessage() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void showVolumePanel(int currentCount) {
    }

    public void hideVolumePanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideVolumePanel()");
        }
    }

    public void showBrightnessPanel(int lightCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showBrightnessPanel() lightCount " + lightCount);
        }
    }

    public void hideBrightnessPanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideBrightnessPanel()");
        }
    }

    public void hidePlayOverFlow(boolean forward, long distance) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hidePlayOverFlow() distance (" + distance + ")");
        }
    }

    public void showPlayOverFlow(boolean forward, long distance) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPlayOverFlow()");
        }
    }

    public boolean showSeekBar() {
        if (this.mSeekBar != null) {
            return this.mSeekBar.isDragging();
        }
        return false;
    }

    private void requestWaterLogo() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            String url = model.getWaterUrlString();
            ImageViewUtils.updateImageView(this.mLogo, url, this.mHandler);
            LogUtils.d(TAG, "showWaterLogo pic=" + url);
        }
    }

    public void clearAd() {
        if (this.mTipView != null) {
            this.mTipView.clearAd();
        }
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
        if (this.mTipView != null) {
            this.mTipView.setOnAdStateListener(listener);
        }
    }

    private void hideSystemTime() {
        if (this.mMediaStateBean != null && this.mMediaStateBean.getPlayerState() != 13) {
            hideView(this.VIEWID_SYSTIME);
            this.mHandler.removeMessages(4);
        }
    }

    public void onSeekBegin(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onSeekBegin(" + i + ")");
        }
        this.mMediaStateBean.setSeeking(true);
        this.mSeekBar.onSeekBegin(view, i);
    }

    public void onProgressChanged(View view, int i) {
    }

    public void onSeekEnd(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onSeekEnd(" + i + ")");
        }
        this.mMediaStateBean.setSeeking(false);
        this.mSeekBar.onSeekEnd(view, i);
        if (this.mMediaStateBean.isTipShown()) {
            showView(0, this.VIEWID_TIP);
            hideView(this.VIEWID_BITSTREAM, this.VIEWID_SYSTIME, this.VIEWID_VIDEONAME, this.VIEWID_HDR);
            return;
        }
        hideView(this.VIEWID_TIP);
    }

    public void setPrimary(boolean b) {
        this.mIsPrimary = b;
    }

    public void hideGuideTip() {
        if (this.mTipView != null) {
            this.mTipView.hideGuide();
        }
    }
}
