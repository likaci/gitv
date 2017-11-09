package com.gala.tvapi.type;

public enum QuickMarkType {
    FC_MYACCOUNT("99b440412489ac88"),
    FC_VIPDETAIL("ab3a03076ffc1a21"),
    FC_PREVIEW("93dced1c7006d2a4");
    
    private String f533a;

    private QuickMarkType(String s) {
        this.f533a = null;
        this.f533a = s;
    }

    public final String toString() {
        return this.f533a;
    }
}
