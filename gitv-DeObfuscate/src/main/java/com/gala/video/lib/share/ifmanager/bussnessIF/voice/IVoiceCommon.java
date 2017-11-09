package com.gala.video.lib.share.ifmanager.bussnessIF.voice;

import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IVoiceCommon extends IInterfaceWrapper {

    public static abstract class Wrapper implements IVoiceCommon {
        public Object getInterface() {
            return this;
        }

        public static IVoiceCommon asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IVoiceCommon)) {
                return null;
            }
            return (IVoiceCommon) wrapper;
        }
    }

    AbsVoiceAction createAbsVoiceAction(int i, String str, Runnable runnable, KeyWordType keyWordType);

    AbsVoiceAction createAbsVoiceAction(String str, Runnable runnable, KeyWordType keyWordType);

    String getFirstKeyWord(VoiceEvent voiceEvent);
}
