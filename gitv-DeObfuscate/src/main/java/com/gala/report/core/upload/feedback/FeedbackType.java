package com.gala.report.core.upload.feedback;

public enum FeedbackType {
    PLAYER_ERROR("播放器报错"),
    CLIENT_CRASH("客户端异常"),
    SUGGEST("改善建议"),
    COMMON("普通问题");
    
    private final String mShortName;

    private FeedbackType(String shortName) {
        this.mShortName = shortName;
    }

    public String toString() {
        return this.mShortName;
    }
}
