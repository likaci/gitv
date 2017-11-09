package com.gala.video.lib.share.common.model.player;

public abstract class AbsPlayParamBuilder {
    public String mBuySource = "";
    public String mFrom = "";
    public String mTabSource = "";

    public AbsPlayParamBuilder setFrom(String from) {
        this.mFrom = from;
        return this;
    }

    public AbsPlayParamBuilder setBuySource(String buySource) {
        this.mBuySource = buySource;
        return this;
    }

    public AbsPlayParamBuilder setTabSource(String tabSource) {
        this.mTabSource = tabSource;
        return this;
    }
}
