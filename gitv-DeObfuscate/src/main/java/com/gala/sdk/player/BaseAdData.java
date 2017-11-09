package com.gala.sdk.player;

import android.widget.RelativeLayout;

public class BaseAdData {
    private int f647a;
    private RelativeLayout f648a;
    private String f649a;
    private int f650b;

    public BaseAdData setAdType(int type) {
        this.f650b = type;
        return this;
    }

    public int getType() {
        return this.f650b;
    }

    public BaseAdData setAdView(RelativeLayout rl) {
        this.f648a = rl;
        return this;
    }

    public RelativeLayout getAdView() {
        return this.f648a;
    }

    public BaseAdData setAdTxt(String txt) {
        this.f649a = txt;
        return this;
    }

    public String getAdTxt() {
        return this.f649a;
    }

    public BaseAdData setAdID(int id) {
        this.f647a = id;
        return this;
    }

    public int getID() {
        return this.f647a;
    }
}
