package com.gala.sdk.player.constants;

import com.gala.sdk.player.Platform;
import com.gala.tvapi.type.PlatformType;
import java.util.HashMap;
import java.util.Locale;

public enum PlayerPlatform {
    TV_APP(PlatformType.NORMAL, PlayerPlatforms.P_TV, 28),
    MOBILE_ANDROID_APP(PlatformType.ANDROID_PHONE, PlayerPlatforms.P_GPhone, 21),
    MOBILE_IOS_APP(PlatformType.IPHONE, PlayerPlatforms.P_Iphone, 20),
    MOBILE_IPAD_APP(PlatformType.IPHONE, PlayerPlatforms.P_Iphone, 23);
    
    private static final HashMap<String, PlayerPlatform> PLATFORM_MAP = null;
    private int mAgentType;
    private PlatformType mApiPlatform;
    private PlayerPlatforms mNativePlayerPlatform;

    static {
        HashMap hashMap = new HashMap();
        PLATFORM_MAP = hashMap;
        hashMap.put(Platform.KEY_PUSH_SOURCE_GPHONE, MOBILE_ANDROID_APP);
        PLATFORM_MAP.put(Platform.KEY_PUSH_SOURCE_IPHONE, MOBILE_IOS_APP);
        PLATFORM_MAP.put(Platform.KEY_PUSH_SOURCE_IPAD, MOBILE_IPAD_APP);
        PLATFORM_MAP.put("", TV_APP);
        PLATFORM_MAP.put(null, TV_APP);
    }

    private PlayerPlatform(PlatformType apiPlatform, PlayerPlatforms nativePlayerPlatform, int agentType) {
        this.mApiPlatform = apiPlatform;
        this.mNativePlayerPlatform = nativePlayerPlatform;
        this.mAgentType = agentType;
    }

    public final PlatformType getApiPlatform() {
        return this.mApiPlatform;
    }

    public final PlayerPlatforms getNativePlayerPlatform() {
        return this.mNativePlayerPlatform;
    }

    public final int getAgentTypeForVipConcurrencyCheck() {
        return this.mAgentType;
    }

    public static PlayerPlatform get(String key) {
        return (PlayerPlatform) PLATFORM_MAP.get(key != null ? key.toLowerCase(Locale.ENGLISH) : null);
    }
}
