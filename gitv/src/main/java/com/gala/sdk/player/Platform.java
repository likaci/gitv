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
    private static final HashMap<String, Platform> a;
    private static final HashMap<String, Platform> b;
    private int f339a;
    private Locale f340a;
    private PlatformType f341a;
    private String f342a;
    private String f343b;

    public enum Locale {
        MAINLAND,
        TAIWAN
    }

    static {
        HashMap hashMap = new HashMap();
        a = hashMap;
        hashMap.put(KEY_PUSH_SOURCE_GPHONE, ANDROIDPHONE_PUSH_TV);
        a.put(KEY_PUSH_SOURCE_IPHONE, IPHONE_PUSH_TV);
        a.put(KEY_PUSH_SOURCE_IPAD, IPAD_PUSH_TV);
        a.put("", TV);
        a.put(KEY_PUSH_SOURCE_NULL, TV);
        hashMap = new HashMap();
        b = hashMap;
        hashMap.put(KEY_PUSH_SOURCE_GPHONE, ANDROIDPHONE_PUSH_TV_TAIWAN);
        b.put(KEY_PUSH_SOURCE_IPHONE, IPHONE_PUSH_TV_TAIWAN);
        b.put(KEY_PUSH_SOURCE_IPAD, IPAD_PUSH_TV_TAIWAN);
        b.put("", TV_TAIWAN);
        b.put(KEY_PUSH_SOURCE_NULL, TV_TAIWAN);
    }

    private Platform(Locale locale, String platformCode, int agentType, PlatformType type, String kSrc) {
        this.f340a = locale;
        this.f342a = platformCode;
        this.f339a = agentType;
        this.f341a = type;
        this.f343b = kSrc;
    }

    public Locale getLocale() {
        return this.f340a;
    }

    public String getPlatformCode() {
        return this.f342a;
    }

    public int getAgentType() {
        return this.f339a;
    }

    public PlatformType getApiPlatformType() {
        return this.f341a;
    }

    public static Platform get(Locale locale, String source) {
        Object obj = null;
        if (!StringUtils.isEmpty(source)) {
            obj = source.toLowerCase();
        }
        switch (locale) {
            case MAINLAND:
                return (Platform) a.get(obj);
            case TAIWAN:
                return (Platform) b.get(obj);
            default:
                return (Platform) a.get(obj);
        }
    }

    public String getPushKSrc() {
        return this.f343b;
    }
}
