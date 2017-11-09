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
    private int f676a = 1000;
    private final Handler f677a;
    private final PositionProvider f678a;
    private final List<HintItem> f679a = new ArrayList();
    private boolean f680a;
    private List<HintItem> f681b = new ArrayList(10);

    public static class HintItem {
        private int f671a;
        private HintListener f672a;
        private int f673b;
        private int f674c;

        public HintItem(int position, int type, HintListener listener) {
            this.f671a = position;
            this.f673b = type;
            this.f672a = listener;
            if (this.f672a == null) {
                throw new IllegalArgumentException("null listener!");
            }
        }

        public int getPosition() {
            return this.f671a;
        }

        public HintListener getListener() {
            return this.f672a;
        }

        public int getType() {
            return this.f673b;
        }

        final int m423a() {
            return this.f674c;
        }

        final void m424a(int i) {
            this.f674c = i;
        }

        public String toString() {
            return "HintItem(mPosition=" + this.f671a + ", mType=" + this.f673b + ", mListener=" + this.f672a + ", mLastCheckPosition=" + this.f674c + ")";
        }
    }

    public interface HintListener {
        void onHintReach(HintItem hintItem, int i);
    }

    private class MyWorker extends Handler {
        private /* synthetic */ PositionHintHelper f675a;

        public MyWorker(PositionHintHelper positionHintHelper, Looper looper) {
            this.f675a = positionHintHelper;
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = 0;
            int position = this.f675a.f676a.getPosition();
            this.f675a.f676a.clear();
            synchronized (this.f675a.f679a) {
                if (!this.f675a.f676a || this.f675a.f679a.isEmpty()) {
                    removeMessages(0);
                } else {
                    int i2 = 0;
                    while (i2 < this.f675a.f679a.size()) {
                        HintItem hintItem = (HintItem) this.f675a.f679a.get(i2);
                        if (position >= hintItem.getPosition() && hintItem.m423a() < hintItem.getPosition()) {
                            if (hintItem.getType() != 1) {
                                this.f675a.f679a.remove(i2);
                                i2--;
                            }
                            this.f675a.f676a.add(hintItem);
                        }
                        if (position >= 0) {
                            hintItem.m424a(position);
                        }
                        i2++;
                    }
                    if (this.f675a.f676a && !this.f675a.f679a.isEmpty()) {
                        sendMessageDelayed(obtainMessage(0), (long) this.f675a.f676a);
                    }
                }
            }
            while (i < this.f675a.f676a.size()) {
                HintListener listener = ((HintItem) this.f675a.f676a.get(i)).getListener();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "handleMessage() position=" + position + ", notify listener " + listener + ", item=" + this.f675a.f676a.get(i));
                }
                if (listener != null) {
                    listener.onHintReach((HintItem) this.f675a.f676a.get(i), position);
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
            this.f677a = new MyWorker(this, Looper.myLooper());
        } else if (Looper.getMainLooper() != null) {
            this.f677a = new MyWorker(this, Looper.getMainLooper());
        } else {
            throw new IllegalArgumentException("No looper!!!");
        }
        this.f678a = provider;
        this.f680a = false;
    }

    public HintItem addHint(int position, int type, HintListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "addHint(" + position + ", " + type + ", " + listener + ") mRunning=" + this.f680a);
        }
        HintItem hintItem = new HintItem(position, type, listener);
        synchronized (this.f679a) {
            this.f679a.add(hintItem);
        }
        if (this.f680a) {
            this.f677a.removeMessages(0);
            this.f677a.obtainMessage(0).sendToTarget();
        }
        return hintItem;
    }

    public boolean removeHint(HintItem item) {
        boolean remove;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "removeHint(" + item + ") mRunning=" + this.f680a);
        }
        synchronized (this.f679a) {
            remove = this.f679a.remove(item);
        }
        return remove;
    }

    public void removeListener(HintListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "removeHintListener(" + listener + ") mRunning=" + this.f680a);
        }
        synchronized (this.f679a) {
            Iterator it = this.f679a.iterator();
            while (it.hasNext()) {
                if (((HintItem) it.next()).getListener() == listener) {
                    it.remove();
                }
            }
        }
    }

    public void clearHints() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "clearHints()");
        }
        synchronized (this.f679a) {
            this.f679a.clear();
        }
    }

    public void setInterval(int msec) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "setInterval(" + msec + ")");
        }
        this.f676a = msec;
        if (this.f676a < 1000) {
            this.f676a = 1000;
        }
    }

    public void start() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "start() mRunning=" + this.f680a);
        }
        if (!this.f680a) {
            this.f680a = true;
            this.f677a.removeMessages(0);
            this.f677a.obtainMessage(0).sendToTarget();
        }
    }

    public void stop() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "stop() mRunning=" + this.f680a);
        }
        this.f680a = false;
        this.f677a.removeMessages(0);
    }

    public void reset() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "reset()");
        }
        this.f676a = 1000;
        this.f680a = false;
        this.f677a.removeCallbacksAndMessages(null);
        synchronized (this.f679a) {
            this.f679a.clear();
        }
    }

    public boolean isRunning() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Utils/PositionHintHelper", "isRunning() return " + this.f680a);
        }
        return this.f680a;
    }
}
