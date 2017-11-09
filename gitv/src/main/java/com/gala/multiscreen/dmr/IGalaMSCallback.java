package com.gala.multiscreen.dmr;

import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;

public interface IGalaMSCallback {
    void onFlingEvent(KeyKind keyKind);

    void onKeyEvent(KeyKind keyKind);

    void onNotifyEvent(RequestKind requestKind, String str);

    void onSeekEvent(KeyKind keyKind);
}
