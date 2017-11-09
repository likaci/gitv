package com.gala.sdk.player.data;

import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.utils.job.JobError;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import java.util.List;

public interface IVideoProvider {
    public static final int DATA_ERROR_DATA_EXCEPTION = -2147483644;
    public static final int EPISODE_LIST_ERROR = -2147483645;
    public static final String ERROR_NETWORK = "network_error";
    public static final int IKAN_PLAY_LIST_ERROR = -2147483646;
    public static final int PLAYLIST_FROM_INPUT = 3;
    public static final int PLAYLIST_FROM_ORIGINAL = 2;
    public static final int PLAYLIST_FROM_RECOMMEND = 1;
    public static final int PLAYLIST_FROM_RELATED = 5;
    public static final int PLAYLIST_FROM_TRAILER = 4;
    public static final int PLAYLIST_FROM_UNKNOWN = 0;
    public static final int RECOMMENDATION_MIDDLE = 1;
    public static final int SWITCH_FAIL = -1;
    public static final int SWITCH_NORMAL_TO_NORMAL = 0;
    public static final int SWITCH_NORMAL_TO_RECOMMEND = 6;
    public static final int SWITCH_NORMAL_TO_RELATED = 9;
    public static final int SWITCH_NORMAL_TO_SUPER = 1;
    public static final int SWITCH_NORMAL_TO_TRAILER = 2;
    public static final int SWITCH_RECOMMEND_TO_RECOMMEND = 8;
    public static final int SWITCH_RELATED_TO_NORMAL = 13;
    public static final int SWITCH_RELATED_TO_RECOMMEND = 12;
    public static final int SWITCH_RELATED_TO_RELATED = 10;
    public static final int SWITCH_RELATED_TO_SUPER = 11;
    public static final int SWITCH_SUPER_TO_RECOMMEND = 7;
    public static final int SWITCH_TRAILER_TO_NORMAL = 3;
    public static final int SWITCH_TRAILER_TO_RECOMMEND = 5;
    public static final int SWITCH_TRAILER_TO_TRAILER = 4;

    public interface AllChannelCallback {
        void onDataReady(TVChannelCarouselTag tVChannelCarouselTag, List<TVChannelCarousel> list);

        void onException(IVideo iVideo, String str, String str2);
    }

    public interface AllChannelDetailCallback {
        void onCacheReady(TVChannelCarouselTag tVChannelCarouselTag, List<CarouselChannelDetail> list);

        void onDataReady(TVChannelCarouselTag tVChannelCarouselTag, List<CarouselChannelDetail> list);

        void onException(IVideo iVideo, String str, String str2);
    }

    public interface ChannelDetailCallback {
        void onCacheReady(CarouselChannelDetail carouselChannelDetail);

        void onDataReady(CarouselChannelDetail carouselChannelDetail);

        void onException(IVideo iVideo, String str, String str2);
    }

    public interface DataLoadListener {
        public static final int TYPE_AUTH_VIP = 6;
        public static final int TYPE_BASIC = 1;
        public static final int TYPE_EPISODE = 2;
        public static final int TYPE_HISTORY = 3;
        public static final int TYPE_PLAYLIST = 5;
        public static final int TYPE_UNKNOWN = 0;

        void onBasicInfoReady(IVideo iVideo);

        void onEpisodeReady(IVideo iVideo);

        void onException(int i, IVideo iVideo, JobError jobError);

        void onFullEpisodeReady(IVideo iVideo);

        void onHistoryReady(IVideo iVideo);

        void onPlaylistReady(IVideo iVideo);
    }

    public interface PlayCheckListener {
        void onPlayCheckException(IVideo iVideo, JobError jobError);

        void onPlayCheckSuccess(IVideo iVideo);
    }

    public enum PlaylistType {
        IKAN_BODAN,
        IKAN_RECOMMEND
    }

    public interface ProgramListCallback {
        void onCacheReady(IVideo iVideo);

        void onDataReady(IVideo iVideo);

        void onException(IVideo iVideo, String str, String str2);
    }

    public interface RecommendationLoadListener {
        void onException(IVideo iVideo, String str, String str2);

        void onRecommendationReady(IVideo iVideo);
    }

    boolean addListener(DataLoadListener dataLoadListener);

    void addToPlaylist(List<Album> list, int i);

    void clearCache();

    IVideo createVideo(Album album);

    List<Album> getCacheRecommendList();

    IVideo getCurrent();

    CarouselChannelDetail getCurrentChannelDetail();

    IVideo getLiveVideo();

    IVideo getNext();

    List<IVideo> getPlaylist();

    List<Album> getPlaylistAlbums();

    String getPlaylistId();

    String getPlaylistName();

    int getPlaylistSize();

    int getPlaylistSource();

    IVideo getSource();

    SourceType getSourceType();

    boolean hasNext();

    boolean isPlaylistEmpty();

    boolean isPlaylistReady();

    int moveToNext();

    void release();

    boolean removeListener(DataLoadListener dataLoadListener);

    void setCurrentChannelDetail(CarouselChannelDetail carouselChannelDetail);

    void setNetworkAvaliable(boolean z);

    void startLoad();

    void startLoadAllChannelDetail(TVChannelCarouselTag tVChannelCarouselTag, AllChannelDetailCallback allChannelDetailCallback);

    void startLoadCarouselProgramList(ProgramListCallback programListCallback, TVChannelCarousel tVChannelCarousel);

    void startLoadCurrentChannelDetail(TVChannelCarousel tVChannelCarousel, ChannelDetailCallback channelDetailCallback);

    void startLoadPlaylist();

    void startLoadPushPlaylist(List<Album> list);

    void stopLoad();

    int switchPlaylist(PlayParams playParams);

    @Deprecated
    void switchSource(IVideo iVideo, SourceType sourceType);

    void switchToLiveVideo();

    int switchVideo(IVideo iVideo);
}
