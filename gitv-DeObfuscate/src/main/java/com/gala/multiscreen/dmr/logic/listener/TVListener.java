package com.gala.multiscreen.dmr.logic.listener;

import com.gala.android.dlna.sdk.mediarenderer.ControlPointConnectRendererListener;
import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;
import com.gala.multiscreen.dmr.util.RunningState;
import com.gala.sdk.player.constants.PlayerIntentConfig2;

public class TVListener {
    private ControlPointConnectRendererListener mControlPointConnectRendererListener = new C01601();

    class C01601 implements ControlPointConnectRendererListener {
        C01601() {
        }

        public void onReceiveDeviceConnect(boolean isConnect) {
            TVListener.this.handle(isConnect);
        }
    }

    public void init(MediaRenderer mMediaRenderer) {
        mMediaRenderer.setControlPointConnectRendererListener(this.mControlPointConnectRendererListener);
    }

    private void handle(boolean isConnect) {
        RunningState.isPhoneConnected = isConnect;
        IGalaMSExpand callback = MSCallbacks.getGalaMS();
        if (callback != null) {
            if (isConnect) {
                MSLog.log("online", LogType.PARAMETER);
                callback.onNotifyEvent(RequestKind.ONLINE, null);
                return;
            }
            MSLog.log(PlayerIntentConfig2.FROM_OFFLINE, LogType.PARAMETER);
            callback.onNotifyEvent(RequestKind.OFFLINE, null);
        }
    }
}
