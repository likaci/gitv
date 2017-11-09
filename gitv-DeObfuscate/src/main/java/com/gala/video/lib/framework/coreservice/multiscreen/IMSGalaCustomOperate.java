package com.gala.video.lib.framework.coreservice.multiscreen;

import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnFlingEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnKeyChanged;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnNotifyEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnPushVideoEvent;

public interface IMSGalaCustomOperate {
    void onAdStart();

    void registerOnFlingEvent(OnFlingEvent onFlingEvent);

    void registerOnKeyChangedEvent(OnKeyChanged onKeyChanged);

    void registerOnNotifyEvent(OnNotifyEvent onNotifyEvent);

    void registerOnPushVideoEvent(OnPushVideoEvent onPushVideoEvent);

    void setMSEnable(boolean z);

    void setMSListener(IMSGalaCustomListener iMSGalaCustomListener);

    void unRegisterOnFlingEvent(OnFlingEvent onFlingEvent);

    void unRegisterOnKeyChangedEvent(OnKeyChanged onKeyChanged);

    void unRegisterOnNotifyEvent(OnNotifyEvent onNotifyEvent);

    void unRegisterOnPushVideoEvent(OnPushVideoEvent onPushVideoEvent);
}
