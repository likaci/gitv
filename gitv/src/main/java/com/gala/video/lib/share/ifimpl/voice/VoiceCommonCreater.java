package com.gala.video.lib.share.ifimpl.voice;

import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;

public class VoiceCommonCreater {
    public static IVoiceCommon create() {
        return new VoiceCommon();
    }
}
