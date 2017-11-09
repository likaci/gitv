package com.gala.video.app.epg.voice.config;

import com.gala.video.app.epg.voice.feature.global.OpenAllAppListener;
import com.gala.video.app.epg.voice.feature.global.OpenPlayHistoryListener;
import com.gala.video.app.epg.voice.feature.global.OpenSearchListener;
import com.gala.video.app.epg.voice.feature.setting.OpenAccountListener;
import com.gala.video.app.epg.voice.feature.setting.OpenBackgroundListener;
import com.gala.video.app.epg.voice.feature.setting.OpenFeedbackListener;
import com.gala.video.app.epg.voice.feature.setting.OpenMultiScreenListener;
import com.gala.video.app.epg.voice.function.OpenAppListener;
import com.gala.video.app.epg.voice.function.OpenPlayListener;
import com.gala.video.app.epg.voice.function.OpenSearchResultListener;
import com.gala.video.app.epg.voice.function.ScreenSaverListener;
import com.gala.video.app.epg.voice.function.SystemKeyListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import java.util.ArrayList;
import java.util.List;

public class VoiceAppConfig {
    public static List<VoiceListener> getVoiceListenerList() {
        return initVoiceFeatures(new ArrayList());
    }

    private static List<VoiceListener> initVoiceFeatures(List<VoiceListener> list) {
        list.add(new OpenAllAppListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenPlayHistoryListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenSearchResultListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenAccountListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenBackgroundListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenFeedbackListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenMultiScreenListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new ScreenSaverListener(AppRuntimeEnv.get().getApplicationContext(), 10000));
        list.add(new SystemKeyListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenAppListener(AppRuntimeEnv.get().getApplicationContext(), 20000));
        list.add(new OpenSearchListener(AppRuntimeEnv.get().getApplicationContext(), 30000));
        list.add(new OpenPlayListener(AppRuntimeEnv.get().getApplicationContext(), 30000));
        return list;
    }
}
