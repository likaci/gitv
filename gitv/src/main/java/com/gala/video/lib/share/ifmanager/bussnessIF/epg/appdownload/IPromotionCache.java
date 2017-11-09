package com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IPromotionCache extends IInterfaceWrapper {

    public static abstract class Wrapper implements IPromotionCache {
        public Object getInterface() {
            return this;
        }

        public static IPromotionCache asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IPromotionCache)) {
                return null;
            }
            return (IPromotionCache) wrapper;
        }
    }

    PromotionMessage getChildPromotion();

    PromotionMessage getChinaPokerAppPromotion();
}
