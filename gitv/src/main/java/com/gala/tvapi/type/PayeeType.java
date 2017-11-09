package com.gala.tvapi.type;

public enum PayeeType {
    YINHE("1"),
    GALA("2");
    
    private String f503a;

    private PayeeType(String v) {
        this.f503a = v;
    }

    public final String toString() {
        return this.f503a;
    }
}
