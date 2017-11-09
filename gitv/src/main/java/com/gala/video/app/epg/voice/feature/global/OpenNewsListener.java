package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenNewsListener extends VoiceOpenListener {
    public OpenNewsListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return R.string.voice_daily_news_default;
    }

    protected boolean doOpen() {
        EntryUtils.startDailyLoopActivity(getContext(), "homerec[1]");
        return true;
    }
}
