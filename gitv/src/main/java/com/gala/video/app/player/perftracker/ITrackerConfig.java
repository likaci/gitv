package com.gala.video.app.player.perftracker;

public interface ITrackerConfig {

    public enum LogType {
        LOGCAT,
        LOGBUFFER,
        BOTH
    }

    public enum ModuleType {
        PLAYER,
        EPG,
        NONE
    }

    public enum TrackType {
        FLOATWINDOW_ONLY,
        LOG_ONLY,
        BOTH
    }

    String getLogTag();

    LogType getLogType();

    ModuleType getModuleType();

    TrackType getTrackType();
}
