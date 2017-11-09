package com.gala.multiscreen.dmr;

import com.gala.multiscreen.dmr.model.MSMessage.PlayKind;
import com.gala.multiscreen.dmr.model.MSMessage.PushKind;
import com.gala.multiscreen.dmr.model.MSMessage.SeekTimeKind;

public interface IStandardMSCallback {
    void onNotify(PlayKind playKind, String str);

    void onSeek(SeekTimeKind seekTimeKind, long j);

    void setAVTransportURI(PushKind pushKind, String str, boolean z);
}
