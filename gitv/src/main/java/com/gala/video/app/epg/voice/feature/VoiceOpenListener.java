package com.gala.video.app.epg.voice.feature;

import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;
import java.util.ArrayList;
import java.util.List;

public abstract class VoiceOpenListener extends VoiceListener {
    private static final String TAG = "VoiceOpenListener";
    private String mDescription;

    protected abstract boolean doOpen();

    protected abstract int getDescriptionId();

    public VoiceOpenListener(Context context, int priority) {
        super(context, priority);
    }

    protected Context getContext() {
        return VoiceManager.instance().getSmartContext();
    }

    protected String getDescription() {
        if (this.mDescription == null) {
            this.mDescription = this.mContext.getString(getDescriptionId());
        }
        return this.mDescription;
    }

    protected List<AbsVoiceAction> doOpenAction() {
        List<AbsVoiceAction> actions = new ArrayList();
        try {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "support voice description = " + getDescription());
            }
            actions.add(new AbsVoiceAction(VoiceEventFactory.createKeywordsEvent(getDescription())) {
                protected boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
                    return VoiceOpenListener.this.dispatchVoiceEvent(voiceEvent);
                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, "support voice action exception", e);
        }
        return actions;
    }

    private boolean dispatchVoiceEvent(VoiceEvent event) {
        IVoiceCommon provider = CreateInterfaceTools.createVoiceCommon();
        if (StringUtils.isTrimEmpty(provider.getFirstKeyWord(event))) {
            return false;
        }
        if (4 == event.getType() && getDescription() != null && getDescription().equals(provider.getFirstKeyWord(event))) {
            return doOpen();
        }
        return false;
    }
}
