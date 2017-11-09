package com.gala.video.app.player.pingback.exitdialog;

import com.gala.video.app.player.pingback.ShowPingback;

public class ExitDialogPageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "qtcurl", "rfr", "e", "block"};

    public ExitDialogPageShowPingback() {
        super(TYPES);
    }
}
