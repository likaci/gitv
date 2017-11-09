package com.gala.video.lib.share.uikit.utils;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import com.gala.video.albumlist.utils.LOG;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandlerThreadPool {
    private List<HandlerThread> mThreadList = new ArrayList();
    private HashMap<HandlerThread, AtomicInteger> mUserCount = new HashMap();

    public static abstract class ThreadHandler implements Callback {
        private Handler mHandler;
        private HandlerThread mThread;

        public abstract void handleMessage(Handler handler, Message message);

        void setThread(HandlerThread thread) {
            if (thread != null) {
                this.mThread = thread;
                if (this.mHandler == null) {
                    this.mHandler = new Handler(thread.getLooper(), this);
                }
            }
        }

        public HandlerThread getThread() {
            return this.mThread;
        }

        public Handler get() {
            return this.mHandler;
        }

        public void release() {
            this.mHandler.removeCallbacksAndMessages(null);
        }

        public boolean handleMessage(Message msg) {
            handleMessage(this.mHandler, msg);
            return true;
        }
    }

    private static class AtomicInteger {
        int mNumber;

        private AtomicInteger() {
            this.mNumber = 0;
        }

        public final int get() {
            return this.mNumber;
        }

        public final void increment() {
            this.mNumber++;
        }

        public final void decrement() {
            this.mNumber--;
        }

        public String toString() {
            return String.valueOf(this.mNumber);
        }
    }

    public HandlerThreadPool(String name, int maximumPoolSize) {
        if (maximumPoolSize != 0) {
            for (int i = 0; i < maximumPoolSize; i++) {
                HandlerThread thread = new HandlerThread(name + "-" + i);
                thread.start();
                this.mThreadList.add(thread);
                this.mUserCount.put(thread, new AtomicInteger());
            }
        }
    }

    public void registerHandler(ThreadHandler handler) {
        if (handler != null) {
            HandlerThread thread = (HandlerThread) this.mThreadList.get(0);
            for (int i = 1; i < this.mThreadList.size(); i++) {
                HandlerThread temp = (HandlerThread) this.mThreadList.get(i);
                if (isBetter(thread, temp)) {
                    thread = temp;
                }
            }
            LOG.m869d("before size = " + this.mUserCount.size() + " count = " + this.mUserCount.get(thread) + " name = " + thread.getName());
            ((AtomicInteger) this.mUserCount.get(thread)).increment();
            LOG.m869d("after size = " + this.mUserCount.size() + " count = " + this.mUserCount.get(thread) + " name = " + thread.getName());
            handler.setThread(thread);
        }
    }

    public void unregisterHandler(ThreadHandler handler) {
        if (handler != null) {
            HandlerThread thread = handler.getThread();
            LOG.m869d("before size = " + this.mUserCount.size() + " count = " + this.mUserCount.get(thread) + " name = " + thread.getName());
            ((AtomicInteger) this.mUserCount.get(handler.getThread())).decrement();
            LOG.m869d("after size = " + this.mUserCount.size() + " count = " + this.mUserCount.get(thread) + " name = " + thread.getName());
            handler.release();
        }
    }

    private boolean isBetter(Thread thread1, Thread thread2) {
        if (thread1.getState() != State.RUNNABLE && thread2.getState() == State.RUNNABLE) {
            return true;
        }
        if (((AtomicInteger) this.mUserCount.get(thread1)).get() > ((AtomicInteger) this.mUserCount.get(thread2)).get()) {
            return true;
        }
        return false;
    }
}
