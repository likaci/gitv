package com.gala.video.app.player.config.playerconfig;

public interface IPlayerConfig {
    boolean canSeekBeforeStart();

    boolean canSeekBeforeStartAD();

    boolean disableGifAnimForDetailPage();

    boolean disableGifViewForCarouselChannel();

    boolean disableHcdnMultiProcess();

    boolean enablePlayerLocalServerConvergeStream();

    boolean enablePlayerLocalServerF4v2Hls();

    boolean enablePlayerMultiProcess();

    int getFixedSizeTypeForSurfaceHolder();

    String getJsonConfig();

    int getPlayerTypeConfig();

    int getSurfaceFormat();

    String getUniPlayerDataConfigJson();

    boolean isDeviceSupport4KH211();

    boolean isDisable4KH264();

    boolean isDisableADCache();

    boolean isDisableAdCaster();

    boolean isMediaPlayerPauseBeforeSeek();

    boolean isSupport4K();

    boolean isSupportAnimation();

    boolean isSupportDolbyVision();

    boolean isSupportH211();

    boolean isSupportHDR10();

    boolean isSupportSmallWindowPlay();

    boolean isUseNativePlayerCarousel();

    void load();

    boolean ready();

    boolean shouldUpdateSurfaceViewAfterStart();

    boolean useFileDescriptorForLocalPlayback();
}
