package com.gala.sdk.player;

import android.view.KeyEvent;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import java.util.List;

public interface IGalaVideoPlayer {
    void addToPlayList(List<Album> list);

    void changeScreenMode(ScreenMode screenMode);

    void finish();

    ITrunkAdController getAdController();

    int getCurrentPosition();

    int getDuration();

    IVideo getNextVideo();

    ISceneActionProvider getSceneActionProvider();

    SourceType getSourceType();

    @Deprecated
    IVideo getSourceVideo();

    IVideo getVideo();

    IVideoOverlay getVideoOverlay();

    void handleErrorFinished();

    boolean handleKeyEvent(KeyEvent keyEvent);

    boolean isCompleted();

    boolean isPaused();

    boolean isPlaying();

    boolean isSleeping();

    void notifyChannelChangeByIndex(int i);

    void onErrorClicked();

    void onUserPause();

    void onUserPlay();

    void pause();

    void release();

    void replay();

    void setOnCarouselProgramClickListener(OnCarouselProgramClickListener onCarouselProgramClickListener);

    void setOnMultiScreenStateChangeListener(OnMultiScreenStateChangeListener onMultiScreenStateChangeListener);

    void setOnOutsideParamsChangeListener(OnOutsideParamsChangeListener onOutsideParamsChangeListener);

    void setOnRedirectOutPageListener(OnRedirectOutPageListener onRedirectOutPageListener);

    void setOnReleaseListener(OnReleaseListener onReleaseListener);

    void setOnUpdateLayoutListener(OnUpdateLayoutListener onUpdateLayoutListener);

    void setPushPlaylist(List<Album> list);

    void setVideoRatio(int i);

    void sleep();

    void start(int i);

    void switchBitStream(BitStream bitStream);

    void switchChannel(TVChannelCarousel tVChannelCarousel, String str);

    void switchPlaylist(PlayParams playParams);

    void switchVideo(IVideo iVideo, String str);

    void wakeUp();
}
