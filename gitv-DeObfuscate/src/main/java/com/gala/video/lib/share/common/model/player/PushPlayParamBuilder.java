package com.gala.video.lib.share.common.model.player;

import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;

public class PushPlayParamBuilder extends AbsPlayParamBuilder {
    public MultiScreenParams mMultiScreenParams;
    public String mSe;

    public PushPlayParamBuilder setMultiScreenParams(MultiScreenParams params) {
        this.mMultiScreenParams = params;
        return this;
    }

    public PushPlayParamBuilder setSe(String se) {
        this.mSe = se;
        return this;
    }
}
