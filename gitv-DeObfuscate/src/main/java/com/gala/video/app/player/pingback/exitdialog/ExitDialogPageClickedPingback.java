package com.gala.video.app.player.pingback.exitdialog;

import com.gala.video.app.player.pingback.ClickPingback;

public class ExitDialogPageClickedPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "c1", "now_c1", "now_qpid"};

    public ExitDialogPageClickedPingback() {
        super(TYPES);
    }
}
