package com.gala.video.lib.share.pingback;

public class PingBackCollectionFieldUtils {
    private static String cardIndex;
    private static String incomeSrc = "others";
    private static String itemIndex;
    private static String mE;
    private static String mNow_c1;
    private static String mNow_qpid;
    private static String mRfr;
    private static String tabIndex;
    private static String tabName = "";

    public static String getNow_c1() {
        return mNow_c1;
    }

    public static void setNow_c1(String now_c1) {
        mNow_c1 = now_c1;
    }

    public static String getNow_qpid() {
        return mNow_qpid;
    }

    public static void setNow_qpid(String now_qpid) {
        mNow_qpid = now_qpid;
    }

    public static String getRfr() {
        return mRfr;
    }

    public static void setRfr(String rfr) {
        mRfr = rfr;
    }

    public static String getE() {
        return mE;
    }

    public static void setE(String e) {
        mE = e;
    }

    public static String getIncomeSrc() {
        return incomeSrc;
    }

    public static void setIncomeSrc(String src) {
        incomeSrc = src;
    }

    public static String getTabName() {
        return tabName;
    }

    public static void setTabName(String tabName) {
        tabName = tabName;
    }

    public static String getTabIndex() {
        return tabIndex;
    }

    public static void setTabIndex(String tabIndex) {
        tabIndex = tabIndex;
    }

    public static String getCardIndex() {
        return cardIndex;
    }

    public static void setCardIndex(String cardIndex) {
        cardIndex = cardIndex;
    }

    public static String getItemIndex() {
        return itemIndex;
    }

    public static void setItemIndex(String itemIndex) {
        itemIndex = itemIndex;
    }
}
