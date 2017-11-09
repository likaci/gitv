package com.gala.video.app.player.data;

import com.gala.video.lib.framework.core.utils.StringUtils;

public class DetailBannerData {
    private String mAdId = "";
    private int mPosition = 0;
    private String mPositionId = "";

    public DetailBannerData(String positionid, int postion) {
        if (StringUtils.isEmpty((CharSequence) positionid)) {
            throw new IllegalArgumentException("positionid can't be null");
        }
        this.mPositionId = positionid;
        this.mPosition = postion;
    }

    public String getPositionId() {
        return this.mPositionId;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public String getAdId() {
        return this.mAdId;
    }

    public void setAdId(String mAdId) {
        this.mAdId = mAdId;
    }
}
