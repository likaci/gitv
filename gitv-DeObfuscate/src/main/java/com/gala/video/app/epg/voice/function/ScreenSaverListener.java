package com.gala.video.app.epg.voice.function;

import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import java.util.ArrayList;
import java.util.List;

public class ScreenSaverListener extends VoiceListener {
    public ScreenSaverListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        List<AbsVoiceAction> actions = new ArrayList();
        actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(-1, "")) {
            protected boolean dispatchVoiceEvent(VoiceEvent event) {
                PingBackUtils.setTabSrc("其他");
                return ScreenSaverListener.this.dispatchVoiceEvent();
            }
        });
        return actions;
    }

    private boolean dispatchVoiceEvent() {
        IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
        if (iOperate.isShowScreenSaver()) {
            iOperate.hideScreenSaver();
        }
        iOperate.reStart();
        return false;
    }
}
