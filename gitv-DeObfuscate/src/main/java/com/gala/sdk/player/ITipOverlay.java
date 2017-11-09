package com.gala.sdk.player;

public interface ITipOverlay {
    void hideTip();

    void showTip(ITip iTip);

    void showTip(CharSequence charSequence);
}
