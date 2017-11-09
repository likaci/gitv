package com.gala.sdk.player;

import com.gala.tvapi.tv2.model.Album;

public interface OnRedirectOutPageListener {
    public static final int LOGIN_TYPE_CHANGE_BITSTREAM_CHGRA = 1;
    public static final int LOGIN_TYPE_CHANGE_BITSTREAM_TIP_RALOGTIPS = 2;

    void redirectToBuyPage(int i, String str, Album album);

    void redirectToLoginPage(int i, BitStream bitStream);
}
