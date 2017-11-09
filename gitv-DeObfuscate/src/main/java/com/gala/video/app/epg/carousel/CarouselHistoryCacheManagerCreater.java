package com.gala.video.app.epg.carousel;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.ICarouselHistoryCacheManager;

public class CarouselHistoryCacheManagerCreater {
    public static ICarouselHistoryCacheManager create() {
        return new CarouselHistoryCacheManager();
    }
}
