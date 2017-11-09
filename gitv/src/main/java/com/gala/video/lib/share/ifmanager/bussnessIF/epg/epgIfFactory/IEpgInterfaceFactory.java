package com.gala.video.lib.share.ifmanager.bussnessIF.epg.epgIfFactory;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IEpgInterfaceFactory extends IInterfaceWrapper {

    public static abstract class Wrapper implements IEpgInterfaceFactory {
        public Object getInterface() {
            return this;
        }

        public static IEpgInterfaceFactory asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IEpgInterfaceFactory)) {
                return null;
            }
            return (IEpgInterfaceFactory) wrapper;
        }
    }

    IInterfaceWrapper getEpgInterface(String str);
}
