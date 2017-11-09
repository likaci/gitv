package com.gala.video.app.epg.voice.function;

import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.voice.utils.EntryUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OpenChannelListener extends VoiceListener {
    private static final int RETRY_DELAY_MS = 5000;
    private static final int RETRY_MAX_COUNT = 3;
    private static final String TAG = "OpenChannelListener";
    private String mChannelNameSuffix;
    private final AtomicInteger mRetryCount = new AtomicInteger(0);

    public OpenChannelListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "do open channel action");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        try {
            this.mChannelNameSuffix = this.mContext.getString(C0508R.string.voice_channel_name_suffix_default);
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "do open action exception", e);
        }
        return actions;
    }

    private AbsVoiceAction createAbsVoiceActionByChannel(final Channel channel, String keyword) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "create voice action by channel keyword = " + keyword);
        }
        return new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(4, keyword)) {
            protected boolean dispatchVoiceEvent(VoiceEvent arg0) {
                PingBackUtils.setTabSrc("其他");
                EntryUtils.startChannelListActivity(VoiceManager.instance().getSmartContext(), channel);
                return true;
            }
        };
    }
}
