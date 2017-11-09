package com.gala.video.lib.share.pingback;

public class UcenterPingbackUtils {
    private static UcenterPingbackUtils mSelf;
    private String mE;

    private UcenterPingbackUtils() {
    }

    public static UcenterPingbackUtils getInstance() {
        if (mSelf == null) {
            mSelf = new UcenterPingbackUtils();
        }
        return mSelf;
    }

    public void setE(String e) {
        this.mE = e;
    }

    public String getE() {
        return this.mE;
    }
}
