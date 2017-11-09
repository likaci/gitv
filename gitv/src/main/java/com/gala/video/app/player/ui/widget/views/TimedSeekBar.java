package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.sdk.player.ISeekOverlay;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.widget.views.ISeekBar.OnSeekBarChangeListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class TimedSeekBar extends RelativeLayout implements IThreeDimensional, ISeekOverlay {
    private static final int MIN_HEAD_TIME = 8000;
    protected static final int PROGRESS_SCALE = 1000;
    private static final int SEEKTO_PROGRESS = 1;
    private static final int SEEK_DELAY_TIME = 400;
    private static final int SEEK_OVERFLOW = 2;
    private static final String TAG = "Player/Ui/TimedSeekBar";
    private final Runnable mCheckLayoutRunnable = new Runnable() {
        public void run() {
            TimedSeekBar.this.checkHeadAndTailLayout();
        }
    };
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TimedSeekBar.TAG, "handleMessage(" + msg + ") mIsSeeking=" + TimedSeekBar.this.mIsSeeking);
                    }
                    TimedSeekBar.this.mIsSeeking = false;
                    return;
                case 2:
                    Integer changeprogess = msg.obj;
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TimedSeekBar.TAG, "handleMessage(" + msg + ") mIsSeeking=" + TimedSeekBar.this.mIsSeeking + ", getProgress= " + TimedSeekBar.this.mSeekBar.getProgress() + ",changeprogess= " + changeprogess);
                    }
                    TimedSeekBar.this.mIsSeeking = false;
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mHaveHeader = false;
    private boolean mHaveTailer = false;
    protected int mHeaderProgress = -1;
    protected ImageView mHeaderView;
    private boolean mIsDragging;
    protected boolean mIsSeeking;
    private boolean mIsTipMode = false;
    protected boolean mIsTouchMove = false;
    protected int mMaxProgress = -1;
    private int mMaxSeekableProgress;
    private ImageView mPauseButton;
    private int mPauseWidth;
    private int mProgress;
    protected ISeekBar mSeekBar;
    private final OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {
        boolean mIsFromUser;
        private int mSeekStart;

        public void onStopTrackingTouch(ISeekBar seekBar) {
            int seekTo;
            if (LogUtils.mIsDebug) {
                LogUtils.d(TimedSeekBar.TAG, "onStopTrackingTouch(" + this.mIsFromUser + ")");
            }
            TimedSeekBar.this.mIsDragging = false;
            int progress = seekBar.getProgress() * 1000;
            if (progress <= TimedSeekBar.this.mMaxSeekableProgress || progress > TimedSeekBar.this.mMaxProgress) {
                seekTo = TimedSeekBar.this.mMaxProgress > progress ? progress : TimedSeekBar.this.mMaxProgress;
            } else if (this.mSeekStart > TimedSeekBar.this.mMaxSeekableProgress) {
                seekTo = progress;
            } else {
                seekTo = TimedSeekBar.this.mMaxSeekableProgress;
            }
            TimedSeekBar.this.setProgress(seekTo, true);
            if (!TimedSeekBar.this.mIsSeeking) {
                TimedSeekBar.this.mIsSeeking = true;
            }
            TimedSeekBar.this.mHandler.removeMessages(1);
            TimedSeekBar.this.mHandler.sendEmptyMessageDelayed(1, 400);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TimedSeekBar.TAG, "onStopTrackingTouch() progress=" + progress + ", seekTo=" + seekTo);
            }
            TimedSeekBar.this.mIsTouchMove = false;
        }

        public void onStartTrackingTouch(ISeekBar seekBar) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TimedSeekBar.TAG, "onStartTrackingTouch(" + seekBar + ")");
            }
            this.mSeekStart = seekBar.getProgress();
            TimedSeekBar.this.mIsDragging = true;
        }

        public void onProgressChanged(ISeekBar seekBar, int progress, boolean fromUser) {
            int seekTo;
            if (LogUtils.mIsDebug) {
                LogUtils.d(TimedSeekBar.TAG, "onProgressChanged(" + progress + ", " + fromUser + ")");
            }
            this.mIsFromUser = fromUser;
            TimedSeekBar.this.mIsDragging = true;
            TimedSeekBar.this.mIsTouchMove = true;
            if (LogUtils.mIsDebug) {
                LogUtils.d(TimedSeekBar.TAG, "onProgressChanged(" + TimedSeekBar.this.mIsDragging + ")");
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TimedSeekBar.TAG, "onProgressChanged(" + this.mIsFromUser + ")");
            }
            progress *= 1000;
            if (progress <= TimedSeekBar.this.mMaxSeekableProgress || progress > TimedSeekBar.this.mMaxProgress) {
                seekTo = TimedSeekBar.this.mMaxProgress > progress ? progress : TimedSeekBar.this.mMaxProgress;
            } else if (this.mSeekStart > TimedSeekBar.this.mMaxSeekableProgress) {
                seekTo = progress;
            } else {
                seekTo = TimedSeekBar.this.mMaxSeekableProgress;
            }
            TimedSeekBar.this.setProgress(seekTo, true);
        }
    };
    protected FrameLayout mSeekBarLayout;
    private int mSeekbarLeftMargin;
    private int mSeekbarWidth;
    protected int mTailerProgress = -1;
    protected ImageView mTailerView;
    private EnhancedTextView mTextVideoTime;
    private int mTimedLeftMargin;
    private EnhancedTextView mTxtTotalTime;

    public TimedSeekBar(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TimedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TimedSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    public void setThreeDimensional(boolean enable) {
        this.mTextVideoTime.setThreeDimensional(enable);
        this.mTxtTotalTime.setThreeDimensional(enable);
        if (enable) {
            LayoutParams timeParams = (LayoutParams) this.mTextVideoTime.getLayoutParams();
            timeParams.leftMargin = this.mTimedLeftMargin / 2;
            this.mTextVideoTime.setLayoutParams(timeParams);
            LayoutParams buttonParams = (LayoutParams) this.mPauseButton.getLayoutParams();
            buttonParams.width = this.mPauseWidth / 2;
            buttonParams.leftMargin = this.mSeekbarLeftMargin / 2;
            this.mPauseButton.setLayoutParams(buttonParams);
            LayoutParams seekbarParams = (LayoutParams) this.mSeekBarLayout.getLayoutParams();
            seekbarParams.width = this.mSeekbarWidth / 2;
            seekbarParams.leftMargin = this.mSeekbarLeftMargin / 2;
            this.mSeekBarLayout.setLayoutParams(seekbarParams);
        }
    }

    private int getLayoutId() {
        return PlayerAppConfig.getSeekBarLayoutId();
    }

    private void initView() {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(getLayoutId(), this);
        this.mSeekBarLayout = (FrameLayout) findViewById(R.id.layout_seekbar);
        this.mSeekBar = (ISeekBar) findViewById(R.id.play_seekbar);
        this.mSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mTextVideoTime = (EnhancedTextView) findViewById(R.id.play_text_video_time);
        this.mTxtTotalTime = (EnhancedTextView) findViewById(R.id.play_text_total_time);
        this.mHeaderView = (ImageView) findViewById(R.id.view_header);
        this.mTailerView = (ImageView) findViewById(R.id.view_tail);
        this.mPauseButton = (ImageView) findViewById(R.id.img_pause_button);
        this.mTimedLeftMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.player_seekbar_time_left_margin);
        this.mSeekbarWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.player_seekbar_width);
        this.mSeekbarLeftMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.player_seekbar_left_margin);
        this.mPauseWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.player_button_width);
        setVisibility(8);
    }

    public void startTipMode() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "startTipMode() mIsTipMode=" + this.mIsTipMode);
        }
        this.mIsTipMode = true;
        this.mTextVideoTime.setVisibility(4);
        this.mTxtTotalTime.setVisibility(4);
        this.mSeekBarLayout.setVisibility(8);
        setVisibility(0);
    }

    public void stopTipMode() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "stopTipMode() mIsTipMode=" + this.mIsTipMode);
        }
        this.mIsTipMode = false;
        this.mTextVideoTime.setVisibility(0);
        this.mTxtTotalTime.setVisibility(0);
        this.mSeekBarLayout.setVisibility(0);
    }

    public boolean isTipMode() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isTipMode() return " + this.mIsTipMode);
        }
        return this.mIsTipMode;
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setMaxProgress(" + maxProgress + ", " + maxSeekableProgress + ") mMaxProgress=" + this.mMaxProgress + ", mMaxSeekableProgress=" + this.mMaxSeekableProgress);
        }
        if (this.mMaxProgress != maxProgress || this.mMaxSeekableProgress != maxSeekableProgress) {
            this.mMaxProgress = maxProgress;
            this.mMaxSeekableProgress = maxSeekableProgress;
            if (this.mMaxSeekableProgress > this.mMaxProgress || this.mMaxSeekableProgress <= 0) {
                this.mMaxSeekableProgress = this.mMaxProgress;
            }
            if (this.mMaxProgress > 0) {
                this.mSeekBar.setMax(this.mMaxProgress / 1000);
            }
        }
    }

    public int getMax() {
        return this.mMaxProgress;
    }

    protected void setTailerProgress(int tailerProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setTailerProgress(" + tailerProgress + ")");
        }
        this.mTailerProgress = tailerProgress;
        int seekBarWidth = this.mSeekBarLayout.getWidth();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "seerBarWidth=" + seekBarWidth);
        }
        if (this.mTailerProgress <= MIN_HEAD_TIME || this.mTailerProgress >= this.mMaxProgress - 8000 || seekBarWidth <= 0) {
            this.mTailerView.setVisibility(8);
            return;
        }
        float f;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mTailerView.getLayoutParams();
        float x = ((((float) this.mTailerProgress) / ((float) this.mMaxProgress)) * ((float) seekBarWidth)) - ((float) (ResourceUtil.getDimensionPixelSize(R.dimen.dimen_3dp) / 2));
        params.leftMargin = ((int) x) + 1;
        float maxLeftMargin = (float) (seekBarWidth - ResourceUtil.getDimensionPixelSize(R.dimen.dimen_3dp));
        if (x > maxLeftMargin) {
            f = maxLeftMargin;
        } else {
            f = (float) params.leftMargin;
        }
        params.leftMargin = (int) f;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setTailerProgress() x=" + x + ", maxLeftMargin=" + maxLeftMargin + ", width=" + getWidth() + ", leftMargin=" + params.leftMargin);
        }
        this.mTailerView.setLayoutParams(params);
        this.mTailerView.setVisibility(0);
        this.mHaveTailer = true;
    }

    protected void setHeaderProgress(int headerProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setHeaderProgress(" + headerProgress + ") header width=" + this.mHeaderView.getWidth());
        }
        int seekBarWidth = this.mSeekBarLayout.getWidth();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "seerBarWidth=" + seekBarWidth);
        }
        this.mHeaderProgress = headerProgress;
        if (headerProgress <= MIN_HEAD_TIME || seekBarWidth <= 0) {
            this.mHeaderView.setVisibility(8);
            return;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mHeaderView.getLayoutParams();
        params.leftMargin = (int) (((((float) headerProgress) / ((float) this.mMaxProgress)) * ((float) seekBarWidth)) - ((float) (ResourceUtil.getDimensionPixelSize(R.dimen.dimen_3dp) / 2)));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setHeaderProgress() leftMargin=" + params.leftMargin);
        }
        this.mHeaderView.setLayoutParams(params);
        this.mHeaderView.setVisibility(0);
        this.mHaveHeader = true;
    }

    public void setProgress(int progress) {
        if (!this.mIsTouchMove) {
            setProgress(progress, false);
        }
    }

    private void setProgress(int progress, boolean force) {
        if (!this.mIsSeeking || force) {
            this.mProgress = progress;
            this.mSeekBar.setProgress(progress / 1000);
            if (isShown()) {
                updateTime(progress);
            }
        }
    }

    private void updateTime(int progress) {
        this.mTextVideoTime.setText(StringUtils.stringForTime(progress));
        this.mTxtTotalTime.setText("/" + StringUtils.stringForTime(this.mMaxProgress));
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onVisibilityChanged = " + visibility);
        }
        if (visibility == 0) {
            updateTime(this.mProgress);
        }
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setSecondaryProgress(" + secondaryProgress + ")");
        }
        this.mSeekBar.setSecondaryProgress((int) ((((float) secondaryProgress) * ((float) this.mSeekBar.getMax())) / 100.0f));
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "amazion hide()");
        }
        setVisibility(8);
        this.mHandler.removeCallbacksAndMessages(Integer.valueOf(1));
    }

    public boolean isDragging() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isDragging() returns " + this.mIsDragging);
        }
        return this.mIsDragging;
    }

    public int getProgress() {
        int progress = this.mSeekBar.getProgress() * 1000;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getProgress() return " + progress);
        }
        return progress;
    }

    public void setHeadAndTailProgress(int headProgress, int tailProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setHeadAndTailProgress(" + headProgress + ", " + tailProgress + ")");
        }
        setHeaderProgress(headProgress);
        setTailerProgress(tailProgress);
        if (getVisibility() == 0) {
            checkHeadAndTailLayout();
        }
    }

    private void checkHeadAndTailLayout() {
        boolean relayout = false;
        if (this.mHeaderProgress > MIN_HEAD_TIME && !this.mHaveHeader) {
            relayout = true;
        }
        if (this.mTailerProgress > MIN_HEAD_TIME && this.mTailerProgress < this.mMaxProgress - 8000 && !this.mHaveTailer) {
            relayout = true;
        }
        if (relayout) {
            LogUtils.w(TAG, "checkHeadAndTailLayout() Why not layout children??????");
            setHeaderProgress(this.mHeaderProgress);
            setTailerProgress(this.mTailerProgress);
            this.mHeaderView.requestLayout();
            this.mTailerView.requestLayout();
            removeCallbacks(this.mCheckLayoutRunnable);
            post(this.mCheckLayoutRunnable);
            return;
        }
        setHeaderProgress(this.mHeaderProgress);
        setTailerProgress(this.mTailerProgress);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onSizeChanged(" + w + ", " + h + ", " + oldw + ", " + oldh + ")");
        }
        checkHeadAndTailLayout();
    }

    public void onSeekBegin(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onSeekBegin(" + i + ")");
        }
        this.mTextVideoTime.setTextColor(this.mContext.getResources().getColor(R.color.highlight_time));
        this.mTextVideoTime.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30dp));
        this.mTxtTotalTime.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_19dp));
    }

    public void onProgressChanged(View view, int i) {
    }

    public void onSeekEnd(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onSeekEnd(" + i + ")");
        }
        this.mTextVideoTime.setTextColor(this.mContext.getResources().getColor(R.color.normal_time));
        this.mTextVideoTime.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_21dp));
        this.mTxtTotalTime.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_21dp));
    }
}
