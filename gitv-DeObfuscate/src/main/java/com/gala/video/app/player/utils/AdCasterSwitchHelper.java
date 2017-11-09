package com.gala.video.app.player.utils;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.system.preference.AppPreference;

public class AdCasterSwitchHelper {
    public static final String SHARED_PREF_AD_CASTER_DISABLE = "ad_caster_disable";

    public static void updateSwitchValue(boolean disable) {
        new AppPreference(AppRuntimeEnv.get().getApplicationContext(), SHARED_PREF_AD_CASTER_DISABLE).save(SHARED_PREF_AD_CASTER_DISABLE, disable);
    }

    public static boolean isDisableAdCaster() {
        return new AppPreference(AppRuntimeEnv.get().getApplicationContext(), SHARED_PREF_AD_CASTER_DISABLE).getBoolean(SHARED_PREF_AD_CASTER_DISABLE, false);
    }
}
