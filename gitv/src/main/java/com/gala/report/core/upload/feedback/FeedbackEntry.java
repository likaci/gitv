package com.gala.report.core.upload.feedback;

import com.gala.video.app.epg.ui.setting.SettingConstants;

public enum FeedbackEntry {
    HOME("ui_home"),
    ALBUM_LIST("ui_albumlist"),
    SEARCH("search"),
    MUTlTISCREEN(SettingConstants.MULTISCREEN),
    PLAYER_KERNEL("player_kernel"),
    SYSTEM_PLAYER("system_player"),
    PLAYER_COMMON("player_common"),
    DETAIL("ui_detail"),
    RECORD("play_record"),
    TVAPI("tvapi"),
    OTHER("others"),
    DEFAULT("tv_gala"),
    USER_FEEDBACK("user_feedback"),
    LOG_RECORD("log_record");
    
    private final String mShortName;

    private FeedbackEntry(String shortName) {
        this.mShortName = shortName;
    }

    public String toString() {
        return this.mShortName;
    }
}
