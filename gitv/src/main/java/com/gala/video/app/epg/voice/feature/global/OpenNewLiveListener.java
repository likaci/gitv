package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenNewLiveListener extends VoiceOpenListener {
    public OpenNewLiveListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return R.string.voice_new_live_default;
    }

    protected boolean doOpen() {
        EntryUtils.startNewLivePage(getContext());
        return true;
    }
}
