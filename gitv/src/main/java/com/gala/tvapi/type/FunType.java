package com.gala.tvapi.type;

public enum FunType {
    NEWEST("homepageNew"),
    HOTEST("homepageHot"),
    MYMOVIE("myMovie");
    
    private String f204a;

    private FunType(String value) {
        this.f204a = value;
    }

    public final String getValue() {
        return this.f204a;
    }
}
