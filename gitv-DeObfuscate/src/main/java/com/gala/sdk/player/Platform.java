package com.gala.sdk.player;

import com.gala.sdk.utils.StringUtils;
import com.gala.tvapi.type.PlatformType;
import java.util.HashMap;

public class Platform {
    public static final Platform ANDROIDPHONE_PUSH_TV = new Platform(Locale.MAINLAND, "00020000000000000000-04000000001000000000", 21, PlatformType.ANDROID_PHONE, "02022001010000000000");
    public static final Platform ANDROIDPHONE_PUSH_TV_TAIWAN = new Platform(Locale.TAIWAN, "00020000000010000000-04000000001010000000", 21, PlatformType.ANDROID_PHONE, "02022001010010000000");
    public static final Platform IPAD_PUSH_TV = new Platform(Locale.MAINLAND, "00030000000000000000-04000000001000000000", 23, PlatformType.IPAD, "03032001010000000000");
    public static final Platform IPAD_PUSH_TV_TAIWAN = new Platform(Locale.TAIWAN, "00030000000010000000-04000000001010000000", 23, PlatformType.IPAD, "03032001010010000000");
    public static final Platform IPHONE_PUSH_TV = new Platform(Locale.MAINLAND, "00030000000000000000-04000000001000000000", 20, PlatformType.IPHONE, "02032001010000000000");
    public static final Platform IPHONE_PUSH_TV_TAIWAN = new Platform(Locale.TAIWAN, "00030000000010000000-04000000001010000000", 20, PlatformType.IPHONE, "02032001010010000000");
    public static final String KEY_PUSH_SOURCE_EMPTY = "";
    public static final String KEY_PUSH_SOURCE_GPHONE = "gphone";
    public static final String KEY_PUSH_SOURCE_IPAD = "ipad";
    public static final String KEY_PUSH_SOURCE_IPHONE = "iphone";
    public static final String KEY_PUSH_SOURCE_NULL = null;
    public static final Platform TV = new Platform(Locale.MAINLAND, "04022001010000000000", 28, PlatformType.NORMAL, "04022001010000000000");
    public static final Platform TV_TAIWAN = new Platform(Locale.TAIWAN, "04022001010010000000", 18, PlatformType.TAIWAN, "04022001010010000000");
    private static final HashMap<String, Platform> f664a;
    private static final HashMap<String, Platform> f665b;
    private int f666a;
    private Locale f667a;
    private PlatformType f668a;
    private String f669a;
    private String f670b;

    public enum Locale {
        MAINLAND,
        TAIWAN
    }

    static {
        HashMap hashMap = new HashMap();
        f664a = hashMap;
        hashMap.put(KEY_PUSH_SOURCE_GPHONE, ANDROIDPHONE_PUSH_TV);
        f664a.put(KEY_PUSH_SOURCE_IPHONE, IPHONE_PUSH_TV);
        f664a.put(KEY_PUSH_SOURCE_IPAD, IPAD_PUSH_TV);
        f664a.put("", TV);
        f664a.put(KEY_PUSH_SOURCE_NULL, TV);
        hashMap = new HashMap();
        f665b = hashMap;
        hashMap.put(KEY_PUSH_SOURCE_GPHONE, ANDROIDPHONE_PUSH_TV_TAIWAN);
        f665b.put(KEY_PUSH_SOURCE_IPHONE, IPHONE_PUSH_TV_TAIWAN);
        f665b.put(KEY_PUSH_SOURCE_IPAD, IPAD_PUSH_TV_TAIWAN);
        f665b.put("", TV_TAIWAN);
        f665b.put(KEY_PUSH_SOURCE_NULL, TV_TAIWAN);
    }

    private Platform(Locale locale, String platformCode, int agentType, PlatformType type, String kSrc) {
        this.f667a = locale;
        this.f669a = platformCode;
        this.f666a = agentType;
        this.f668a = type;
        this.f670b = kSrc;
    }

    public Locale getLocale() {
        return this.f667a;
    }

    public String getPlatformCode() {
        return this.f669a;
    }

    public int getAgentType() {
        return this.f666a;
    }

    public PlatformType getApiPlatformType() {
        return this.f668a;
    }

    public static Platform get(Locale locale, String source) {
        Object obj = null;
        if (!StringUtils.isEmpty(source)) {
            obj = source.toLowerCase();
        }
        switch (locale) {
            case MAINLAND:
                return (Platform) f664a.get(obj);
            case TAIWAN:
                return (Platform) f665b.get(obj);
            default:
                return (Platform) f664a.get(obj);
        }
    }

    public String getPushKSrc() {
        return this.f670b;
    }
}
