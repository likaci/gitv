package com.gala.sdk.player;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PositionHintHelper {
    public static final int TYPE_ONCE = 0;
    public static final int TYPE_PERSIST = 1;
    private int a = 1000;
    private final Handler f344a;
    private final PositionProvider f345a;
    private final List<HintItem> f346a = new ArrayList();
    private boolean f347a;
    private List<HintItem> b = new ArrayList(10);

    public static class HintItem {
        private int a;
        private HintListener f348a;
        private int b;
        private int c;

        public HintItem(int position, int type, HintListener listener) {
            this.a = position;
            this.b = type;
            this.f348a = listener;
            if (this.f348a == null) {
                throw new IllegalArgumentException("null listener!");
            }
        }

        public int getPosition() {
            return this.a;
        }

        public HintListener getListener() {
            return this.f348a;
        }

        public int getType() {
            return this.b;
        }

        final int a() {
            return this.c;
        }

        final void a(int i) {
            this.c = i;
        }

        public String toString() {
            return "HintItem(mPosition=" + this.a + ", mType=" + this.b + ", mListener=" + this.f348a + ", mLastCheckPosition=" + this.c + ")";
        }
    }

    public interface HintListener {
        void onHintReach(HintItem hintItem, int i);
    }

    private class MyWorker extends Handler {
        private /* synthetic */ PositionHintHelper a;

        public MyWorker(PositionHintHelper positionHintHelper, Looper looper) {
            this.a = positionHintHelper;
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = 0;
            int position = this.a.a.getPosition();
            this.a.a.clear();
            synchronized (this.a.f346a) {
                if (!this.a.a || this.a.f346a.isEmpty()) {
                    removeMessages(0);
                } else {
                    int i2 = 0;
                    while (i2 < this.a.f346a.size()) {
                        HintItem hintItem = (HintItem) this.a.f346a.get(i2);
                        if (position >= hintItem.getPosition() && hintItem.a() < hintItem.getPosition()) {
                            if (hintItem.getType() != 1) {
                                this.a.f346a.remove(i2);
                                i2--;
                            }
                            this.a.a.add(hintItem);
                        }
                        if (position >= 0) {
                            hintItem.a(position);
                        }
                        i2++;
                    }
                    if (this.a.a && !this.a.f346a.isEmpty()) {
                        sendMessageDelayed(obtainMessage(0), (long) this.a.a);
                    }
                }
            }
            while (i < this.a.a.size()) {
                HintListener listener = ((HintItem) this.a.a.get(i)).getListener();
                if (LogUtils.mIsDebug) {
                    LogUtils.d("Player/Lib/Utils/PositionHintHelper", "handleMessage() position=" + position + ", notify listener " + listener + ", item=" + this.a.a.get(i));
                }
                if (listener != null) {
                    listener.onHintReach((HintItem) this.a.a.get(i), position);
                }
                i++;
            }
        }
    }

    public interface PositionProvider {
        int getPosition();
    }

    public PositionHintHelper(PositionProvider provider) {
        if (Looper.myLooper() != null) {
            this.f344a = new MyWorker(this, Looper.myLooper());
        } else if (Looper.getMainLooper() != null) {
            this.f344a = new MyWorker(this, Looper.getMainLooper());
        } else {
            throw new IllegalArgumentException("No looper!!!");
        }
        this.f345a = provider;
        this.f347a = false;
    }

    public HintItem addHint(int position, int type, HintListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "addHint(" + position + ", " + type + ", " + listener + ") mRunning=" + this.f347a);
        }
        HintItem hintItem = new HintItem(position, type, listener);
        synchronized (this.f346a) {
            this.f346a.add(hintItem);
        }
        if (this.f347a) {
            this.f344a.removeMessages(0);
            this.f344a.obtainMessage(0).sendToTarget();
        }
        return hintItem;
    }

    public boolean removeHint(HintItem item) {
        boolean remove;
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "removeHint(" + item + ") mRunning=" + this.f347a);
        }
        synchronized (this.f346a) {
            remove = this.f346a.remove(item);
        }
        return remove;
    }

    public void removeListener(HintListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "removeHintListener(" + listener + ") mRunning=" + this.f347a);
        }
        synchronized (this.f346a) {
            Iterator it = this.f346a.iterator();
            while (it.hasNext()) {
                if (((HintItem) it.next()).getListener() == listener) {
                    it.remove();
                }
            }
        }
    }

    public void clearHints() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "clearHints()");
        }
        synchronized (this.f346a) {
            this.f346a.clear();
        }
    }

    public void setInterval(int msec) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "setInterval(" + msec + ")");
        }
        this.a = msec;
        if (this.a < 1000) {
            this.a = 1000;
        }
    }

    public void start() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "start() mRunning=" + this.f347a);
        }
        if (!this.f347a) {
            this.f347a = true;
            this.f344a.removeMessages(0);
            this.f344a.obtainMessage(0).sendToTarget();
        }
    }

    public void stop() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "stop() mRunning=" + this.f347a);
        }
        this.f347a = false;
        this.f344a.removeMessages(0);
    }

    public void reset() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "reset()");
        }
        this.a = 1000;
        this.f347a = false;
        this.f344a.removeCallbacksAndMessages(null);
        synchronized (this.f346a) {
            this.f346a.clear();
        }
    }

    public boolean isRunning() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/Lib/Utils/PositionHintHelper", "isRunning() return " + this.f347a);
        }
        return this.f347a;
    }
}
