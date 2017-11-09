package com.gala.video.lib.share.ifmanager.bussnessIF.epg.homeconstants;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IHomeConstants extends IInterfaceWrapper {

    public static abstract class Wrapper implements IHomeConstants {
        public Object getInterface() {
            return this;
        }

        public static IHomeConstants asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IHomeConstants)) {
                return null;
            }
            return (IHomeConstants) wrapper;
        }
    }

    boolean isIsStartPreViewFinished();
}
