package com.gala.tvapi.type;

public enum PayeeType {
    YINHE("1"),
    GALA("2");
    
    private String f1139a;

    private PayeeType(String v) {
        this.f1139a = v;
    }

    public final String toString() {
        return this.f1139a;
    }
}
