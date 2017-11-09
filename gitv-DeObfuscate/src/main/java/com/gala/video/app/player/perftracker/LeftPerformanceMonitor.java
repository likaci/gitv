package com.gala.video.app.player.perftracker;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LeftPerformanceMonitor {
    private static final int MSG_HIDE_UI = 3;
    private static final int MSG_SHOW_UI = 1;
    private static final int MSG_UPDATE_TIME = 2;
    public static final String TAG = "Player/Lib/Data/LeftPerformanceMonitor";
    private static LeftPerformanceMonitor mInstance;
    private WeakReference<Context> mContextRef;
    private Handler mHandler;
    private LeftFloatingWindow mLeftFloatingWindow;
    private TextView mTVCurrentTime;
    private HandlerThread mUIThread;

    public static synchronized LeftPerformanceMonitor getInstance(Context context) {
        LeftPerformanceMonitor leftPerformanceMonitor;
        synchronized (LeftPerformanceMonitor.class) {
            if (mInstance == null) {
                mInstance = new LeftPerformanceMonitor(context);
            }
            leftPerformanceMonitor = mInstance;
        }
        return leftPerformanceMonitor;
    }

    private LeftPerformanceMonitor(Context context) {
        this.mContextRef = new WeakReference(context);
    }

    private static String getNow() {
        return new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH).format(new Date());
    }

    public void startPerf() {
        this.mUIThread = new HandlerThread("CurrentTimeThread");
        this.mUIThread.setPriority(10);
        this.mUIThread.start();
        this.mHandler = new Handler(this.mUIThread.getLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        LeftPerformanceMonitor.this.mLeftFloatingWindow = new LeftFloatingWindow((Context) LeftPerformanceMonitor.this.mContextRef.get(), LayoutInflater.from((Context) LeftPerformanceMonitor.this.mContextRef.get()).inflate(C1291R.layout.player_floating_view_left, null));
                        LeftPerformanceMonitor.this.mTVCurrentTime = (TextView) LeftPerformanceMonitor.this.mLeftFloatingWindow.getContentView().findViewById(C1291R.id.txt_currentTime);
                        LeftPerformanceMonitor.this.mLeftFloatingWindow.showWindow();
                        return;
                    case 2:
                        LeftPerformanceMonitor.this.mTVCurrentTime.setText(LeftPerformanceMonitor.getNow());
                        removeMessages(2);
                        sendEmptyMessageDelayed(2, 100);
                        return;
                    case 3:
                        removeCallbacksAndMessages(null);
                        if (LeftPerformanceMonitor.this.mLeftFloatingWindow != null) {
                            LeftPerformanceMonitor.this.mLeftFloatingWindow.hideWindow();
                        }
                        if (LeftPerformanceMonitor.this.mUIThread.quit()) {
                            LogUtils.m1568d(LeftPerformanceMonitor.TAG, "uiThread quit.");
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mHandler.sendEmptyMessage(1);
        this.mHandler.sendEmptyMessage(2);
    }

    public void stopPerf() {
        this.mHandler.sendEmptyMessage(3);
    }
}
