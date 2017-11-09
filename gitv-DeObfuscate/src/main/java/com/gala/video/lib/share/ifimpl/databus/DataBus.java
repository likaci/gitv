package com.gala.video.lib.share.ifimpl.databus;

import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.Wrapper;

final class DataBus extends Wrapper {
    private static final String TAG = "home/DataBus";
    private MyObservable mObservable = new MyObservable();

    public void registerSubscriber(String event, MyObserver observer) {
        this.mObservable.register(event, observer);
    }

    public void registerStickySubscriber(String event, MyObserver observer) {
        this.mObservable.register(event, observer, true);
    }

    public void unRegisterSubscriber(String event, MyObserver observer) {
        this.mObservable.unregister(event, observer);
    }

    public void postEvent(String event) {
        this.mObservable.notify(event);
    }

    public void postEvent(String event, int block) {
        this.mObservable.notify(event);
    }

    public void postStickyEvent(String event) {
        this.mObservable.stickyNotify(event);
    }
}
