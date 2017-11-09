package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenHotPlayListener extends VoiceOpenListener {
    public OpenHotPlayListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return C0508R.string.voice_hot_play_default;
    }

    protected boolean doOpen() {
        EntryUtils.startHotPlayPage(getContext());
        return true;
    }
}
