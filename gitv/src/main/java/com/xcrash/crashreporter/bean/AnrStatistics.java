package com.xcrash.crashreporter.bean;

import com.xcrash.crashreporter.anno.MessageAnnotation;

@MessageAnnotation(isEncode = true, name = "mirror_qos", requestUrl = "http://qosp.msg.71.am/mobile_qos_anr")
public class AnrStatistics {
    public String crpo;
    public String pchv;
    public String plg;
    public String plgv;
    public final String tt = "0";

    public AnrStatistics(String crpo, String plg, String plgv, String pchv) {
        this.crpo = crpo;
        this.plg = plg;
        this.plgv = plgv;
        this.pchv = pchv;
    }
}
