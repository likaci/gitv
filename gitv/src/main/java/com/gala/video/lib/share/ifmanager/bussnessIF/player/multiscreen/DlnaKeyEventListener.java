package com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen;

import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;

public interface DlnaKeyEventListener {
    boolean onDlnaKeyEvent(DlnaKeyEvent dlnaKeyEvent, KeyKind keyKind);
}
