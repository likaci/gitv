package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;

public class OpenOffLineListener extends VoiceOpenListener {
    public OpenOffLineListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return C0508R.string.voice_setting_offline_default;
    }

    protected boolean doOpen() {
        return true;
    }
}
