package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenSearchListener extends VoiceOpenListener {
    public OpenSearchListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return C0508R.string.voice_search_default;
    }

    protected boolean doOpen() {
        EntryUtils.startSearchActivity(getContext());
        return true;
    }
}
