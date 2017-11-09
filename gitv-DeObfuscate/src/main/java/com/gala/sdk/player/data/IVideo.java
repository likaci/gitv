package com.gala.sdk.player.data;

import android.util.SparseArray;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.IMedia;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Cast;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.result.ApiResultPushAlbum;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.CornerMark;
import com.gala.tvapi.vrs.result.ApiResultVideoInfo;
import java.util.List;
import java.util.Map;

public interface IVideo extends IMedia {
    public static final int FLAG_ARTICLES = 64;
    public static final int FLAG_BODAN = 2;
    public static final int FLAG_CAROUSEL = 16;
    public static final int FLAG_EPISODE = 1;
    public static final int FLAG_ERROR = 32768;
    public static final int FLAG_RECOMMENDATION = 4;
    public static final int FLAG_STARS = 32;
    public static final int FLAG_SUPER = 8;
    public static final int FLAG_VIDEO_INFO = 3;
    public static final int FLOWERS_SHOW_TYPE_DEFAULT = 0;
    public static final int FLOWERS_SHOW_TYPE_RECOMMEND = 1;
    public static final int PLAY_TIME_BEGIN = -1;
    public static final int PLAY_TIME_END = -2;

    public interface OnVideoDataChangedListener {
        void onException(int i);

        void onVideoDataChanged(int i);
    }

    public enum VideoKind {
        VIDEO_SINGLE,
        VIDEO_SOURCE,
        VIDEO_EPISODE,
        ALBUM_EPISODE,
        ALBUM_SOURCE
    }

    boolean addListener(OnVideoDataChangedListener onVideoDataChangedListener);

    IVideo clone();

    void copyFrom(Album album);

    void copyFrom(Episode episode);

    void copyVideoListFrom(IVideo iVideo);

    boolean equalAlbum(IVideo iVideo);

    boolean equalAlbum(Album album);

    boolean equalVideo(IVideo iVideo);

    boolean equalVrsTv(Album album);

    Album getAlbum();

    String getAlbumDesc();

    String getAlbumDetailPic();

    String getAlbumFocus();

    String getAlbumId();

    String getAlbumName();

    String getAlbumOriginPrice();

    String getAlbumPic();

    String getAlbumPrice();

    String getAlbumSubName();

    String getAlbumTvPic();

    AlbumType getAlbumType();

    List<BitStream> getAllBitStreams();

    String getBOSS_CID();

    String getBOSS_T();

    String getBOSS_U();

    String getBodanName();

    List<IVideo> getBodanVideos();

    List<IVideo> getCarouseProgramList();

    TVChannelCarousel getCarouselChannel();

    Cast getCast();

    int getChannelId();

    String getChannelName();

    CornerMark getCornerMark();

    String getCouponCount();

    BitStream getCurrentBitStream();

    Album getCurrentCarouselProgram();

    String getDirector();

    int getEndTime();

    Episode getEpisode();

    int getEpisodeIndex();

    int getEpisodeMaxOrder();

    List<IVideo> getEpisodeVideos();

    int getEpisodesTotalCount();

    String getEventId();

    String getExtraImageUrl();

    int getFlowerShowType();

    String getFocus();

    int getHeaderTime();

    String getIChannelId();

    String getIssueTime();

    VideoKind getKind();

    String getLiveChannelId();

    long getLiveEndTime();

    long getLiveStartTime();

    Map<Integer, String> getLiveUrls();

    String getMainActor();

    String getPictureUrl();

    List<BitStream> getPlayBitStreams();

    String getPlayCount();

    int getPlayOrder();

    int getPlayTime();

    int getPlaylistSource();

    int getPlaylistTreeHashCode();

    int getPreviewTime();

    int getPreviewType();

    IVideoProvider getProvider();

    List<IVideo> getRecommendations();

    String getScore();

    String getSourceCode();

    SourceType getSourceType();

    String getSourceUpdateTime();

    List<IStarData> getStarList();

    String getStrategy();

    String getStreamVer();

    int getSuperId();

    List<IVideo> getSuperList();

    String getSuperName();

    String getTag();

    int getTailerTime();

    int getTvCount();

    String getTvId();

    String getTvName();

    int getTvSets();

    CornerMark getVarietyMark();

    String getVarietyName();

