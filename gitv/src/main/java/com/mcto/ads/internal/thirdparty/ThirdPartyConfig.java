package com.mcto.ads.internal.thirdparty;

import java.util.HashMap;
import java.util.Map;

public class ThirdPartyConfig {
    public Map<TrackingProvider, Boolean> enableMmaConfig;

    public ThirdPartyConfig() {
        init();
    }

    public ThirdPartyConfig(Map<String, Object> cupidExtras) {
        init();
        if (!"1".equals(cupidExtras.get(ThirdPartyConstants.ENABLE_MMA_ADMASTER) + "")) {
            this.enableMmaConfig.put(TrackingProvider.ADMASTER, Boolean.valueOf(false));
        }
        if (!"1".equals(cupidExtras.get(ThirdPartyConstants.ENABLE_MMA_MIAOZHEN) + "")) {
            this.enableMmaConfig.put(TrackingProvider.MIAOZHEN, Boolean.valueOf(false));
        }
        if (!"1".equals(cupidExtras.get(ThirdPartyConstants.ENABLE_MMA_NIELSEN) + "")) {
            this.enableMmaConfig.put(TrackingProvider.NIELSEN, Boolean.valueOf(false));
        }
        if (!"1".equals(cupidExtras.get(ThirdPartyConstants.ENABLE_MMA_CTR) + "")) {
            this.enableMmaConfig.put(TrackingProvider.CTR, Boolean.valueOf(false));
        }
    }

    public ThirdPartyConfig(int configBundle) {
        init();
        if ((configBundle & 1) == 0) {
            this.enableMmaConfig.put(TrackingProvider.ADMASTER, Boolean.valueOf(false));
        }
        if ((configBundle & 2) == 0) {
            this.enableMmaConfig.put(TrackingProvider.MIAOZHEN, Boolean.valueOf(false));
        }
        if ((configBundle & 4) == 0) {
            this.enableMmaConfig.put(TrackingProvider.NIELSEN, Boolean.valueOf(false));
        }
        if ((configBundle & 8) == 0) {
            this.enableMmaConfig.put(TrackingProvider.CTR, Boolean.valueOf(false));
        }
    }

    private void init() {
        this.enableMmaConfig = new HashMap<TrackingProvider, Boolean>() {
            {
                put(TrackingProvider.ADMASTER, Boolean.valueOf(true));
                put(TrackingProvider.MIAOZHEN, Boolean.valueOf(true));
                put(TrackingProvider.NIELSEN, Boolean.valueOf(true));
                put(TrackingProvider.CTR, Boolean.valueOf(true));
            }
        };
    }
}
