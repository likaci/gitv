package com.gala.video.lib.share.ifimpl.voice;

import android.os.Handler;
import android.os.Looper;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon.Wrapper;

class VoiceCommon extends Wrapper {
    private static final String TAG = "VoiceCommon";

    @Deprecated
    public String getFirstKeyWord(VoiceEvent event) {
        if (event == null) {
            return null;
        }
        return event.getKeyword();
    }

    public AbsVoiceAction createAbsVoiceAction(String keyword, Runnable runnable, KeyWordType type) {
        return createAbsVoiceAction(4, keyword, runnable, type);
    }

    public AbsVoiceAction createAbsVoiceAction(int voiceEventType, String keyword, final Runnable runnable, KeyWordType type) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("VoiceCommon", "VoiceCommon>createAbsVoiceAction= voiceEventType = " + voiceEventType + " ; keyword = " + keyword + " ; type = " + type);
        }
        return new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(voiceEventType, keyword), type) {

            class C17631 implements Runnable {
                C17631() {
                }

                public void run() {
                    runnable.run();
                }
            }

            protected boolean dispatchVoiceEvent(VoiceEvent event) {
                PingBackUtils.setTabSrc("其他");
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d("VoiceCommon", "createAbsVoiceAction>dispatchVoiceEvent");
                }
                new Handler(Looper.getMainLooper()).post(new C17631());
                return true;
            }
        };
    }
}