    String getVid();

    String getVideoName();

    int getVideoPlayTime();

    List<BitStream> getVipBitStreams();

    SparseArray<String> getVrsVidMap();

    boolean has4K();

    boolean hasDolby();

    boolean hasH211();

    boolean hasHDR();

    boolean is3d();

    boolean is4K();

    boolean isAlbumCoupon();

    boolean isAlbumSinglePay();

    boolean isAlbumVip();

    boolean isBodanFilled();

    boolean isBodanTvSeriesVideo();

    boolean isCanDownload();

    boolean isDolby();

    boolean isEpisodeFilled();

    boolean isExclusive();

    boolean isFavored();

    boolean isFlower();

    boolean isH211();

    boolean isLive();

    boolean isLiveVipShowTrailer();

    boolean isPictureVertical();

    boolean isPreview();

    boolean isRecommendationsFilled();

    boolean isSeries();

    boolean isSourceType();

    boolean isStarArticlesFilled();

    boolean isStarListFilled();

    boolean isSuperFilled();

    boolean isTrailer();

    boolean isTvSeries();

    boolean isVip();

    boolean isVipAuthorized();

    void notifyArticleListReady();

    boolean removeListener(OnVideoDataChangedListener onVideoDataChangedListener);

    void setAlbum(Album album);

    void setAlbumCoupon(boolean z);

    void setAlbumDesc(String str);

    void setAlbumDetailPic(String str);

    void setAlbumId(String str);

    void setAlbumName(String str);

    void setAlbumOriginPrice(String str);

    void setAlbumPrice(String str);

    void setAlbumSinglePay(boolean z);

    void setAlbumVip(boolean z);

    void setBOSS_CID(String str);

    void setBOSS_T(String str);

    void setBOSS_U(String str);

    void setBodan(List<Album> list);

    void setCarouseProgramList(List<IVideo> list);

    void setCarouselChannel(TVChannelCarousel tVChannelCarousel);

    void setCast(Cast cast);

    void setChannelId(int i);

    void setChannelName(String str);

    void setCouponCount(String str);

    void setCurrentBitStream(BitStream bitStream);

    void setCurrentCarouselProgram(Album album);

    void setEpisodeMaxOrder(int i);

    void setEpisodes(List<Episode> list, int i);

    void setExtraImageUrl(String str);

    void setFavored(boolean z);

    void setFocus(String str);

    void setHeaderTime(int i);

    void setIChannelId(String str);

    void setIsLive(boolean z);

    void setIsPreview(boolean z);

    void setIsTrailer(boolean z);

    void setIssueTime(String str);

    void setLiveUrls(Map<Integer, String> map);

    void setLiveVipShowTrailer(boolean z);

    void setPictureVertical(boolean z);

    void setPlayCount(String str);

    void setPlayOrder(int i);

    void setPlaylistSource(int i);

    void setPlaylistTreeHashCode(int i);

    void setPreviewTime(int i);

    void setPreviewType(int i);

    void setProvider(IVideoProvider iVideoProvider);

    void setRecommendations(List<Album> list);

    void setScore(String str);

    void setSourceCode(String str);

    void setSourceUpdateTime(String str);

    void setStarList(List<IStarData> list);

    void setStrategy(String str);

    void setStreamVer(String str);

    void setSuperId(int i);

    void setSuperList(List<Album> list);

    void setSuperName(String str);

    void setTag(String str);

    void setTailerTime(int i);

    void setTvCount(int i);

    void setTvId(String str);

    void setTvSbtitle(String str);

    void setTvSeries(boolean z);

    void setTvSets(int i);

    void setVarietyName(String str);

    void setVideoPlayTime(int i);

    void setVipAuthorized(boolean z);

    void setVrsAlbumId(String str);

    void setVrsTvId(String str);

    void setVrsVid(String str);

    void setVrsVidMap(SparseArray<String> sparseArray);

    boolean shouldUpDateBitStream();

    String toStringBrief();

    void updateBitStreamList(List<BitStream> list);

    void updateMultiInfo(ApiResultPushAlbum apiResultPushAlbum);

    void updatePlayHistory(Album album);

    void updatePushInfo(ApiResultVideoInfo apiResultVideoInfo);

    void updateTvServerInfo(Album album);
}
