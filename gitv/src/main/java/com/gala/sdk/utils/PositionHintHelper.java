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
    private int a = 1000;
    private final Handler f357a;
    private final PositionProvider f358a;
    private final List<HintItem> f359a = new ArrayList();
    private boolean f360a;

    public static class HintItem {
        private int a;
        private HintListener f361a;
        private int b;
        private int c;

        public HintItem(int position, int type, HintListener listener) {
            this.a = position;
            this.b = type;
            this.f361a = listener;
            if (this.f361a == null) {
                throw new IllegalArgumentException("null listener!");
            }
        }

        public int getPosition() {
            return this.a;
        }

        public HintListener getListener() {
            return this.f361a;
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
            return "HintItem(mPosition=" + this.a + ", mType=" + this.b + ", mListener=" + this.f361a + ", mLastCheckPosition=" + this.c + ")";
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

        public void handleMessage(Message msg) {
            HintItem hintItem;
            int position = this.a.a.getPosition();
            if (Log.DEBUG) {
                Log.d("PlayerUtils/PositionHintHelper", "handleMessage(" + msg + ") mRunning=" + this.a.a + ", mHints=" + this.a.a + ", mInterval=" + this.a.a + ", position=" + position);
            }
            List<HintItem> arrayList = new ArrayList();
            synchronized (this.a.a) {
                if (!this.a.a || this.a.a.isEmpty()) {
                    removeMessages(0);
                } else {
                    Iterator it = this.a.a.iterator();
                    while (it.hasNext()) {
                        hintItem = (HintItem) it.next();
                        if (position >= hintItem.getPosition() && hintItem.a() < hintItem.getPosition()) {
                            if (hintItem.getType() != 1) {
                                it.remove();
                            }
                            arrayList.add(hintItem);
                        }
                        if (position >= 0) {
                            hintItem.a(position);
                        }
                    }
                    if (this.a.a && !this.a.a.isEmpty()) {
                        sendMessageDelayed(obtainMessage(0), (long) this.a.a);
                    }
                }
            }
            for (HintItem hintItem2 : arrayList) {
                HintListener listener = hintItem2.getListener();
                if (Log.DEBUG) {
                    Log.d("PlayerUtils/PositionHintHelper", "handleMessage() position=" + position + ", notify listener " + listener + ", item=" + hintItem2);
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
            this.f357a = new MyWorker(this, Looper.myLooper());
        } else if (Looper.getMainLooper() != null) {
            this.f357a = new MyWorker(this, Looper.getMainLooper());
        } else {
            throw new IllegalArgumentException("No looper!!!");
        }
        this.f358a = provider;
        this.f360a = false;
    }

    public HintItem addHint(int position, int type, HintListener listener) {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "addHint(" + position + ", " + type + ", " + listener + ") mRunning=" + this.f360a);
        }
        HintItem hintItem = new HintItem(position, type, listener);
        synchronized (this.f359a) {
            this.f359a.add(hintItem);
        }
        if (this.f360a) {
            this.f357a.removeMessages(0);
            this.f357a.obtainMessage(0).sendToTarget();
        }
        return hintItem;
    }

    public boolean removeHint(HintItem item) {
        boolean remove;
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "removeHint(" + item + ") mRunning=" + this.f360a);
        }
        synchronized (this.f359a) {
            remove = this.f359a.remove(item);
        }
        return remove;
    }

    public void removeListener(HintListener listener) {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "removeHintListener(" + listener + ") mRunning=" + this.f360a);
        }
        synchronized (this.f359a) {
            Iterator it = this.f359a.iterator();
            while (it.hasNext()) {
                if (((HintItem) it.next()).getListener() == listener) {
                    it.remove();
                }
            }
        }
    }

    public void clearHints() {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "clearHints()");
        }
        synchronized (this.f359a) {
            this.f359a.clear();
        }
    }

    public void setInterval(int msec) {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "setInterval(" + msec + ")");
        }
        this.a = msec;
        if (this.a < 1000) {
            this.a = 1000;
        }
    }

    public void start() {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "start() mRunning=" + this.f360a);
        }
        if (!this.f360a) {
            this.f360a = true;
            this.f357a.removeMessages(0);
            this.f357a.obtainMessage(0).sendToTarget();
        }
    }

    public void stop() {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "stop() mRunning=" + this.f360a);
        }
        this.f360a = false;
        this.f357a.removeMessages(0);
    }

    public void reset() {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "reset()");
        }
        this.a = 1000;
        this.f360a = false;
        this.f357a.removeCallbacksAndMessages(null);
        synchronized (this.f359a) {
            this.f359a.clear();
        }
    }

    public boolean isRunning() {
        if (Log.DEBUG) {
            Log.d("PlayerUtils/PositionHintHelper", "isRunning() return " + this.f360a);
        }
        return this.f360a;
    }
}
