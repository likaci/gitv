package com.gala.video.app.epg.voice.function;

import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.VoiceEventParser;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ChnList;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.voice.utils.EntryUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import java.util.ArrayList;
import java.util.List;

public class OpenSearchResultListener extends VoiceListener {
    private static final String TAG = "OpenSearchResultListener";

    private static class MyListener implements IApiCallback<ApiResultAlbumList> {
        private ApiResultAlbumList mResult;

        private MyListener() {
        }

        public ApiResultAlbumList getAlbumList() {
            return this.mResult;
        }

        public void onException(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1569d(OpenSearchResultListener.TAG, "listener exception = ", e);
            }
            this.mResult = null;
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(OpenSearchResultListener.TAG, "success result=" + result);
            }
            this.mResult = result;
        }

        public static int getChannelId(ApiResultAlbumList list, String channelName) {
            if (StringUtils.isTrimEmpty(channelName) || list == null || list.chnList == null) {
                return -1;
            }
            for (ChnList channel : list.chnList) {
                if (channelName.equals(channel.chnName)) {
                    return channel.chnId;
                }
            }
            return -1;
        }
    }

    public OpenSearchResultListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "do open action");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        try {
            actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(3, "")) {
                protected boolean dispatchVoiceEvent(VoiceEvent event) {
                    PingBackUtils.setTabSrc("其他");
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(OpenSearchResultListener.TAG, "dispatch voice event");
                    }
                    String channelName = VoiceEventParser.parseChannelName(event);
                    int channelId = -1;
                    if (!StringUtils.isTrimEmpty(channelName)) {
                        channelId = MyListener.getChannelId(OpenSearchResultListener.this.search(event.getKeyword(), -1 + ""), channelName);
                    }
                    EntryUtils.searchAlbumByAlbumName(VoiceManager.instance().getSmartContext(), channelId, CreateInterfaceTools.createVoiceCommon().getFirstKeyWord(event), channelName);
                    return true;
                }
            });
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "do open action exception = ", e);
            e.printStackTrace();
        }
        return actions;
    }

    private ApiResultAlbumList search(String value, String channelId) {
        MyListener listener = new MyListener();
        TVApi.albumSearch.callSync(listener, value, channelId, "1", "20");
        return listener.getAlbumList();
    }
}
