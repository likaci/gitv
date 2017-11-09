package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IPlayerConfigProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IPlayerConfigProvider {
        public Object getInterface() {
            return this;
        }

        public static IPlayerConfigProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IPlayerConfigProvider)) {
                return null;
            }
            return (IPlayerConfigProvider) wrapper;
        }
    }

    boolean canSeekBeforeStart();

    boolean disableGifAnimForDetailPage();

    boolean disableGifViewForCarouselChannel();

    boolean enablePlayerLocalServerConvergeStream();

    boolean enablePlayerLocalServerF4v2Hls();

    boolean enablePlayerMultiProcess();

    int getFixedSizeTypeForSurfaceHolder();

    int getPlayerTypeConfig();

    int getSurfaceFormat();

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

    boolean shouldUpdateSurfaceViewAfterStart();

    boolean useFileDescriptorForLocalPlayback();
}
