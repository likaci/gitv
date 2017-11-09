package com.gala.tv.voice.service;

import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.core.VoiceUtils;

public class DefaultVoiceFilter implements IVoiceFilter {
    private static DefaultVoiceFilter a = new DefaultVoiceFilter();

    public static DefaultVoiceFilter getDefault() {
        return a;
    }

    public boolean accept(AbsVoiceAction absVoiceAction, VoiceEvent voiceEvent) {
        if (absVoiceAction == null || voiceEvent == null) {
            return false;
        }
        int type = absVoiceAction.getSupportedEvent().getType();
        int type2 = voiceEvent.getType();
        if (type != -1) {
            if (type != type2) {
                return false;
            }
            if (type == 4) {
                switch (absVoiceAction.getSupportedKeyWordType()) {
                    case FUZZY:
                        return VoiceUtils.contain(voiceEvent.getKeyword(), absVoiceAction.getSupportedEvent().getKeyword());
                    case RESERVED:
                        return VoiceUtils.match(voiceEvent.getKeyword(), absVoiceAction.getSupportedEvent().getKeyword());
                    default:
                        return VoiceUtils.equal(voiceEvent.getKeyword(), absVoiceAction.getSupportedEvent().getKeyword());
                }
            } else if (type == 15 || type == 10) {
                return VoiceUtils.equal(voiceEvent.getKeyword(), absVoiceAction.getSupportedEvent().getKeyword());
            }
        }
        return true;
    }
}
