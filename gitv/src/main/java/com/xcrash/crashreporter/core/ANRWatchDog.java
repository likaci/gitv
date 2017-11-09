package com.xcrash.crashreporter.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.Map.Entry;
import org.cybergarage.soap.SOAP;

public class ANRWatchDog {
    private static final String TAG = "xcrash.anr";
    public static ANRWatchDog instance;
    private final int MSG_START_WATCH = 1;
    private boolean isANR = false;
    private Thread mMainThread = Looper.getMainLooper().getThread();
    private int mTimeoutInterval = 5000;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private WatchDogHandler mWatchHandler;

    class WatchDogHandler extends Handler {
        private WatchDogHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ANRWatchDog.this.isANR = true;
                    ANRWatchDog.this.mUiHandler.post(new Runnable() {
                        public void run() {
                            ANRWatchDog.this.isANR = false;
                        }
                    });
                    try {
                        Thread.sleep((long) ANRWatchDog.this.mTimeoutInterval);
                        if (ANRWatchDog.this.isANR) {
                            Log.i(ANRWatchDog.TAG, "AnrMonitor Sucess" + ANRWatchDog.this.getAnrMesage());
                            return;
                        } else {
                            sendEmptyMessageDelayed(1, (long) ANRWatchDog.this.mTimeoutInterval);
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public static ANRWatchDog getInstance() {
        if (instance == null) {
            synchronized (ANRWatchDog.class) {
                if (instance == null) {
                    instance = new ANRWatchDog();
                }
            }
        }
        return instance;
    }

    private ANRWatchDog() {
        HandlerThread monitorThread = new HandlerThread("ANR-WatchDog");
        monitorThread.start();
        this.mWatchHandler = new WatchDogHandler(monitorThread.getLooper());
    }

    public void startWatchDog() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.mWatchHandler.removeMessages(1);
            this.mWatchHandler.sendEmptyMessageDelayed(1, (long) this.mTimeoutInterval);
        }
    }

    public void stopWatchDog() {
        this.mWatchHandler.removeMessages(1);
        Looper looper = this.mWatchHandler.getLooper();
        looper.quit();
        try {
            looper.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private String getAnrMesage() {
        StringBuilder sb = new StringBuilder("msg:\n");
        if (this.mMainThread != null) {
            for (Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
                Thread thread = (Thread) entry.getKey();
                sb.append("\"" + thread.getName() + "\":" + thread.getState() + "\n");
                for (StackTraceElement e : (StackTraceElement[]) entry.getValue()) {
                    sb.append(e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + SOAP.DELIM + e.getLineNumber() + ")\n");
                }
            }
        }
        return sb.toString();
    }
}
