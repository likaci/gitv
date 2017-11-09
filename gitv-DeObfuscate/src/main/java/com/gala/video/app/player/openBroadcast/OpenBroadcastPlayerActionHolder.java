package com.gala.video.app.player.openBroadcast;

import com.gala.video.lib.share.common.configs.IntentConfig.BroadcastAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.ActionHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.IOpenBroadcastActionHolder.Wrapper;

public class OpenBroadcastPlayerActionHolder extends Wrapper {
    public ActionHolder[] getActionHolder() {
        return new ActionHolder[]{new ActionHolder("ACTION_DETAIL", new OpenDetailAction()), new ActionHolder(BroadcastAction.ACTION_PLAY_VIDEO, new OpenPlayAction())};
    }
}
