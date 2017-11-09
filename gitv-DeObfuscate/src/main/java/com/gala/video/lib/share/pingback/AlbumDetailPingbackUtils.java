package com.gala.video.lib.share.pingback;

public class AlbumDetailPingbackUtils {
    private static AlbumDetailPingbackUtils mSelf;
    private String mAllViewS2;
    private String mEntryAllTitle;
    private String mS1;
    private String mS2;
    private String mTabSrc;

    public String getTabSrc() {
        return this.mTabSrc;
    }

    public void setTabSrc(String tabSrc) {
        this.mTabSrc = tabSrc;
    }

    public String getAllViewS2() {
        return this.mAllViewS2;
    }

    public void setAllViewS2(String mAllViewS2) {
        this.mAllViewS2 = mAllViewS2;
    }

    private AlbumDetailPingbackUtils() {
    }

    public static AlbumDetailPingbackUtils getInstance() {
        if (mSelf == null) {
            mSelf = new AlbumDetailPingbackUtils();
        }
        return mSelf;
    }

    public void setS1(String s1) {
        this.mS1 = s1;
    }

    public void setS2(String s2) {
        this.mS2 = s2;
    }

    public void setEntryAllTitle(String entryAllTitle) {
        this.mEntryAllTitle = entryAllTitle;
    }

    public String getS1() {
        return this.mS1;
    }

    public String getS2() {
        return this.mS2;
    }

    public String getEntryAllTitle() {
        return this.mEntryAllTitle;
    }
}
