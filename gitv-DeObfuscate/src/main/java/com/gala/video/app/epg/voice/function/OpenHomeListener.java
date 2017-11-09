package com.gala.video.app.epg.voice.function;

import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.voice.utils.EntryUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import java.util.ArrayList;
import java.util.List;

public class OpenHomeListener extends VoiceListener {
    private static final String TAG = "OpenHomeListener";

    public OpenHomeListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        List<AbsVoiceAction> actions = new ArrayList();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "do open action");
        }
        try {
            List<TabModel> tabModelList = TabProvider.getInstance().getTabInfo();
            if (tabModelList != null) {
                int size = tabModelList.size();
                for (int i = 0; i < size; i++) {
                    final int index = i;
                    actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(4, ((TabModel) tabModelList.get(i)).getTitle())) {
                        protected boolean dispatchVoiceEvent(VoiceEvent event) {
                            PingBackUtils.setTabSrc("其他");
                            EntryUtils.setHomeActivity(OpenHomeListener.this.mContext, index);
                            return true;
                        }
                    });
                }
            }
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "do open action exception = ", e);
        }
        return actions;
    }
}
