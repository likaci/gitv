package com.gala.video.lib.share.ifmanager.bussnessIF.epg;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IStartupDataLoader extends IInterfaceWrapper {

    public static abstract class Wrapper implements IStartupDataLoader {
        public Object getInterface() {
            return this;
        }

        public static IStartupDataLoader asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IStartupDataLoader)) {
                return null;
            }
            return (IStartupDataLoader) wrapper;
        }
    }

    void forceLoad(boolean z);

    boolean isDataLoading();

    void load(boolean z);

    void stop();
}
