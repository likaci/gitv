package com.gala.tv.voice.service;

import android.util.Log;
import com.gala.tv.voice.VoiceEvent;

public abstract class AbsVoiceAction {
    private VoiceEvent f807a;
    private KeyWordType f808a;

    protected abstract boolean dispatchVoiceEvent(VoiceEvent voiceEvent);

    public final VoiceEvent getSupportedEvent() {
        return this.f807a;
    }

    public AbsVoiceAction(VoiceEvent voiceEvent) {
        this.f807a = voiceEvent;
        this.f808a = KeyWordType.DEFAULT;
    }

    public AbsVoiceAction(VoiceEvent voiceEvent, KeyWordType keyWordType) {
        this.f807a = voiceEvent;
        this.f808a = keyWordType;
    }

    public final KeyWordType getSupportedKeyWordType() {
        return this.f808a;
    }

    protected IVoiceFilter getVoiceFilter() {
        return DefaultVoiceFilter.getDefault();
    }

    public final boolean accept(VoiceEvent voiceEvent) {
        boolean accept;
        IVoiceFilter voiceFilter = getVoiceFilter();
        if (voiceFilter != null) {
            accept = voiceFilter.accept(this, voiceEvent);
        } else {
            accept = true;
        }
        Log.d("AbsVoiceHolder", "accept(supported=" + this.f807a + ", input=" + voiceEvent + ") return " + accept);
        return accept;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName()).append("(supportedVoiceEvent=").append(this.f807a).append(",supportedKeyWordType=").append(this.f808a).append(")");
        return stringBuilder.toString();
    }
}
