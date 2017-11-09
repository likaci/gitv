package com.gala.video.app.player.perftracker;

import com.gala.video.app.player.perftracker.ITrackerConfig.LogType;
import com.gala.video.app.player.perftracker.ITrackerConfig.TrackType;

public abstract class AbsTrackerConfig implements ITrackerConfig {
    public TrackType getTrackType() {
        return TrackType.BOTH;
    }

    public LogType getLogType() {
        return LogType.BOTH;
    }
}
