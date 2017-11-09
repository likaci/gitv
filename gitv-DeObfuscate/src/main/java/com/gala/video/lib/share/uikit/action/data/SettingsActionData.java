package com.gala.video.lib.share.uikit.action.data;

import java.io.Serializable;

public class SettingsActionData implements Serializable {
    private String mName;
    private int mSettinsType;

    public void setSettinsType(int mSettinsType) {
        this.mSettinsType = mSettinsType;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getSettinsType() {
        return this.mSettinsType;
    }

    public String getName() {
        return this.mName;
    }
}
