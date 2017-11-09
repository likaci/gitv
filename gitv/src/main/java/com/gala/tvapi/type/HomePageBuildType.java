package com.gala.tvapi.type;

public enum HomePageBuildType {
    JAVA("0"),
    COCOS2D("1");
    
    private String f504a;

    private HomePageBuildType(String value) {
        this.f504a = value;
    }

    public final String getValue() {
        return this.f504a;
    }
}
