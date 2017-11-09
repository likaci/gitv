package com.gala.sdk.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PositionHintHelper {
    public static final int TYPE_ONCE = 0;
    public static final int TYPE_PERSIST = 1;
    private int f707a = 1000;
    private final Handler f708a;
    private final PositionProvider f709a;
    private final List<HintItem> f710a = new ArrayList();
    private boolean f711a;

    public static class HintItem {
        private int f702a;
        private HintListener f703a;
        private int f704b;
        private int f705c;

        public HintItem(int position, int type, HintListener listener) {
            this.f702a = position;
            this.f704b = type;
            this.f703a = listener;
            if (this.f703a == null) {
                throw new IllegalArgumentException("null listener!");
            }
        }

        public int getPosition() {
            return this.f702a;
        }

        public HintListener getListener() {
            return this.f703a;
        }

        public int getType() {
            return this.f704b;
        }

        final int m470a() {
            return this.f705c;
        }

        final void m471a(int i) {
            this.f705c = i;
        }

        public String toString() {
            return "HintItem(mPosition=" + this.f702a + ", mType=" + this.f704b + ", mListener=" + this.f703a + ", mLastCheckPosition=" + this.f705c + ")";
        }
    }

    public interface HintListener {
        void onHintReach(HintItem hintItem, int i);
    }

    private class MyWorker extends Handler {
        private /* synthetic */ PositionHintHelper f706a;

        public MyWorker(PositionHintHelper positionHintHelper, Looper looper) {
            this.f706a = positionHintHelper;
            super(looper);
        }

        public void handleMessage(Message msg) {
            int position = this.f706a.f707a.getPosition();
            if (Log.DEBUG) {
                Log.m454d("PlayerUtils/PositionHintHelper", "handleMessage(" + msg + ") mRunning=" + this.f706a.f707a + ", mHints=" + this.f706a.f707a + ", mInterval=" + this.f706a.f707a + ", position=" + position);
            }
            List<HintItem> arrayList = new ArrayList();
            synchronized (this.f706a.f707a) {
                HintItem hintItem;
                if (!this.f706a.f707a || this.f706a.f707a.isEmpty()) {
                    removeMessages(0);
                } else {
                    Iterator it = this.f706a.f707a.iterator();
                    while (it.hasNext()) {
                        hintItem = (HintItem) it.next();
                        if (position >= hintItem.getPosition() && hintItem.m470a() < hintItem.getPosition()) {
                            if (hintItem.getType() != 1) {
                                it.remove();
                            }
                            arrayList.add(hintItem);
                        }
                        if (position >= 0) {
                            hintItem.m471a(position);
                        }
                    }
                    if (this.f706a.f707a && !this.f706a.f707a.isEmpty()) {
                        sendMessageDelayed(obtainMessage(0), (long) this.f706a.f707a);
                    }
                }
            }
            for (HintItem hintItem2 : arrayList) {
                HintListener listener = hintItem2.getListener();
                if (Log.DEBUG) {
                    Log.m454d("PlayerUtils/PositionHintHelper", "handleMessage() position=" + position + ", notify listener " + listener + ", item=" + hintItem2);
                }
                if (listener != null) {
                    listener.onHintReach(hintItem2, position);
                }
            }
        }
    }

    public interface PositionProvider {
        int getPosition();
    }

    public PositionHintHelper(PositionProvider provider) {
        if (Looper.myLooper() != null) {
            this.f708a = new MyWorker(this, Looper.myLooper());
        } else if (Looper.getMainLooper() != null) {
            this.f708a = new MyWorker(this, Looper.getMainLooper());
        } else {
            throw new IllegalArgumentException("No looper!!!");
        }
        this.f709a = provider;
        this.f711a = false;
    }

    public HintItem addHint(int position, int type, HintListener listener) {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "addHint(" + position + ", " + type + ", " + listener + ") mRunning=" + this.f711a);
        }
        HintItem hintItem = new HintItem(position, type, listener);
        synchronized (this.f710a) {
            this.f710a.add(hintItem);
        }
        if (this.f711a) {
            this.f708a.removeMessages(0);
            this.f708a.obtainMessage(0).sendToTarget();
        }
        return hintItem;
    }

    public boolean removeHint(HintItem item) {
        boolean remove;
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "removeHint(" + item + ") mRunning=" + this.f711a);
        }
        synchronized (this.f710a) {
            remove = this.f710a.remove(item);
        }
        return remove;
    }

    public void removeListener(HintListener listener) {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "removeHintListener(" + listener + ") mRunning=" + this.f711a);
        }
        synchronized (this.f710a) {
            Iterator it = this.f710a.iterator();
            while (it.hasNext()) {
                if (((HintItem) it.next()).getListener() == listener) {
                    it.remove();
                }
            }
        }
    }

    public void clearHints() {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "clearHints()");
        }
        synchronized (this.f710a) {
            this.f710a.clear();
        }
    }

    public void setInterval(int msec) {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "setInterval(" + msec + ")");
        }
        this.f707a = msec;
        if (this.f707a < 1000) {
            this.f707a = 1000;
        }
    }

    public void start() {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "start() mRunning=" + this.f711a);
        }
        if (!this.f711a) {
            this.f711a = true;
            this.f708a.removeMessages(0);
            this.f708a.obtainMessage(0).sendToTarget();
        }
    }

    public void stop() {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "stop() mRunning=" + this.f711a);
        }
        this.f711a = false;
        this.f708a.removeMessages(0);
    }

    public void reset() {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "reset()");
        }
        this.f707a = 1000;
        this.f711a = false;
        this.f708a.removeCallbacksAndMessages(null);
        synchronized (this.f710a) {
            this.f710a.clear();
        }
    }

    public boolean isRunning() {
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/PositionHintHelper", "isRunning() return " + this.f711a);
        }
        return this.f711a;
    }
}
