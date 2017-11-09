package com.gala.video.app.epg.screensaver;

import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;

public class ScreenSaverCreater {
    public static IScreenSaverOperate create() {
        return new ScreenSaverOperator();
    }
}
