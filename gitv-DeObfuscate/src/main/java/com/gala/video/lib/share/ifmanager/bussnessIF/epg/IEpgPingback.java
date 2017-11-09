package com.gala.video.lib.share.ifmanager.bussnessIF.epg;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IEpgPingback extends IInterfaceWrapper {

    public static abstract class Wrapper implements IEpgPingback {
        public Object getInterface() {
            return this;
        }

        public static IEpgPingback asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IEpgPingback)) {
                return null;
            }
            return (IEpgPingback) wrapper;
        }
    }

    void onLoadUser(boolean z);

    void onSaveTvHistory(boolean z);
}
