package com.gala.sdk.player;

import android.widget.RelativeLayout;

public class BaseAdData {
    private int a;
    private RelativeLayout f330a;
    private String f331a;
    private int b;

    public BaseAdData setAdType(int type) {
        this.b = type;
        return this;
    }

    public int getType() {
        return this.b;
    }

    public BaseAdData setAdView(RelativeLayout rl) {
        this.f330a = rl;
        return this;
    }

    public RelativeLayout getAdView() {
        return this.f330a;
    }

    public BaseAdData setAdTxt(String txt) {
        this.f331a = txt;
        return this;
    }

    public String getAdTxt() {
        return this.f331a;
    }

    public BaseAdData setAdID(int id) {
        this.a = id;
        return this;
    }

    public int getID() {
        return this.a;
    }
}
