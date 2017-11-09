package com.gala.video.app.player.controller.error;

import android.content.Context;
import com.gala.sdk.player.ui.IPlayerOverlay;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;

public class FullscreenErrorStrategy extends AbsErrorStrategy {
    public FullscreenErrorStrategy(Context context, IFeedbackDialogController controller, IPlayerOverlay overlay) {
        super(context, controller, overlay);
    }
}
