package com.gala.video.lib.share.ifimpl.databus;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MyObservable {
    private static final String TAG = "HomeDataObservable";
    private Set<String> mEventPool = new HashSet();
    private HashMap<String, List<MyObserver>> mMap = new HashMap();

    MyObservable() {
    }

    public synchronized void register(String event, MyObserver observer) {
        if (event != null) {
            List<MyObserver> observers = (List) this.mMap.get(event);
            if (observers != null) {
                observers.add(observer);
            } else {
                observers = new ArrayList();
                observers.add(observer);
                this.mMap.put(event, observers);
            }
        }
    }

    public synchronized void register(String event, MyObserver observer, boolean isSticky) {
        LogUtils.m1568d(TAG, "register event = " + event + " observers = " + observer);
        register(event, observer);
        if (isSticky && this.mEventPool.contains(event)) {
            notify(event);
            this.mEventPool.remove(event);
        }
    }

    public synchronized void unregister(String event, MyObserver observer) {
        if (event != null) {
            List<MyObserver> observers = (List) this.mMap.get(event);
            LogUtils.m1568d(TAG, "unregister event = " + event + " observers = " + observers);
            if (observers != null) {
                observers.remove(observer);
            }
        }
    }

    public synchronized void notify(String event) {
        if (event != null) {
            List<MyObserver> observers = (List) this.mMap.get(event);
            if (observers != null) {
                for (MyObserver observer : observers) {
                    observer.update(event);
                }
            }
        }
    }

    public synchronized void stickyNotify(String event) {
        if (event != null) {
            List<MyObserver> observers = (List) this.mMap.get(event);
            LogUtils.m1568d(TAG, "stickyNotify event = " + event);
            if ((observers == null || observers.size() != 0) && observers != null) {
                for (MyObserver observer : observers) {
                    observer.update(event);
                }
            } else {
                LogUtils.m1568d(TAG, "there is no observer cache home event (" + event + ")");
                this.mEventPool.add(event);
            }
        }
    }
}
