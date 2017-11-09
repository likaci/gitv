package com.gala.video.app.epg.home.promotion.local;

import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IPromotionCache.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import java.io.IOException;
import java.util.HashMap;

public class PromotionCache extends Wrapper {
    public static final String CHILD_APP_TAG = "child_app";
    public static final String CHINAPOKER_APP_TAG = "chinapoker_app";

    private static class SingletonHelper {
        private static PromotionCache sIns = new PromotionCache();

        private SingletonHelper() {
        }
    }

    private PromotionCache() {
    }

    public static PromotionCache instance() {
        return SingletonHelper.sIns;
    }

    public synchronized boolean save(HashMap<String, PromotionMessage> messageHashMap) {
        boolean z;
        try {
            SerializableUtils.write(messageHashMap, HomeDataConfig.HOME_PROMOTION_APP);
            z = true;
        } catch (IOException e) {
            e.printStackTrace();
            z = false;
        }
        return z;
    }

    public synchronized HashMap<String, PromotionMessage> get() {
        HashMap<String, PromotionMessage> hashMap;
        try {
            hashMap = (HashMap) SerializableUtils.read(HomeDataConfig.HOME_PROMOTION_APP);
        } catch (Exception e) {
            e.printStackTrace();
            hashMap = new HashMap();
        }
        return hashMap;
    }

    public synchronized PromotionMessage getChinaPokerAppPromotion() {
        PromotionMessage promotionMessage;
        try {
            HashMap<String, PromotionMessage> messages = (HashMap) SerializableUtils.read(HomeDataConfig.HOME_PROMOTION_APP);
            if (messages != null) {
                promotionMessage = (PromotionMessage) messages.get(CHINAPOKER_APP_TAG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        promotionMessage = null;
        return promotionMessage;
    }

    public synchronized PromotionMessage getChildPromotion() {
        PromotionMessage promotionMessage;
        try {
            HashMap<String, PromotionMessage> messages = (HashMap) SerializableUtils.read(HomeDataConfig.HOME_PROMOTION_APP);
            if (messages != null) {
                promotionMessage = (PromotionMessage) messages.get(CHILD_APP_TAG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        promotionMessage = null;
        return promotionMessage;
    }
}
