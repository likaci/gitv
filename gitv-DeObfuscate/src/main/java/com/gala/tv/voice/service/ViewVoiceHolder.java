package com.gala.tv.voice.service;

import android.view.View;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;

public class ViewVoiceHolder extends AbsVoiceAction {
    private View f817a;

    public ViewVoiceHolder(String str, View view) {
        super(VoiceEventFactory.createKeywordsEvent(str), KeyWordType.FUZZY);
        this.f817a = view;
    }

    public boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        return VoiceViewHelper.performView(this.f817a);
    }
}
