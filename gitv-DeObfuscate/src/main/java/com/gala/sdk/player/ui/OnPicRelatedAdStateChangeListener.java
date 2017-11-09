package com.gala.sdk.player.ui;

import com.gala.sdk.player.AdType;

public interface OnPicRelatedAdStateChangeListener {
    void onAdError(AdType adType, int i, String str);

    void onAdHide(AdType adType, int i, String str);

    void onAdShow(AdType adType, int i, String str);
}
