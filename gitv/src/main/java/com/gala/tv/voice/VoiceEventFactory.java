package com.gala.tv.voice;

import android.os.Bundle;
import com.gala.tv.voice.core.Log;
import com.gala.tv.voice.core.ParamsHelper;

public class VoiceEventFactory {

    public static class PlayVoiceEvent extends VoiceEvent {
        public PlayVoiceEvent(int i, String str) {
            super(i, str);
        }

        public PlayVoiceEvent setChannelName(String str) {
            Log.d("VoiceEventFactory", "setChannelName(channelName=" + str + ")");
            Bundle extras = getExtras();
            ParamsHelper.setChannelName(extras, str);
            putExtras(extras);
            return this;
        }

        public PlayVoiceEvent setEpisodeIndex(int i) {
            Log.d("VoiceEventFactory", "setEpisodeIndex(episodeIndex=" + i + ")");
            Bundle extras = getExtras();
            ParamsHelper.setEpisodeIndex(extras, i);
            putExtras(extras);
            return this;
        }
    }

    public static class SearchVoiceEvent extends VoiceEvent {
        public SearchVoiceEvent(int i, String str) {
            super(i, str);
        }

        public SearchVoiceEvent setChannelName(String str) {
            Bundle extras = getExtras();
            ParamsHelper.setChannelName(extras, str);
            putExtras(extras);
            return this;
        }
    }

    public static VoiceEvent createSeekToEvent(long j) {
        Log.d("VoiceEventFactory", "createSeekToEvent(" + j + ")");
        return new VoiceEvent(1, String.valueOf(j));
    }

    public static VoiceEvent createSeekOffsetEvent(long j) {
        Log.d("VoiceEventFactory", "createSeekOffsetEvent(" + j + ")");
        return new VoiceEvent(2, String.valueOf(j));
    }

    public static VoiceEvent createSelectEpisodeIndexEvent(int i) {
        Log.d("VoiceEventFactory", "createSelectEpisodeIndexEvent()");
        return new VoiceEvent(10, String.valueOf(i));
    }

    public static VoiceEvent createPreviousEpisodeEvent() {
        Log.d("VoiceEventFactory", "createPreviousEpisodeEvent()");
        return new VoiceEvent(15, "-1");
    }

    public static VoiceEvent createNextEpisodeEvent() {
        Log.d("VoiceEventFactory", "createNextEpisodeEvent()");
        return new VoiceEvent(15, "1");
    }

    public static VoiceEvent createKeywordsEvent(String str) {
        Log.d("VoiceEventFactory", "createKeywordsEvent(" + str + ")");
        return new VoiceEvent(4, str);
    }

    public static SearchVoiceEvent createSearchEvent(String str) {
        return new SearchVoiceEvent(3, str);
    }

    public static VoiceEvent createKeyEvent(int i) {
        Log.d("VoiceEventFactory", "createKeyEvent(" + i + ")");
        return new VoiceEvent(13, String.valueOf(i));
    }

    public static VoiceEvent createPositionEvent(int i, int i2) {
        Log.d("VoiceEventFactory", "createPositionEvent(row=" + i + ", column=" + i2 + ")");
        VoiceEvent voiceEvent = new VoiceEvent(6, i + ", " + i2);
        Bundle bundle = new Bundle();
        ParamsHelper.setRow(bundle, i);
        ParamsHelper.setColumn(bundle, i2);
        voiceEvent.putExtras(bundle);
        return voiceEvent;
    }

    public static PlayVoiceEvent createPlayEvent(String str) {
        Log.d("VoiceEventFactory", "createPlayEvent(keyword=" + str + ")");
        return new PlayVoiceEvent(16, str);
    }

    public static VoiceEvent createVoiceEvent(int i, String str) {
        Log.d("VoiceEventFactory", "createVoiceEvent(" + i + ", " + str + ")");
        return new VoiceEvent(i, str);
    }
}
