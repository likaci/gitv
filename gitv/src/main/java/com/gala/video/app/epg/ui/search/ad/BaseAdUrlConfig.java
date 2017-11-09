package com.gala.video.app.epg.ui.search.ad;

import android.os.Build;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.UserType;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.AdsClient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseAdUrlConfig {
    protected Map<String, String> mAdUrlParamsMap;

    public BaseAdUrlConfig() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        this.mAdUrlParamsMap = new HashMap(30);
        this.mAdUrlParamsMap.put("device_id", TVApiBase.getTVApiProperty().getPassportDeviceId());
        this.mAdUrlParamsMap.put(Keys.TV_ID, "");
        this.mAdUrlParamsMap.put(Keys.V_ID, "");
        this.mAdUrlParamsMap.put(Keys.PLAYER_ID, Project.getInstance().getBuild().getAdPlayerId());
        this.mAdUrlParamsMap.put(Keys.APP_VERSION, Project.getInstance().getBuild().getVersionString());
        this.mAdUrlParamsMap.put(Keys.RES_INDEX, Values.value16);
        this.mAdUrlParamsMap.put(Keys.G, "");
        this.mAdUrlParamsMap.put(Keys.ALBUM_ID, "");
        this.mAdUrlParamsMap.put(Keys.CLIENT_TYPE, "gtv");
        this.mAdUrlParamsMap.put(Keys.BROWSER_INFO, "");
        this.mAdUrlParamsMap.put("channel_id", "");
        this.mAdUrlParamsMap.put(Keys.MDOEL_KEY, Project.getInstance().getBuild().getVrsUUID());
        this.mAdUrlParamsMap.put(Keys.USER_AGENT, Build.MODEL);
        this.mAdUrlParamsMap.put("udid", DeviceUtils.getMd5FormatMacAddr());
        this.mAdUrlParamsMap.put(Keys.VIDEO_EVENT_ID, UUID.randomUUID().toString());
        this.mAdUrlParamsMap.put(Keys.PREROLL_LIMIT, "0");
        this.mAdUrlParamsMap.put(Keys.SCREEN_INDEX, "9");
        Map map = this.mAdUrlParamsMap;
        String str = Keys.SDK_VERSION;
        AdsClientUtils.getInstance();
        map.put(str, AdsClient.getSDKVersion());
        this.mAdUrlParamsMap.put(Keys.IS_VIP, isVip);
        this.mAdUrlParamsMap.put(Keys.AD_TYPE, "");
        this.mAdUrlParamsMap.put(Keys.APP_ID, "1");
        this.mAdUrlParamsMap.put(Keys.VIDEO_DURATION, "");
        this.mAdUrlParamsMap.put(Keys.E_A, "1");
        this.mAdUrlParamsMap.put(Keys.NW, CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo());
        this.mAdUrlParamsMap.put(Keys.TOTAL_VIEWED_DURATION, "");
        this.mAdUrlParamsMap.put(Keys.TOTAL_VIEWED_VIDEO_NUMBER, "");
        this.mAdUrlParamsMap.put(Keys.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
        this.mAdUrlParamsMap.put(Keys.PASSPORT_COOKIE, GetInterfaceTools.getIGalaAccountManager().getAuthCookie());
    }

    public BaseAdUrlConfig add(String key, String value) {
        this.mAdUrlParamsMap.put(key, value);
        return this;
    }

    public String getKey(String key) {
        return (String) this.mAdUrlParamsMap.get(key);
    }
}
