package com.gala.sdk.player.ui;

public interface OnPageAdvertiseStateChangeListener {
    public static final int SELECTION_BANNER = 102;
    public static final int SWITCHING_BITSTREAM_AD = 101;

    void onPageAdHide(int i, Object obj);

    void onPageAdShow(int i, Object obj);
}
