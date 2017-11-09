package com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen;

import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.sdk.player.IEventInput;
import com.gala.tv.voice.service.AbsVoiceAction;
import java.util.List;

public interface ISuperEventInput extends IEventInput {
    int getLastPosition();

    List<AbsVoiceAction> getSupportedPlaybackVoices(List<AbsVoiceAction> list);

    boolean onDlnaEvent(KeyKind keyKind);

    void onGetSceneAction(KeyValue keyValue);

    void onPhoneSeekEvent(int i);
}
