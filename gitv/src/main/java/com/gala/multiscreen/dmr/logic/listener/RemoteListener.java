package com.gala.multiscreen.dmr.logic.listener;

import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.mediarenderer.QuicklySendMessageListener;
import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.multiscreen.dmr.util.MSKeyUtils;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;

public class RemoteListener {
    private static final String TAG = "RemoteListener->";
    private MSKeyUtils mGalaKeyDispatch = new MSKeyUtils();
    private QuicklySendMessageListener mQuicklySendMessageListener = new QuicklySendMessageListener() {
        public void onQuicklySendMessageRecieved(byte arg0) {
            short type = (short) arg0;
            IGalaMSExpand callback = MSCallbacks.getGalaMS();
            MSLog.log("RemoteListener->onQuicklySendMessageREcieved() msg=" + type + ", callback=" + callback, LogType.QUICK_MSG);
            RemoteListener.this.mGalaKeyDispatch.send(type, callback);
        }
    };

    public void init(MediaRenderer mMediaRenderer) {
        mMediaRenderer.setQuicklySend(true);
        mMediaRenderer.setQuicklySendMessageListener(this.mQuicklySendMessageListener);
    }
}
