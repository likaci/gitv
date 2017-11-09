package com.gala.video.app.epg.home.data.pingback;

import com.gala.video.app.epg.appdownload.utils.AppUtils;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.tvos.appdetailpage.client.Constants;
import java.util.HashMap;

public final class HomePageClickPingback extends HomePingback {
    private ClickPingback mHomePingbackType;

    public HomePageClickPingback(ClickPingback pingbackType) {
        this.mHomePingbackType = pingbackType;
    }

    protected final void addDefaultField(HashMap<String, String> map) {
        map.put(Keys.T, "20");
        map.put("rt", "i");
        dealOtherKey(map);
    }

    public void dealOtherKey(HashMap<String, String> map) {
        switch (ItemDataType.getItemTypeByValue((String) map.remove(ClickPingbackUtils.PINGBACK_CLICK_TYPE))) {
            case RECOMMEND_APP:
                String r_recommend_app = (String) map.get("r");
                PromotionAppInfo promotionAppInfo = null;
                PromotionMessage promotionMessage = null;
                if ("chinapokerapp".equals(r_recommend_app)) {
                    promotionMessage = PromotionCache.instance().getChinaPokerAppPromotion();
                } else if ("childapp".equals(r_recommend_app)) {
                    promotionMessage = PromotionCache.instance().getChildPromotion();
                }
                if (promotionMessage != null) {
                    promotionAppInfo = promotionMessage.getDocument().getAppInfo();
                }
                map.put("state", AppUtils.isInstalled(AppRuntimeEnv.get().getApplicationContext(), promotionAppInfo != null ? promotionAppInfo.getAppPckName() : "") ? Constants.PINGBACK_ACTION_INSTALL_DONE : Constants.PINGBACK_ACTION_UNINSTALL_DONE);
                return;
            default:
                return;
        }
    }

    public String getType() {
        return this.mHomePingbackType.getValue();
    }
}
