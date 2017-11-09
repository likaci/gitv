package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.overlay.LiveMediaControllerOverlay.onCountDowntimeListener;
import com.gala.video.app.player.ui.widget.views.ICountDownView.OnVisibilityChangeListener;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class LiveCountdownView extends RelativeLayout implements ICountDownView {
    private static final int MSG_UPDATE_TIME = 1;
    private static final String TAG = "Player/Ui/LiveCountdownView";
    private static final int UPDATE_BY_MINUTE = 15000;
    private static final int UPDATE_BY_SECOND = 500;
    private Handler mHandler = new C15571();
    private long mLiveStartTime;
    private onCountDowntimeListener mOnCountDowntimeListener;
    private TextView mTime0Dec;
    private TextView mTime0Unit;
    private TextView mTime1Dec;
    private TextView mTime1Unit;
    private TextView mTime2Dec;
    private TextView mTime2Unit;
    private TextView mTitle;
    private int mUpdateDuration = 500;
    private OnVisibilityChangeListener mVisibilityChangeListener;

    class C15571 extends Handler {
        C15571() {
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int countDownTime = LiveCountdownView.this.getCountdownTime();
                if (countDownTime < 0) {
                    LiveCountdownView.this.hide();
                    if (LiveCountdownView.this.mOnCountDowntimeListener != null) {
                        LiveCountdownView.this.mOnCountDowntimeListener.onFinished();
                        return;
                    }
                    return;
                }
                LiveCountdownView.this.updateTime();
                sendMessageDelayed(obtainMessage(1), (long) LiveCountdownView.this.mUpdateDuration);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(LiveCountdownView.TAG, "handlerMessage() countDownTime=" + countDownTime + ", isShown()=" + LiveCountdownView.this.isShown());
                }
            }
        }
    }

    private static class TimeHolder {
        private static final int DAY_SECOND = 86400;
        private static final int HOUR_SECOND = 3600;
        private static final int MINUTE_SECOND = 60;
        public static final int MODE_MINUTE = 1;
        public static final int MODE_SECOND = 0;
        private static final String TIME_FORMAT = "%02d";
        public int mode;
        public TimeInfo[] timeInfos;

        public TimeHolder(int mode, TimeInfo[] timeInfos) {
            this.mode = mode;
            this.timeInfos = timeInfos;
        }

        public static TimeHolder getTimeInfos(Context context, int timeSeconds) {
            int mode;
            TimeInfo[] infos = new TimeInfo[3];
            int day = (timeSeconds - 60) / 86400;
            int hour = (timeSeconds - (day * 86400)) / HOUR_SECOND;
            int minute = (timeSeconds % HOUR_SECOND) / 60;
            int second = (timeSeconds % 60) / 1;
            if (day > 0) {
                mode = (timeSeconds + -120) / 86400 > 0 ? 1 : 0;
                infos[0] = new TimeInfo(String.format(TIME_FORMAT, new Object[]{Integer.valueOf(day)}), context.getString(C1291R.string.day_unit));
                infos[1] = new TimeInfo(String.format(TIME_FORMAT, new Object[]{Integer.valueOf(hour)}), context.getString(C1291R.string.hour_unit));
                infos[2] = new TimeInfo(String.format(TIME_FORMAT, new Object[]{Integer.valueOf(minute)}), context.getString(C1291R.string.minute_unit));
            } else {
                mode = 0;
                infos[0] = new TimeInfo(String.format(TIME_FORMAT, new Object[]{Integer.valueOf(hour)}), context.getString(C1291R.string.hour_unit));
                infos[1] = new TimeInfo(String.format(TIME_FORMAT, new Object[]{Integer.valueOf(minute)}), context.getString(C1291R.string.minute_unit));
                infos[2] = new TimeInfo(String.format(TIME_FORMAT, new Object[]{Integer.valueOf(second)}), context.getString(C1291R.string.second_unit));
            }
            return new TimeHolder(mode, infos);
        }
    }

    private static class TimeInfo {
        public String timeDec;
        public String timeUnit;

        private TimeInfo(String dec, String unit) {
            this.timeDec = dec;
            this.timeUnit = unit;
        }
    }

    public LiveCountdownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public LiveCountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiveCountdownView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View v = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C1291R.layout.player_live_countdown_view, null);
        this.mTitle = (TextView) v.findViewById(C1291R.id.txt_title);
        this.mTime0Dec = (TextView) v.findViewById(C1291R.id.txt_time0_dec);
        this.mTime0Unit = (TextView) v.findViewById(C1291R.id.txt_time0_unit);
        this.mTime1Dec = (TextView) v.findViewById(C1291R.id.txt_time1_dec);
        this.mTime1Unit = (TextView) v.findViewById(C1291R.id.txt_time1_unit);
        this.mTime2Dec = (TextView) v.findViewById(C1291R.id.txt_time2_dec);
        this.mTime2Unit = (TextView) v.findViewById(C1291R.id.txt_time2_unit);
        addView(v);
    }

    private void updateTime() {
        int countDownTime = getCountdownTime();
        if (countDownTime < 0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "invalid countdownTime = " + countDownTime);
            }
            setVisibility(8);
            this.mTime0Dec.setText(null);
            this.mTime0Unit.setText(null);
            this.mTime1Dec.setText(null);
            this.mTime1Unit.setText(null);
            this.mTime2Dec.setText(null);
            this.mTime2Unit.setText(null);
            if (this.mOnCountDowntimeListener != null) {
                this.mOnCountDowntimeListener.onFinished();
                return;
            }
            return;
        }
        setVisibility(0);
        TimeHolder holder = TimeHolder.getTimeInfos(getContext(), countDownTime);
        this.mUpdateDuration = holder.mode == 0 ? 500 : UPDATE_BY_MINUTE;
        this.mTime0Dec.setText(holder.timeInfos[0].timeDec);
        this.mTime0Unit.setText(holder.timeInfos[0].timeUnit);
        this.mTime1Dec.setText(holder.timeInfos[1].timeDec);
        this.mTime1Unit.setText(holder.timeInfos[1].timeUnit);
        this.mTime2Dec.setText(holder.timeInfos[2].timeDec);
        this.mTime2Unit.setText(holder.timeInfos[2].timeUnit);
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "show()");
        }
        this.mHandler.removeCallbacksAndMessages(null);
        updateTime();
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), (long) this.mUpdateDuration);
    }

    public void setLiveStartTime(long time) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setLiveStartTime() time=" + time);
        }
        this.mLiveStartTime = time;
    }

    private int getCountdownTime() {
        int count = -1;
        if (this.mLiveStartTime != -1) {
            long countDownTime = this.mLiveStartTime - DeviceUtils.getServerTimeMillis();
            if (countDownTime > 0) {
                count = (int) (countDownTime / 1000);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getCountdownTime() return " + count);
        }
        return count;
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hide()");
        }
        this.mHandler.removeMessages(1);
        setVisibility(8);
        this.mTime0Dec.setText(null);
        this.mTime0Unit.setText(null);
        this.mTime1Dec.setText(null);
        this.mTime1Unit.setText(null);
        this.mTime2Dec.setText(null);
        this.mTime2Unit.setText(null);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setVisibility() visibility=" + visibility);
        }
        if (this.mVisibilityChangeListener != null) {
            switch (visibility) {
                case 0:
                    this.mVisibilityChangeListener.onShown();
                    requestLayout();
                    return;
                case 4:
                case 8:
                    this.mVisibilityChangeListener.onHidden();
                    return;
                default:
                    return;
            }
        }
    }

    public void setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        this.mVisibilityChangeListener = listener;
    }

    public void setThreeDimensional(boolean is3d) {
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        float ratio = zoomRatio;
        if (isFullScreen) {
            ratio = 1.0f;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "switchScreen(isFullScreen=" + isFullScreen + ", ratio=" + ratio + ")");
        }
        float titleSize = ((float) getResources().getDimensionPixelSize(C1291R.dimen.live_title_text_size)) * ratio;
        updateViewSizeTitle(this.mTitle, -1, -1, -1, -1, -1, (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_title_margin_bottom)) * ratio));
        this.mTitle.setTextSize(0, titleSize);
        int timeDecW = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_countdown_view_item_width)) * ratio);
        int timeDecH = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_countdown_view_item_height)) * ratio);
        float timeDecTextSize = ((float) getResources().getDimensionPixelSize(C1291R.dimen.live_time_dec_text_size)) * ratio;
        updateViewSize(this.mTime0Dec, timeDecW, timeDecH, -1, -1, -1, -1);
        updateViewSize(this.mTime1Dec, timeDecW, timeDecH, -1, -1, -1, -1);
        updateViewSize(this.mTime2Dec, timeDecW, timeDecH, -1, -1, -1, -1);
        this.mTime0Dec.setTextSize(0, timeDecTextSize);
        this.mTime1Dec.setTextSize(0, timeDecTextSize);
        this.mTime2Dec.setTextSize(0, timeDecTextSize);
        int timeUnitW = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_countdown_view_item_width)) * ratio);
        int timeUnitMarginT = (int) (((float) getResources().getDimensionPixelSize(C1291R.dimen.live_time_unit_margin_top)) * ratio);
        float timeUnitTextSize = ((float) getResources().getDimensionPixelSize(C1291R.dimen.live_time_unit_text_size)) * ratio;
        updateViewSize(this.mTime0Unit, -1, -1, -1, timeUnitMarginT, -1, -1);
        updateViewSize(this.mTime1Unit, -1, -1, -1, timeUnitMarginT, -1, -1);
        updateViewSize(this.mTime2Unit, -1, -1, -1, timeUnitMarginT, -1, -1);
        this.mTime0Unit.setTextSize(0, timeUnitTextSize);
        this.mTime1Unit.setTextSize(0, timeUnitTextSize);
        this.mTime2Unit.setTextSize(0, timeUnitTextSize);
    }

    private void updateViewSizeTitle(View v, int w, int h, int marginL, int marginT, int marginR, int marginB) {
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        if (w >= 0) {
            layoutParams.width = w;
        }
        if (h >= 0) {
            layoutParams.height = h;
        }
        if (marginL >= 0) {
            layoutParams.leftMargin = marginL;
        }
        if (marginT >= 0) {
            layoutParams.topMargin = marginT;
        }
        if (marginR >= 0) {
            layoutParams.rightMargin = marginR;
        }
        if (marginB >= 0) {
            layoutParams.bottomMargin = marginB;
        }
        v.setLayoutParams(layoutParams);
    }

    private void updateViewSize(View v, int w, int h, int marginL, int marginT, int marginR, int marginB) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();
        if (w >= 0) {
            layoutParams.width = w;
        }
        if (h >= 0) {
            layoutParams.height = h;
        }
        if (marginL >= 0) {
            layoutParams.leftMargin = marginL;
        }
        if (marginT >= 0) {
            layoutParams.topMargin = marginT;
        }
        if (marginR >= 0) {
            layoutParams.rightMargin = marginR;
        }
        if (marginB >= 0) {
            layoutParams.bottomMargin = marginB;
        }
        v.setLayoutParams(layoutParams);
    }

    public void show(int countDownTime) {
    }

    public void setCountDowntimeListener(onCountDowntimeListener listener) {
        this.mOnCountDowntimeListener = listener;
    }
}
