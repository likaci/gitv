package com.gala.video.app.epg.voice.feature.setting;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenAudioListener extends VoiceOpenListener {
    public OpenAudioListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return R.string.voice_setting_audio_default;
    }

    protected boolean doOpen() {
        EntryUtils.startSettingActivity(getContext(), EntryUtils.ACTION_SETTING_AUDIO);
        return true;
    }
}
