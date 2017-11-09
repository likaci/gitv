package com.gala.sdk.player;

public interface IMediaProfile {
    public static final String FEATURE_AUDIO_MULTI_TRACK = "audio_multi_track";
    public static final String FEATURE_CHINA_DRM = "drmc";
    public static final String FEATURE_DOLBY = "dolby";
    public static final String FEATURE_DOLBY_ATMOS = "dolby_atmos";
    public static final String FEATURE_DOLBY_VISION = "dolby_vision";
    public static final String FEATURE_H211 = "h211";
    public static final String FEATURE_HDR10 = "hdr_10";
    public static final String FEATURE_INTERTRUST_DRM = "drmt";
    public static final String FEATURE_NORMAL = "normal";

    boolean isSupport4K();
}
