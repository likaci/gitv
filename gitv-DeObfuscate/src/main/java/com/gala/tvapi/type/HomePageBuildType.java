package com.gala.tvapi.type;

public enum HomePageBuildType {
    JAVA("0"),
    COCOS2D("1");
    
    private String f1130a;

    private HomePageBuildType(String value) {
        this.f1130a = value;
    }

    public final String getValue() {
        return this.f1130a;
    }
}
