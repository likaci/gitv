package com.gala.tvapi.type;

public enum TopListType {
    HOT("wee"),
    TOP("cmm");
    
    private String f536a;

    private TopListType(String v) {
        this.f536a = v;
    }

    public final String toString() {
        return this.f536a;
    }
}
