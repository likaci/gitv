package com.xcrash.crashreporter.bean;

import com.xcrash.crashreporter.anno.MessageAnnotation;

@MessageAnnotation(isEncode = true, name = "dragon_qos", requestUrl = "http://qosp.msg.71.am/mobile_qos_bizerror")
public class BizErrorStatistics {
    public String crplg;
    public String crplgv;
    public String crpo;
    public String pchv;
    public final String tt = "0";

    public BizErrorStatistics(String crpo, String plg, String plgv, String pchv) {
        this.crpo = crpo;
        this.crplg = plg;
        this.crplgv = plgv;
        this.pchv = pchv;
    }
}
