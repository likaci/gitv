package com.gala.video.app.player.provider;

import com.gala.video.app.player.config.PlayerConfigManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerConfigProvider.Wrapper;

public class PlayerConfigProvider extends Wrapper {
    public boolean isSupport4K() {
        return PlayerConfigManager.getPlayerConfig().isSupport4K();
    }

    public boolean isSupportH211() {
        return PlayerConfigManager.getPlayerConfig().isSupportH211();
    }

    public boolean isSupportSmallWindowPlay() {
        return PlayerConfigManager.getPlayerConfig().isSupportSmallWindowPlay();
    }

    public boolean isSupportAnimation() {
        return PlayerConfigManager.getPlayerConfig().isSupportAnimation();
    }

    public boolean isDisableADCache() {
        return PlayerConfigManager.getPlayerConfig().isDisableADCache();
    }

    public boolean isMediaPlayerPauseBeforeSeek() {
        return PlayerConfigManager.getPlayerConfig().isMediaPlayerPauseBeforeSeek();
    }

    public boolean canSeekBeforeStart() {
        return PlayerConfigManager.getPlayerConfig().canSeekBeforeStart();
    }

    public boolean isUseNativePlayerCarousel() {
        return PlayerConfigManager.getPlayerConfig().isUseNativePlayerCarousel();
    }

    public boolean disableGifViewForCarouselChannel() {
        return PlayerConfigManager.getPlayerConfig().disableGifViewForCarouselChannel();
    }

    public boolean disableGifAnimForDetailPage() {
        return PlayerConfigManager.getPlayerConfig().disableGifAnimForDetailPage();
    }

    public boolean enablePlayerMultiProcess() {
        return PlayerConfigManager.getPlayerConfig().enablePlayerMultiProcess();
    }

    public boolean enablePlayerLocalServerF4v2Hls() {
        return PlayerConfigManager.getPlayerConfig().enablePlayerLocalServerF4v2Hls();
    }

    public boolean enablePlayerLocalServerConvergeStream() {
        return PlayerConfigManager.getPlayerConfig().enablePlayerLocalServerConvergeStream();
    }

    public int getPlayerTypeConfig() {
        return PlayerConfigManager.getPlayerConfig().getPlayerTypeConfig();
    }

    public int getSurfaceFormat() {
        return PlayerConfigManager.getPlayerConfig().getSurfaceFormat();
    }

    public int getFixedSizeTypeForSurfaceHolder() {
        return PlayerConfigManager.getPlayerConfig().getFixedSizeTypeForSurfaceHolder();
    }

    public boolean useFileDescriptorForLocalPlayback() {
        return PlayerConfigManager.getPlayerConfig().useFileDescriptorForLocalPlayback();
    }

    public boolean isDisableAdCaster() {
        return PlayerConfigManager.getPlayerConfig().isDisableAdCaster();
    }

    public boolean shouldUpdateSurfaceViewAfterStart() {
        return PlayerConfigManager.getPlayerConfig().shouldUpdateSurfaceViewAfterStart();
    }

    public boolean isSupportDolbyVision() {
        return PlayerConfigManager.getPlayerConfig().isSupportDolbyVision();
    }

    public boolean isSupportHDR10() {
        return PlayerConfigManager.getPlayerConfig().isSupportHDR10();
    }

    public boolean isDisable4KH264() {
        return PlayerConfigManager.getPlayerConfig().isDisable4KH264();
    }

    public boolean isDeviceSupport4KH211() {
        return PlayerConfigManager.getPlayerConfig().isDeviceSupport4KH211();
    }
}
