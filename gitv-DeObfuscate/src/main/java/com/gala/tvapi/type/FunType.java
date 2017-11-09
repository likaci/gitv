package com.gala.tvapi.type;

public enum FunType {
    NEWEST("homepageNew"),
    HOTEST("homepageHot"),
    MYMOVIE("myMovie");
    
    private String f1128a;

    private FunType(String value) {
        this.f1128a = value;
    }

    public final String getValue() {
        return this.f1128a;
    }
}
