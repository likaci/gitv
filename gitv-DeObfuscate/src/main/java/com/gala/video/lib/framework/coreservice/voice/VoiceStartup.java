package com.gala.video.lib.framework.coreservice.voice;

import com.gala.tv.voice.service.ResourceSemanticTranslator.Filter;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.tv.voice.service.VoiceTestServer;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.lang.reflect.Field;
import java.util.List;

public class VoiceStartup {
    private static final String TAG = "VoiceStartup";

    public static synchronized void initialize(List<VoiceListener> list, IVoiceStartUpPrepareListener listener) {
        synchronized (VoiceStartup.class) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "voice init");
            }
            VoiceManager.instance().initialize(AppRuntimeEnv.get().getApplicationContext());
            initVoiceFeatures(list, listener);
            initVoiceTest();
        }
    }

    private static void initVoiceFeatures(final List<VoiceListener> list, final IVoiceStartUpPrepareListener listener) {
        new Thread8K(new Runnable() {

            class C16301 implements Filter {
                C16301() {
                }

                public boolean access(Field field) {
                    String arrayName = field.getName();
                    return arrayName.startsWith("voice_") && arrayName.endsWith("_array");
                }
            }

            public void run() {
                listener.onPrepare(new C16301());
                for (VoiceListener listener : list) {
                    VoiceManager.instance().addListener(listener.getPriority(), listener);
                }
            }
        }, TAG).start();
    }

    private static void initVoiceTest() {
        if ("true".equalsIgnoreCase(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_VOICE_TEST, "false"))) {
            new VoiceTestServer(AppRuntimeEnv.get().getApplicationContext(), BuildCache.getInstance().getString(BuildConstance.APK_PACKAGE_NAME, AppEnvConstant.DEF_PKG_NAME)).start();
        }
    }
}
