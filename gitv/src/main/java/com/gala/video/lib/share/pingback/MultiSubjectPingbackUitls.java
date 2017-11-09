package com.gala.video.lib.share.pingback;

public class MultiSubjectPingbackUitls {
    private static MultiSubjectPingbackUitls mSelf;
    private String mE;
    private String mS1;
    private String mS2;

    private MultiSubjectPingbackUitls() {
    }

    public static MultiSubjectPingbackUitls getInstance() {
        if (mSelf == null) {
            mSelf = new MultiSubjectPingbackUitls();
        }
        return mSelf;
    }

    public void setE(String e) {
        this.mE = e;
    }

    public void setS1(String s1) {
        this.mS1 = s1;
    }

    public void setS2(String s2) {
        this.mS2 = s2;
    }

    public String getS2() {
        return this.mS2;
    }

    public String getS1() {
        return this.mS1;
    }

    public String getE() {
        return this.mE;
    }
}
