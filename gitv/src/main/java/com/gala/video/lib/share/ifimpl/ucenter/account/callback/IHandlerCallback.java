package com.gala.video.lib.share.ifimpl.ucenter.account.callback;

import android.os.Message;

public interface IHandlerCallback {
    Message obtainMessage(int i, Object obj);

    void removeCallbacksAndMessages(Object obj);

    void removeMessages(int i);

    void sendEmptyMessage(int i);

    void sendMessage(Message message);
}
