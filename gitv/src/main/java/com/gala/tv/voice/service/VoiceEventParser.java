package com.gala.tv.voice.service;

import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.core.ParamsHelper;

public class VoiceEventParser {
    public static String parseChannelName(VoiceEvent voiceEvent) {
        return ParamsHelper.parseChannelName(voiceEvent.getExtras());
    }

    public static int parseEpisodeIndex(VoiceEvent voiceEvent) {
        return ParamsHelper.parseEpisodeIndex(voiceEvent.getExtras());
    }

    public static int parseRow(VoiceEvent voiceEvent) {
        return ParamsHelper.parseRow(voiceEvent.getExtras());
    }

    public static int parseColumn(VoiceEvent voiceEvent) {
        return ParamsHelper.parseColumn(voiceEvent.getExtras());
    }
}
