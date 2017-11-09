package com.gala.video.app.player.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class DataDispatcher {
    private static final String TAG = "Detail/Controller/DataDispatcher";
    private static DataDispatcher sInstance;
    WeakHashMap<Context, WeakReference<IDataCallback>> SListenersMap = new WeakHashMap();
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            for (Entry<Context, WeakReference<IDataCallback>> entry : DataDispatcher.this.SListenersMap.entrySet()) {
                MessageContext messageContext = msg.obj;
                if (messageContext.getContext() == entry.getKey()) {
                    IDataCallback callback = (IDataCallback) ((WeakReference) entry.getValue()).get();
                    if (callback != null) {
                        callback.onDataReady(msg.what, messageContext.getData());
                        return;
                    }
                    return;
                }
            }
        }
    };

    public class MessageContext {
        private Context mContext;
        private Object mData;

        public Context getContext() {
            return this.mContext;
        }

        public Object getData() {
            return this.mData;
        }

        public MessageContext(Context context, Object data) {
            this.mData = data;
            this.mContext = context;
        }
    }

    private DataDispatcher() {
    }

    public static synchronized DataDispatcher instance() {
        DataDispatcher dataDispatcher;
        synchronized (DataDispatcher.class) {
            if (sInstance == null) {
                sInstance = new DataDispatcher();
            }
            dataDispatcher = sInstance;
        }
        return dataDispatcher;
    }

    public static synchronized void release() {
        synchronized (DataDispatcher.class) {
            sInstance = null;
        }
    }

    public void register(Context context, IDataCallback listener) {
        if (listener != null) {
            this.SListenersMap.put(context, new WeakReference(listener));
        }
    }

    public void unregister(Context context) {
        if (context != null) {
            this.SListenersMap.remove(context);
        }
    }

    public void clear() {
        this.SListenersMap.clear();
    }

    public void post(Context context, int msg, Object data) {
        for (Entry<Context, WeakReference<IDataCallback>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                IDataCallback callback = (IDataCallback) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onDataReady(msg, data);
                    return;
                }
                return;
            }
        }
    }

    public void postOnMainThread(Context context, int msg, Object data) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(msg, new MessageContext(context, data)));
            return;
        }
        for (Entry<Context, WeakReference<IDataCallback>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                IDataCallback callback = (IDataCallback) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onDataReady(msg, data);
                    return;
                }
                return;
            }
        }
    }
}
