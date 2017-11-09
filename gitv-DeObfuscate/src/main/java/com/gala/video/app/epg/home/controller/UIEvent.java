package com.gala.video.app.epg.home.controller;

import android.os.Handler;
import android.os.Message;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIEvent {
    private final List<UICallback> meventlist = new CopyOnWriteArrayList();
    private Handler mhandler = new C06181();
    private final List<UICallback> mmainEventlist = new CopyOnWriteArrayList();

    public interface UICallback {
        boolean onMessage(int i, Object obj);
    }

    class C06181 extends Handler {
        C06181() {
        }

        public void handleMessage(Message msg) {
            for (UICallback callback : UIEvent.this.mmainEventlist) {
                callback.onMessage(msg.what, msg.obj);
            }
            super.handleMessage(msg);
        }
    }

    public void post(int event, Object arg) {
        for (UICallback callback : this.meventlist) {
            callback.onMessage(event, arg);
        }
    }

    public void postOnMainThread(int event, Object arg) {
        Message msg = this.mhandler.obtainMessage(event);
        msg.obj = arg;
        this.mhandler.sendMessage(msg);
    }

    public void registerOnMainThread(UICallback callback) {
        this.mmainEventlist.add(callback);
    }

    public void unregisterOnMainThread(UICallback callback) {
        this.mmainEventlist.remove(callback);
    }

    public void register(UICallback callback) {
        this.meventlist.add(callback);
    }

    public void unregister(UICallback callback) {
        this.meventlist.remove(callback);
    }
}
