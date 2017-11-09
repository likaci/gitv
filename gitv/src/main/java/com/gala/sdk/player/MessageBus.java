package com.gala.sdk.player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MessageBus {
    public static final int STATE_CANCEL = 2;
    public static final int STATE_IDLE = 0;
    public static final int STATE_RUNNING = 1;
    private static MessageBus a;
    private int f332a;
    private Thread f333a;
    private Map<String, CopyOnWriteArrayList<OnMessageListener>> f334a;
    private DelayQueue<MessageWrapper> f335a;

    class CenterRunnable implements Runnable {
        private /* synthetic */ MessageBus a;

        CenterRunnable(MessageBus this$0) {
            this.a = this$0;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r4 = this;
            r0 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;
            if (r0 == 0) goto L_0x000d;
        L_0x0004:
            r0 = "Player/Lib/Utils/MessageBus";
            r1 = "CenterRunnable.run() begin.";
            com.gala.video.lib.framework.core.utils.LogUtils.d(r0, r1);
        L_0x000d:
            r0 = java.lang.Thread.interrupted();
            if (r0 != 0) goto L_0x0045;
        L_0x0013:
            r0 = r4.a;
            r0 = r0.f332a;
            r1 = 2;
            if (r0 == r1) goto L_0x0045;
        L_0x001c:
            r0 = 1;
        L_0x001d:
            if (r0 == 0) goto L_0x0037;
        L_0x001f:
            r1 = 0;
            r0 = r4.a;	 Catch:{ InterruptedException -> 0x0047 }
            r0 = r0.f332a;	 Catch:{ InterruptedException -> 0x0047 }
            r0 = r0.take();	 Catch:{ InterruptedException -> 0x0047 }
            r0 = (com.gala.sdk.player.MessageBus.MessageWrapper) r0;	 Catch:{ InterruptedException -> 0x0047 }
        L_0x002c:
            if (r0 != 0) goto L_0x004d;
        L_0x002e:
            r0 = "Player/Lib/Utils/MessageBus";
            r1 = "CenterRunnable.run() exit for no message.";
            com.gala.video.lib.framework.core.utils.LogUtils.w(r0, r1);
        L_0x0037:
            r0 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;
            if (r0 == 0) goto L_0x0044;
        L_0x003b:
            r0 = "Player/Lib/Utils/MessageBus";
            r1 = "CenterRunnable.run() exit all.";
            com.gala.video.lib.framework.core.utils.LogUtils.d(r0, r1);
        L_0x0044:
            return;
        L_0x0045:
            r0 = 0;
            goto L_0x001d;
        L_0x0047:
            r0 = move-exception;
            r0.printStackTrace();
            r0 = r1;
            goto L_0x002c;
        L_0x004d:
            r1 = r0.getObserverName();
            r2 = r0.getWhat();
            r3 = r0.getMessage();
            r0 = r4.a;
            r0 = r0.f332a;
            r0 = r0.get(r1);
            r0 = (java.util.concurrent.CopyOnWriteArrayList) r0;
            if (r0 == 0) goto L_0x000d;
        L_0x0067:
            r1 = r0.iterator();
        L_0x006b:
            r0 = r1.hasNext();
            if (r0 == 0) goto L_0x000d;
        L_0x0071:
            r0 = r1.next();
            r0 = (com.gala.sdk.player.MessageBus.OnMessageListener) r0;
            r0.onMessage(r3, r2);
            goto L_0x006b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.gala.sdk.player.MessageBus.CenterRunnable.run():void");
        }
    }

    static class MessageWrapper implements Delayed {
        private int a;
        private long f336a;
        private Object f337a;
        private String f338a;

        static MessageWrapper a(String str, Object obj, int i, long j) {
            return new MessageWrapper(str, obj, i, j);
        }

        private MessageWrapper(String observerName, Object message, int what, long triggerTime) {
            this.f337a = message;
            this.f338a = observerName;
            this.a = what;
            this.f336a = triggerTime;
        }

        public Object getMessage() {
            return this.f337a;
        }

        public String getObserverName() {
            return this.f338a;
        }

        public int getWhat() {
            return this.a;
        }

        public long getTriggerTime() {
            return this.f336a;
        }

        public int compareTo(Delayed another) {
            MessageWrapper messageWrapper = (MessageWrapper) another;
            if (this.f336a > messageWrapper.f336a) {
                return 1;
            }
            if (this.f336a < messageWrapper.f336a) {
                return -1;
            }
            return 0;
        }

        public long getDelay(TimeUnit unit) {
            return unit.convert(this.f336a - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    public interface OnMessageListener {
        void onMessage(Object obj, int i);
    }

    private MessageBus() {
        this.f332a = 0;
        this.f334a = new HashMap();
        this.f335a = new DelayQueue();
        this.f332a = 1;
        this.f333a = new Thread(new CenterRunnable(this), "Player/Lib/Utils/MessageBus.thread");
        this.f333a.start();
    }

    public static synchronized MessageBus defaultMessageBus() {
        MessageBus messageBus;
        synchronized (MessageBus.class) {
            if (a == null) {
                a = new MessageBus();
            }
            messageBus = a;
        }
        return messageBus;
    }

    public synchronized void registerObserver(String observerName, OnMessageListener listener) {
        CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) this.f334a.get(observerName);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
            this.f334a.put(observerName, copyOnWriteArrayList);
        }
        if (!copyOnWriteArrayList.contains(listener)) {
            copyOnWriteArrayList.add(listener);
        }
    }

    public synchronized void unregisterObserver(String observerName, OnMessageListener listener) {
        CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) this.f334a.get(observerName);
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(listener);
        }
    }

    public synchronized void unregisterObserver(String observerName) {
        CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) this.f334a.get(observerName);
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.clear();
            this.f334a.remove(observerName);
        }
    }

    public synchronized void sendMessage(String observerName, Object message, int what) {
        this.f335a.add(MessageWrapper.a(observerName, message, what, 0));
    }

    public synchronized void sendMessageDelay(String observerName, Object message, int what, long delayTime) {
        this.f335a.add(MessageWrapper.a(observerName, message, what, System.currentTimeMillis() + delayTime));
    }

    public synchronized void sendMessageAtTime(String observerName, Object message, int what, long triggerTime) {
        this.f335a.add(MessageWrapper.a(observerName, message, what, triggerTime));
    }

    public int getState() {
        return this.f332a;
    }
}
