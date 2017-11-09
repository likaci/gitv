package com.gala.sdk.player;

public interface OnShowHintListener {

    public enum HintType {
        BUFFER_LAG,
        NET_CHECK,
        NOT_SUPPORT_4K
    }

    void onShowHint(HintType hintType);
}
