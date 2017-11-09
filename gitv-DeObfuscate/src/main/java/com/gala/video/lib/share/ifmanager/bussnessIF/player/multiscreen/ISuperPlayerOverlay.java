package com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen;

import com.gala.sdk.player.ui.IPlayerOverlay;

public interface ISuperPlayerOverlay extends IPlayerOverlay, DlnaKeyEventListener, IVolumeView, IErrorHandler {
    boolean isLoadingViewShown();
}
