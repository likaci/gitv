package com.gala.tv.voice.service;

import android.util.Log;
import com.gala.tv.voice.VoiceEvent;

public abstract class AbsVoiceAction {
    private VoiceEvent a;
    private KeyWordType f410a;

    protected abstract boolean dispatchVoiceEvent(VoiceEvent voiceEvent);

    public final VoiceEvent getSupportedEvent() {
        return this.a;
    }

    public AbsVoiceAction(VoiceEvent voiceEvent) {
        this.a = voiceEvent;
        this.f410a = KeyWordType.DEFAULT;
    }

    public AbsVoiceAction(VoiceEvent voiceEvent, KeyWordType keyWordType) {
        this.a = voiceEvent;
        this.f410a = keyWordType;
    }

    public final KeyWordType getSupportedKeyWordType() {
        return this.f410a;
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
        Log.d("AbsVoiceHolder", "accept(supported=" + this.a + ", input=" + voiceEvent + ") return " + accept);
        return accept;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName()).append("(supportedVoiceEvent=").append(this.a).append(",supportedKeyWordType=").append(this.f410a).append(")");
        return stringBuilder.toString();
    }
}
