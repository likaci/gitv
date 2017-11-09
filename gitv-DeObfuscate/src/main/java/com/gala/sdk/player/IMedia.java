package com.gala.sdk.player;

import java.io.Serializable;
import java.util.Map;

public interface IMedia extends Serializable {

    public static final class BenefitType {
        public static final int CAN_NOT_PLAY = 1;
        public static final int CAN_PLAY = 4;
        public static final int PREVIEW_EPISODE = 3;
        public static final int PREVIEW_MINUITE = 2;
        public static final int UNKNOWN = 0;
    }

    public static final class DrmType {
        public static final int DRM_TYPE_CHINA = 102;
        public static final int DRM_TYPE_INTERTRUST = 101;
        public static final int DRM_TYPE_NONE = 100;
    }

    public static final class LiveType {
        public static final int LIVE_TYPE_CAROUSEL = 3;
        public static final int LIVE_TYPE_NORMAL = 1;
        public static final int LIVE_TYPE_TEMPORARY = 2;
        public static final int LIVE_TYPE_UNKNOWN = 0;
    }

    public static final class MediaType {
        public static final int NORMAL = 1;
        public static final int PANORAMIC = 2;
        public static final int THREE_DIMENSION = 3;
        public static final int UNKNOWN = 0;
    }

    String getAlbumId();

    int getDrmType();

    Map<String, Object> getExtra();

    String getLiveChannelId();

    String getLiveProgramId();

    int getLiveType();

    int getMediaType();

    long getPlayLength();

    int getStartPosition();

    String getTvId();

    boolean isLive();

    boolean isOffline();

    boolean isVip();

    void setAlbumId(String str);

    void setDrmType(int i);

    void setExtra(Map<String, Object> map);

    void setIsLive(boolean z);

    void setIsVip(boolean z);

    void setLiveChannelId(String str);

    void setLiveProgramId(String str);

    void setLiveType(int i);

    void setMediaType(int i);

    void setPlayLength(long j);

    void setStartPosition(int i);

    void setTvId(String str);
}
