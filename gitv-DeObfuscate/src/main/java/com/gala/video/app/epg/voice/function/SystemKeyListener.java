package com.gala.video.app.epg.voice.function;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import java.util.ArrayList;
import java.util.List;

public class SystemKeyListener extends VoiceListener {
    private static final String TAG = "SystemKeyListener";
    private static final Instrumentation mInstrumentation = new Instrumentation();

    public SystemKeyListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "do open action");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        try {
            Activity activity = VoiceManager.instance().getCurrentActivity();
            if (!(activity == null || activity.isFinishing())) {
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_click_default), 23));
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_left_default), 21));
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_right_default), 22));
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_up_default), 19));
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_down_default), 20));
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_back_default), 4));
                actions.add(createVoiceActionbyKeyEvent(getString(C0508R.string.voice_key_menu_default), 82));
            }
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "do open action exception = ", e);
        }
        return actions;
    }

    private String getString(int resId) {
        return this.mContext.getString(resId);
    }

    AbsVoiceAction createVoiceActionbyKeyEvent(String keyWord, final int keyCode) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "create voice action keyword = " + keyWord);
        }
        return new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(4, keyWord)) {

            class C12531 implements Runnable {
                C12531() {
                }

                public void run() {
                    try {
                        SystemKeyListener.mInstrumentation.sendKeyDownUpSync(keyCode);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            protected boolean dispatchVoiceEvent(VoiceEvent arg0) {
                PingBackUtils.setTabSrc("其他");
                ThreadUtils.execute(new C12531());
                return true;
            }
        };
    }
}
