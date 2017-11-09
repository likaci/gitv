package com.mcto.ads.internal.thirdparty;

import android.util.Log;
import java.net.URL;

public class ThirdPartyUtils {
    public static boolean enableMma(String trackingUrl, ThirdPartyConfig thirdPartyConfig) {
        Boolean enableTrackingProvider = (Boolean) thirdPartyConfig.enableMmaConfig.get(getTrackingProvider(trackingUrl));
        if (enableTrackingProvider == null || !enableTrackingProvider.booleanValue()) {
            return false;
        }
        return true;
    }

    public static TrackingProvider getTrackingProvider(String trackingUrl) {
        try {
            String host = new URL(trackingUrl).getHost();
            if (host.contains("admaster.com.cn")) {
                return TrackingProvider.ADMASTER;
            }
            if (host.contains("miaozhen.com")) {
                return TrackingProvider.MIAOZHEN;
            }
            if (host.contains("cr-nielsen.com")) {
                return TrackingProvider.NIELSEN;
            }
            if (host.contains("mma.ctrmi.com")) {
                return TrackingProvider.CTR;
            }
            return TrackingProvider.DEFAULT;
        } catch (Exception ex) {
            Log.e("a71_ads_client", "get trackingUrl provider error", ex);
        }
    }
}
