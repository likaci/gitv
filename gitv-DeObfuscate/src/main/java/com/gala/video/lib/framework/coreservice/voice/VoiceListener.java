package com.gala.video.lib.framework.coreservice.voice;

import android.content.Context;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.IVocal;
import java.util.List;

public abstract class VoiceListener implements IVocal {
    protected Context mContext;
    private int mPriority;

    protected abstract List<AbsVoiceAction> doOpenAction();

    public VoiceListener(Context context, int priority) {
        this.mContext = context;
        this.mPriority = priority;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        return doOpenAction();
    }
}
