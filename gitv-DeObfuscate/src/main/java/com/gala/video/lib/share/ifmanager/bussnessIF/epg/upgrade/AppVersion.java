package com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class AppVersion {
    private String md5;
    private String tip = "";
    private UpgradeType upgradeType = UpgradeType.NONE;
    private String url = "";
    private String version = "";

    public enum UpgradeType {
        NONE(-1),
        NORMAL(0),
        FORCE(1),
        MANUAL(2);
        
        final int _type;

        private UpgradeType(int type) {
            this._type = type;
        }

        public static UpgradeType valueOf(int type) {
            switch (type) {
                case 0:
                    return NORMAL;
                case 1:
                    return FORCE;
                case 2:
                    return MANUAL;
                default:
                    return NONE;
            }
        }
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UpgradeType getUpgradeType() {
        return this.upgradeType;
    }

    public void setUpgradeType(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
    }

    public void setUpgradeType(int upgradeType) {
        this.upgradeType = UpgradeType.valueOf(upgradeType);
    }

    public boolean isForceUpdate() {
        return this.upgradeType == UpgradeType.FORCE;
    }

    public boolean isNormalUpdate() {
        return this.upgradeType == UpgradeType.NORMAL;
    }

    public boolean isManualUpdate() {
        return this.upgradeType == UpgradeType.MANUAL;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5Info) {
        this.md5 = md5Info;
    }

    public String toString() {
        return "AppVersion [tip=" + this.tip + ", url=" + this.url + ", upgradeType=" + this.upgradeType + ", version=" + this.version + AlbumEnterFactory.SIGN_STR;
    }
}
