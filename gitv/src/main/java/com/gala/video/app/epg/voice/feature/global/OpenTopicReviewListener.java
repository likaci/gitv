package com.gala.video.app.epg.voice.feature.global;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.voice.feature.VoiceOpenListener;
import com.gala.video.app.epg.voice.utils.EntryUtils;

public class OpenTopicReviewListener extends VoiceOpenListener {
    public OpenTopicReviewListener(Context context, int priority) {
        super(context, priority);
    }

    protected int getDescriptionId() {
        return R.string.voice_topic_review_default;
    }

    protected boolean doOpen() {
        EntryUtils.startTopicReviewPage(getContext());
        return true;
    }
}
