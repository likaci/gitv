package com.gala.tv.voice.service;

import com.gala.tv.voice.VoiceEvent;

public interface IVoiceFilter {
    boolean accept(AbsVoiceAction absVoiceAction, VoiceEvent voiceEvent);
}
