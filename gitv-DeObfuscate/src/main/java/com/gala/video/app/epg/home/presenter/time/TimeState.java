package com.gala.video.app.epg.home.presenter.time;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeState {
    private static final int MSG = 100;
    private static final int ONE_MINUTE = 60000;
    private static final String TAG = "home/TimeState";
    private final Handler mHandler = new MyHandler(Looper.getMainLooper());
    private final TextView mTimeView;

    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            TimeState.this.updateTime();
        }
    }

    public TimeState(TextView timeView) {
        this.mTimeView = timeView;
    }

    public void onStart() {
        updateTime();
    }

    private static String formatTime(long currentTime) {
        String result = "";
        return new SimpleDateFormat("HH:mm").format(new Date(currentTime));
    }

    public void onStop() {
        this.mHandler.removeMessages(100);
    }

    private void updateTime() {
        String result = formatTime(DeviceUtils.getServerTimeMillis());
        LogUtils.m1568d(TAG, "updateTime current time : " + result);
        this.mTimeView.setText(result);
        this.mHandler.sendEmptyMessageDelayed(100, 60000);
    }
}
