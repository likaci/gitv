package com.gala.tvapi.type;

public enum PackageType {
    NO_PACKAGE(0),
    GOLD_PACKAGE(1),
    SILVER_PACKAGE(3),
    PLATINUM_PACKAGE(4);
    
    private int f1135a;

    private PackageType(int type) {
        this.f1135a = -1;
        this.f1135a = type;
    }

    public final int getPackageType() {
        return this.f1135a;
    }
}
