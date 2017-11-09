package com.gala.video.lib.share.uikit.action.data;

import java.io.Serializable;

public class UcenterRecordAllData implements Serializable {
    private boolean mNeedToLogin;
    private String mTitle;

    public UcenterRecordAllData(String title, boolean needToLogin) {
        this.mTitle = title;
        this.mNeedToLogin = needToLogin;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setNeedToLogin(boolean needToLogin) {
        this.mNeedToLogin = needToLogin;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public boolean isNeedToLogin() {
        return this.mNeedToLogin;
    }
}
