package com.gala.video.lib.share.ifmanager.bussnessIF.epg.devcecheck;

import com.gala.tvapi.tv2.model.ResId;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import java.util.List;

public interface IDeviceCheckProxy extends IInterfaceWrapper {

    public static abstract class Wrapper implements IDeviceCheckProxy {
        public Object getInterface() {
            return this;
        }

        public static IDeviceCheckProxy asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IDeviceCheckProxy)) {
                return null;
            }
            return (IDeviceCheckProxy) wrapper;
        }
    }

    List<ResId> getHomeResId();

    boolean isDevCheckPass();
}
