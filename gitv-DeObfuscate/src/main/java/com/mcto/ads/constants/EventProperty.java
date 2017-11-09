package com.mcto.ads.constants;

import com.mcto.ads.internal.persist.DBConstants;

public enum EventProperty {
    EVENT_PROP_KEY_PLAY_TYPE(DBConstants.DB_KEY_PLAY_TYPE),
    EVENT_PROP_KEY_CLICK_AREA("clickArea"),
    EVENT_PROP_KEY_PLAY_DURATION("playDuration"),
    EVENT_PROP_KEY_AD_ZONE_ID("adZoneId"),
    EVENT_PROP_KEY_TIME_SLICE(Interaction.KEY_TIME_SLICE);
    
    private final String value;

    private EventProperty(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
