package com.gala.video.app.player.albumdetail.ui;

import android.view.View;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.data.IVideo;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumVideoItem;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.uikit.UIKitEngine;
import java.util.List;

public interface IDetailOverlay {
    void clearAlbumListDefaultSelectedTextColor();

    UIKitEngine getAllViewEngine();

    ScreenMode getCurrentScreenMode();

    UIKitEngine getEngine();

    IMultiSubjectInfoModel getIntentModel();

    List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> list);

    boolean isWindowPlay();

    void notifyScreenModeSwitched(ScreenMode screenMode, boolean z);

    void notifyVideoDataCreated(AlbumVideoItem albumVideoItem);

    void notifyVideoPlayFinished();

    void notifyVideoSwitched(IVideo iVideo);

    void onActivityFinishing();

    void onActivityPaused();

    void onActivityResumed(int i);

    void onActivityStarted();

    void onActivityStopped();

    void playTraAllView();

    void setAllViewPlayGif();

    void setAppDownloadComplete(boolean z);

    void setCurrentFocusView(View view);

    void showAllViews();

    void showError(Object obj);

    void showFullDescPanel(AlbumInfo albumInfo);

    void showLoading();

    void startPlayFromAllView(boolean z);

    void startTrailers(PlayParams playParams);

    void updateAlbumDetailTotally(IVideo iVideo);

    void updateAlbumDetailTrailers(IVideo iVideo);

    void updateBasicInfo(AlbumInfo albumInfo);

    void updateCoupon(AlbumInfo albumInfo);

    void updateEpisodeList(AlbumInfo albumInfo);

    void updateFavInfo(AlbumInfo albumInfo);

    void updateTvod(AlbumInfo albumInfo);

    void updateVIPInfo(AlbumInfo albumInfo);
}
