package com.gala.sdk.player;

import com.gala.sdk.player.data.IVideo;
import java.util.List;

public interface OnMultiScreenStateChangeListener {
    void onAdStart();

    void onBitStreamChange(BitStream bitStream);

    void onBitStreamListReady(List<BitStream> list);

    void onError(IVideo iVideo, ISdkError iSdkError);

    void onMoiveVipStateReady(boolean z);

    void onMovieComplete();

    void onMoviePause();

    void onMovieStart();

    void onMovieStop();

    void onMovieSwitched(IVideo iVideo);

    void onScreenModeSwitched(ScreenMode screenMode);

    void onSeekEnd();
}
