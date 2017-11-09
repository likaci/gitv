package com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import java.util.List;

public interface ICarouselHistoryCacheManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements ICarouselHistoryCacheManager {
        public Object getInterface() {
            return this;
        }

        public static ICarouselHistoryCacheManager asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof ICarouselHistoryCacheManager)) {
                return null;
            }
            return (ICarouselHistoryCacheManager) wrapper;
        }
    }

    List<CarouselHistoryInfo> getCarouselHistoryList();

    void loadLocalToMemory();

    void put(CarouselHistoryInfo carouselHistoryInfo);
}
