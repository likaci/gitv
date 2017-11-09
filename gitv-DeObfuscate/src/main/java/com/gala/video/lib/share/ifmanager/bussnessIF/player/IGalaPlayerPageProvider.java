package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import android.content.Context;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.LivePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.PushPlayParamBuilder;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IGalaPlayerPageProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IGalaPlayerPageProvider {
        public Object getInterface() {
            return this;
        }

        public static IGalaPlayerPageProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IGalaPlayerPageProvider)) {
                return null;
            }
            return (IGalaPlayerPageProvider) wrapper;
        }
    }

    void startAlbumDetailPlayerPage(Context context, AlbumDetailPlayParamBuilder albumDetailPlayParamBuilder);

    void startBasePlayerPage(Context context, BasePlayParamBuilder basePlayParamBuilder);

    void startCarouselPlayerPage(Context context, CarouselPlayParamBuilder carouselPlayParamBuilder);

    void startLivePlayerPage(Context context, LivePlayParamBuilder livePlayParamBuilder);

    void startNewsDetailPlayerPage(Context context, NewsDetailPlayParamBuilder newsDetailPlayParamBuilder);

    void startPlayerAdapterSettingPage(Context context);

    void startPushPlayerPage(Context context, PushPlayParamBuilder pushPlayParamBuilder);
}
