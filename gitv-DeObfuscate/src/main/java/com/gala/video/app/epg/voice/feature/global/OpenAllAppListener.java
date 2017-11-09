package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenAllAppListener extends VoiceOpenListener {
    public OpenAllAppListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return C0508R.string.voice_more_app_default;
    }

    protected boolean doOpen() {
        EntryUtils.startAppList(getContext());
        return true;
    }
}
