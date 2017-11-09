package com.gala.tvapi.type;

import com.gala.video.lib.share.pingback.PingBackParams.Values;

public enum PlatformType {
    NORMAL("28", ""),
    HAIXIN("93", ""),
    SKYWORTH_VIPPROJECT("130", "27"),
    ALIVIP("131", ""),
    TCL_GOLIVE("132", ""),
    YINHESHANDONG("", ""),
    YINHEVIP("135", ""),
    ANDROID_PHONE("21", ""),
    IPHONE("20", ""),
    STORMTV("119", ""),
    YINHEBOYUAN("", ""),
    BOYUANANHUI("", ""),
    YIDONGANHUI("", ""),
    YIDONGZHEJIANG("", ""),
    VR("140", ""),
    YINHEBOYUANNATION("", ""),
    HOLATEKVIP("151", ""),
    LENOVOSHIYUNVIP("152", ""),
    IPAD("23", ""),
    TAIWAN(Values.value18, ""),
    CVTE("169", ""),
    DRPENG("168", ""),
    VR_ANDROID_PHONE("176", ""),
    VR_ANDROID_ALLINONE("176", ""),
    VR_ANDROID_GAME("", ""),
    YINHESHANXIYD("", ""),
    FOXCONN("189", ""),
    SKYWORTH_DIGITAL("191", ""),
    WSTV("192", ""),
    XIAOMI_CNTV("7", ""),
    XIAOMI_GITV("7", ""),
    MIFENG("218", ""),
    HUAWEI("", ""),
    TCLGLOBALPLAY("230", "");
    
    private String f195a;
    private String b;

    private PlatformType(String agenttype, String type) {
        this.f195a = agenttype;
        this.b = type;
    }

    public final String getAgentType() {
        return this.f195a;
    }

    public final String getType() {
        return this.b;
    }
}
