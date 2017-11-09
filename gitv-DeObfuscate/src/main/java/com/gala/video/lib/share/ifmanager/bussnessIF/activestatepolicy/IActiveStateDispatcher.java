package com.gala.video.lib.share.ifmanager.bussnessIF.activestatepolicy;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IActiveStateDispatcher extends IInterfaceWrapper {

    public static abstract class Wrapper implements IActiveStateDispatcher, IActiveStateChangeListener {
        public Object getInterface() {
            return this;
        }

        public static IActiveStateDispatcher asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IActiveStateDispatcher)) {
                return null;
            }
            return (IActiveStateDispatcher) wrapper;
        }
    }

    void notifyKeyEvent();

    void resister(IActiveStateChangeListener iActiveStateChangeListener);

    void unResister(IActiveStateChangeListener iActiveStateChangeListener);
}
