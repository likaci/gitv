package com.gala.sdk.plugin.server.upgrade;

import android.webkit.URLUtil;
import com.gala.sdk.plugin.server.utils.Util;

public class UpgradeInfo {
    private String mMd5;
    private String mTip;
    private int mType;
    private String mUrl;
    private String mVersion;

    UpgradeInfo() {
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getTip() {
        return this.mTip;
    }

    public void setTip(String tip) {
        this.mTip = tip;
    }

    public String getMd5() {
        return this.mMd5;
    }

    public void setMd5(String md5) {
        this.mMd5 = md5;
    }

    public boolean needCheckMd5() {
        return !Util.isEmpty(this.mMd5);
    }

    public String toString() {
        return "UpgradeInfo(" + ", type=" + this.mType + ", url=" + this.mUrl + ", version=" + this.mVersion + ", tip=" + this.mTip + ", md5=" + this.mMd5 + ")";
    }

    public static boolean isVaild(UpgradeInfo upgradeInfo) {
        if (upgradeInfo == null || !URLUtil.isValidUrl(upgradeInfo.getUrl())) {
            return false;
        }
        return true;
    }
}
