package com.gala.sdk.player.ui;

import android.view.KeyEvent;
import android.view.View;
import com.gala.sdk.event.OnNetworkChangeListener;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.IBufferOverlay;
import com.gala.sdk.player.IBufferOverlay.OnBufferlistener;
import com.gala.sdk.player.ISeekOverlay;
import com.gala.sdk.player.IStateOverlay;
import com.gala.sdk.player.ISubtitleOverlay;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.sdk.player.ITipOverlay;
import com.gala.sdk.player.ITrunkAdController;
import com.gala.sdk.player.IVideoOverlay;
import com.gala.sdk.player.OnFullScreenHintChangedListener;
import com.gala.sdk.player.OnUserBitStreamChangeListener;
import com.gala.sdk.player.OnUserChangeVideoRatioListener;
import com.gala.sdk.player.OnUserChannelChangeListener;
import com.gala.sdk.player.OnUserPlayPauseListener;
import com.gala.sdk.player.OnUserSkipHeadTailChangeListener;
import com.gala.sdk.player.OnUserVideoChangeListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import java.util.List;

public interface IPlayerOverlay extends OnNetworkChangeListener, IBufferOverlay, OnBufferlistener, ISeekOverlay, IStateOverlay, ISubtitleOverlay, IThreeDimensional, ITipOverlay, IVideoOverlay {
    void changeScreenMode(boolean z, float f);

    void clearError();

    void clearMediaControllerState();

    boolean dispatchKeyEvent(KeyEvent keyEvent);

    View getContentView();

    void hide();

    void hideFullScreenHint();

    void hideLoadingView();

    boolean isInFullScreenMode();

    boolean isMenuPanelShowing();

    void menuPanelEnableShow(boolean z);

    void notifyChannelChangeByIndex(int i);

    void notifyChannelInfoChange(int i, boolean z, boolean z2);

    void notifyVideoDataChanged(int i);

    void onDestroy();

    void onStop();

    void setAdController(ITrunkAdController iTrunkAdController);

    void setAlbumId(String str);

    void setAllTagList(List<TVChannelCarouselTag> list);

    void setCurrentChannel(TVChannelCarousel tVChannelCarousel);

    void setCurrentVideo(IVideo iVideo);

    void setIsShowGallery(boolean z);

    void setOnFullScreenHintChangedListener(OnFullScreenHintChangedListener onFullScreenHintChangedListener);

    void setOnPageAdvertiseStateChangeListener(OnPageAdvertiseStateChangeListener onPageAdvertiseStateChangeListener);

    void setOnRequestChannelInfoListener(OnRequestChannelInfoListener onRequestChannelInfoListener);

    void setOnUserBitStreamChangeListener(OnUserBitStreamChangeListener onUserBitStreamChangeListener);

    void setOnUserChangeVideoRatioListener(OnUserChangeVideoRatioListener onUserChangeVideoRatioListener);

    void setOnUserChannelChangeListener(OnUserChannelChangeListener onUserChannelChangeListener);

    void setOnUserPlayPauseListener(OnUserPlayPauseListener onUserPlayPauseListener);

    void setOnUserSkipHeadTailChangeListener(OnUserSkipHeadTailChangeListener onUserSkipHeadTailChangeListener);

    void setOnUserVideoChangeListener(OnUserVideoChangeListener onUserVideoChangeListener);

    void setSourceType(SourceType sourceType);

    void showFullScreenHint(FullScreenHintType fullScreenHintType);

    void showLoading(String str, long j);

    void updateAdCountDown(int i);

    void updateBitStream(List<BitStream> list, BitStream bitStream);

    void updateBitStreamDefinition(BitStream bitStream);

    void updateSkipHeadAndTail(boolean z);
}
