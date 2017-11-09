package com.gala.video.lib.share.ifimpl.ucenter.history.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.gala.video.lib.share.ifimpl.ucenter.account.callback.IHandlerCallback;

abstract class HistoryBase implements IHandlerCallback {
    HistoryHandler mHistoryHandler;

    class HistoryHandler extends Handler {
        HistoryHandler(Looper looper) {
            super(looper);
        }
    }

    HistoryBase() {
    }

    public void sendMessage(Message msg) {
        this.mHistoryHandler.sendMessage(msg);
    }

    public Message obtainMessage(int what, Object obj) {
        return this.mHistoryHandler.obtainMessage(what, obj);
    }

    public void sendEmptyMessage(int what) {
        this.mHistoryHandler.sendEmptyMessage(what);
    }

    public void removeMessages(int what) {
        this.mHistoryHandler.removeMessages(what);
    }

    public void removeCallbacksAndMessages(Object obj) {
        this.mHistoryHandler.removeCallbacksAndMessages(obj);
    }

    public void sendEmptyMessageDelayed(int what, int delay) {
        this.mHistoryHandler.sendEmptyMessageDelayed(what, (long) delay);
    }

    public void sendMessageDelayed(Message msg, int delay) {
        this.mHistoryHandler.sendMessageDelayed(msg, (long) delay);
    }
}
