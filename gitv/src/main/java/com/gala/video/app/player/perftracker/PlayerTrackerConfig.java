package com.gala.video.app.player.perftracker;

import com.gala.video.app.player.perftracker.ITrackerConfig.ModuleType;

public class PlayerTrackerConfig extends AbsTrackerConfig {
    public String getLogTag() {
        return "Player";
    }

    public ModuleType getModuleType() {
        return ModuleType.PLAYER;
    }
}
