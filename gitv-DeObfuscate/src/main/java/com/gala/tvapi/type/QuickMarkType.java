package com.gala.tvapi.type;

public enum QuickMarkType {
    FC_MYACCOUNT("99b440412489ac88"),
    FC_VIPDETAIL("ab3a03076ffc1a21"),
    FC_PREVIEW("93dced1c7006d2a4");
    
    private String f1148a;

    private QuickMarkType(String s) {
        this.f1148a = null;
        this.f1148a = s;
    }

    public final String toString() {
        return this.f1148a;
    }
}
