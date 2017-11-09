package com.gala.video.app.player.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.KeyValue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RCMultiKeyEventUtils {
    private static final int KEY_INTERVAL = 1000;
    private static final ArrayList<Integer> NUMKEYS = new ArrayList();
    private static final String TAG = "RCMultiKeyEventUtils";
    private static final String TOAST_STRING = "TOAST_STRING";
    private static final int UPDATE_TOAST_MESSAGE = 285212673;
    private static RCMultiKeyEventUtils sInstance;
    private WeakReference<Context> mContextRef;
    private int mCurrentOrder;
    private WeakReference<KeyValue> mKeyValue;
    private MultiKeysEvent mMultiKeysEvent = new MultiKeysEvent();
    private Timer mTimer;

    class C15791 extends TimerTask {
        C15791() {
        }

        public void run() {
            Context context = (Context) RCMultiKeyEventUtils.this.mContextRef.get();
            if (context == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1577w(RCMultiKeyEventUtils.TAG, "run: context is null");
                }
            } else if (RCMultiKeyEventUtils.this.mMultiKeysEvent != null) {
                Runnable r = null;
                int num = RCMultiKeyEventUtils.this.mMultiKeysEvent.getNum();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(RCMultiKeyEventUtils.TAG, "run: pressed num=" + num);
                }
                String nthEpisodeStr = context.getResources().getString(C1291R.string.vc_nth_episode, new Object[]{Integer.valueOf(num)});
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(RCMultiKeyEventUtils.TAG, "run: episode string=" + nthEpisodeStr);
                }
                if (RCMultiKeyEventUtils.this.mKeyValue != null) {
                    r = ((KeyValue) RCMultiKeyEventUtils.this.mKeyValue.get()).getReservedRunnable(nthEpisodeStr);
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(RCMultiKeyEventUtils.TAG, "run: runnable=" + r);
                }
                String noEpisodeStr = "";
                boolean showToast = false;
                if (r == null) {
                    noEpisodeStr = context.getResources().getString(C1291R.string.vc_missing_nth_episode, new Object[]{Integer.valueOf(num)});
                    showToast = true;
                } else if (num == RCMultiKeyEventUtils.this.mCurrentOrder) {
                    showToast = true;
                    noEpisodeStr = context.getResources().getString(C1291R.string.vc_already_nth_episode, new Object[]{Integer.valueOf(num)});
                } else {
                    RCMultiKeyEventUtils.this.onAction(nthEpisodeStr, r);
                }
                if (showToast) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString(RCMultiKeyEventUtils.TOAST_STRING, noEpisodeStr);
                    message.what = RCMultiKeyEventUtils.UPDATE_TOAST_MESSAGE;
                    message.setData(bundle);
                    new MyHanlder(context.getMainLooper()).sendMessage(message);
                }
                RCMultiKeyEventUtils.this.mMultiKeysEvent.clearKeyEvents();
            }
        }
    }

    private static class MultiKeysEvent {
        private static final String TAG = "RCMultiKeyEventUtils/MultiKeysEvent";
        private ArrayList<KeyEvent> mMultiKeys;

        private MultiKeysEvent() {
            this.mMultiKeys = new ArrayList();
        }

        public synchronized void add(KeyEvent event) {
            this.mMultiKeys.add(event);
        }

        public synchronized void clearKeyEvents() {
            this.mMultiKeys.clear();
        }

        public synchronized int getNum() {
            int num;
            num = 0;
            int size = this.mMultiKeys.size();
            for (int i = 0; i < size; i++) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "getNum: keycode[" + i + "]=" + ((KeyEvent) this.mMultiKeys.get(i)).getKeyCode());
                }
                num = (int) (((double) num) + (((double) (((KeyEvent) this.mMultiKeys.get(i)).getKeyCode() - 7)) * Math.pow(10.0d, (double) ((this.mMultiKeys.size() - i) - 1))));
            }
            return num;
        }
    }

    private class MyHanlder extends Handler {
        public MyHanlder(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RCMultiKeyEventUtils.UPDATE_TOAST_MESSAGE /*285212673*/:
                    QToast.makeTextAndShow((Context) RCMultiKeyEventUtils.this.mContextRef.get(), msg.getData().getString(RCMultiKeyEventUtils.TOAST_STRING), (int) QToast.LENGTH_LONG);
                    return;
                default:
                    return;
            }
        }
    }

    static {
        NUMKEYS.add(Integer.valueOf(7));
        NUMKEYS.add(Integer.valueOf(8));
        NUMKEYS.add(Integer.valueOf(9));
        NUMKEYS.add(Integer.valueOf(10));
        NUMKEYS.add(Integer.valueOf(11));
        NUMKEYS.add(Integer.valueOf(12));
        NUMKEYS.add(Integer.valueOf(13));
        NUMKEYS.add(Integer.valueOf(14));
        NUMKEYS.add(Integer.valueOf(15));
        NUMKEYS.add(Integer.valueOf(16));
    }

    public static synchronized RCMultiKeyEventUtils getInstance() {
        RCMultiKeyEventUtils rCMultiKeyEventUtils;
        synchronized (RCMultiKeyEventUtils.class) {
            if (sInstance == null) {
                sInstance = new RCMultiKeyEventUtils();
            }
            rCMultiKeyEventUtils = sInstance;
        }
        return rCMultiKeyEventUtils;
    }

    public void initialize(Context context, KeyValue keyValue) {
        this.mContextRef = new WeakReference(context);
        this.mKeyValue = new WeakReference(keyValue);
    }

    public void onKeyDown(KeyEvent event, int currentOrder) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onKeyDown() " + event);
        }
        if (event.getAction() == 0 && NUMKEYS.contains(Integer.valueOf(event.getKeyCode()))) {
            this.mMultiKeysEvent.add(event);
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer.purge();
                this.mTimer = null;
            }
            this.mCurrentOrder = currentOrder;
            TimerTask combinationKeyTask = new C15791();
            this.mTimer = new Timer(true);
            this.mTimer.schedule(combinationKeyTask, 1000);
        }
    }

    private boolean onAction(String key, final Runnable runnable) {
        if (!(this.mContextRef.get() instanceof Activity)) {
            return false;
        }
        ((Activity) this.mContextRef.get()).runOnUiThread(new Runnable() {
            public void run() {
                runnable.run();
            }
        });
        return true;
    }
}
