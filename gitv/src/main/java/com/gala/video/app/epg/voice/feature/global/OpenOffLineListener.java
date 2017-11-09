package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;

public class OpenOffLineListener extends VoiceOpenListener {
    public OpenOffLineListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return R.string.voice_setting_offline_default;
    }

    protected boolean doOpen() {
        return true;
    }
}
