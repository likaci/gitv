package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class CarouselCountdownView extends LinearLayout {
    private static long MAX_COUNT_DOWN_TIME = 356400000;
    protected static final int MSG_UPDATE_TIME = 1;
    private static final String TAG = "Player/ui/CarouselCountdownView";
    private static final int UPDATE_TIME_DURATION = 1000;
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CarouselCountdownView.this.updateTime();
            }
        }
    };
    private long mStartTime = -1;
    private TextView mTxtHour;
    private TextView mTxtMin;
    private TextView mTxtSecond;
    private TextView mTxtTip;

    public CarouselCountdownView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CarouselCountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public CarouselCountdownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(this.mContext).inflate(R.layout.player_carousel_countdown_time, this);
        this.mTxtTip = (TextView) findViewById(R.id.carousel_tip);
        this.mTxtHour = (TextView) findViewById(R.id.hour);
        this.mTxtMin = (TextView) findViewById(R.id.min);
        this.mTxtSecond = (TextView) findViewById(R.id.second);
        Typeface tf = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/DS-DIGI.TTF");
        this.mTxtHour.setTypeface(tf, 1);
        this.mTxtMin.setTypeface(tf, 1);
        this.mTxtSecond.setTypeface(tf, 1);
        setOrientation(1);
        setGravity(17);
        setVisibility(8);
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "CarouselCountdownView show()=" + this.mStartTime);
        }
        this.mHandler.removeCallbacksAndMessages(null);
        updateTime();
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hide()");
        }
        this.mHandler.removeMessages(1);
        setVisibility(8);
    }

    public void setStartTime(long time) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setStartTime:" + time);
        }
        this.mStartTime = time;
    }

    private void updateTime() {
        if (this.mStartTime > 0) {
            long countDownTime = this.mStartTime - DeviceUtils.getServerTimeMillis();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "updateTime() countDownTime=" + countDownTime);
            }
            if (countDownTime > 0) {
                formatTime(countDownTime);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), 1000);
            }
        }
    }

    private void formatTime(long countDownTime) {
        if (countDownTime < MAX_COUNT_DOWN_TIME && countDownTime >= 0) {
            int mi = 1000 * 60;
            int hh = mi * 60;
            long hours = countDownTime / ((long) hh);
            long minutes = (countDownTime - (((long) hh) * hours)) / ((long) mi);
            long seconds = ((countDownTime - (((long) hh) * hours)) - (((long) mi) * minutes)) / ((long) 1000);
            String strHour = hours < 10 ? "0" + hours : String.valueOf(hours);
            String strMinute = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
            String strSecond = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
            this.mTxtHour.setText(strHour);
            this.mTxtMin.setText(strMinute);
            this.mTxtSecond.setText(strSecond);
            setVisibility(0);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "formatTime strHour:" + strHour + ", strMinute=" + strMinute + ", strSecond=" + strSecond);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "invalid countdownTime = " + countDownTime);
        }
    }
}
