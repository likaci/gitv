package com.gala.report.core.upload.tracker;

public enum TrackerBIZType {
    _BIZTYPE_CRASH("tv_logRecord_crash"),
    _BIZTYPE_AUTO("tv_Logrecord_auto"),
    _BIZTYPE_FEEDBACK("tv_feedback");
    
    private final String mShortName;

    private TrackerBIZType(String shortName) {
        this.mShortName = shortName;
    }

    public String toString() {
        return this.mShortName;
    }
}
