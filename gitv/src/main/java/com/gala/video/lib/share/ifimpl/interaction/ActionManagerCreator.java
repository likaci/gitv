package com.gala.video.lib.share.ifimpl.interaction;

import com.gala.video.lib.share.ifmanager.bussnessIF.interaction.IActionManager;

public class ActionManagerCreator {
    public static IActionManager create() {
        return new ActionManager();
    }
}
