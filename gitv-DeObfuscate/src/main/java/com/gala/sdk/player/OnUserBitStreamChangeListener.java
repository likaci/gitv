package com.gala.sdk.player;

import android.view.View;

public interface OnUserBitStreamChangeListener {
    void onBitStreamChange(View view, BitStream bitStream);

    void onHDRToggleChanged(View view, boolean z);
}
