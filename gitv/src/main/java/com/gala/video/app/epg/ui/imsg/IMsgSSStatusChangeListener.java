package com.gala.video.app.epg.ui.imsg;

import com.gala.video.app.epg.ui.imsg.dialog.MsgDialogHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;

public class IMsgSSStatusChangeListener implements IStatusListener {
    public void onStart() {
    }

    public void onStop() {
        MsgDialogHelper.get().onMessage(null);
    }
}
