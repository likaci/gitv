package com.gala.video.lib.share.pingback;

public class SoloTabPingbackUitls {
    private static SoloTabPingbackUitls mSelf;
    private String mE;
    private String mS2;
    private String tabName;

    private SoloTabPingbackUitls() {
    }

    public static SoloTabPingbackUitls getInstance() {
        if (mSelf == null) {
            mSelf = new SoloTabPingbackUitls();
        }
        return mSelf;
    }

    public String getTabName() {
        return this.tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public void setE(String e) {
        this.mE = e;
    }

    public void setS2(String s2) {
        this.mS2 = s2;
    }

    public String getS2() {
        return this.mS2;
    }

    public String getE() {
        return this.mE;
    }
}
