package com.gala.tvapi.type;

public enum TopListType {
    HOT("wee"),
    TOP("cmm");
    
    private String f1155a;

    private TopListType(String v) {
        this.f1155a = v;
    }

    public final String toString() {
        return this.f1155a;
    }
}
