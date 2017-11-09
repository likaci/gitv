package com.mcto.ads.internal.net;

class BasicInfo {
    public String code;
    public String subType;
    public String type;

    public BasicInfo(String type, String subType, String code) {
        this.type = type;
        this.subType = subType;
        this.code = code;
    }
}
