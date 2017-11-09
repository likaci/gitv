package com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IOpenBroadcastActionHolder extends IInterfaceWrapper {

    public static abstract class Wrapper implements IOpenBroadcastActionHolder {
        public Object getInterface() {
            return this;
        }

        public static IOpenBroadcastActionHolder asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IOpenBroadcastActionHolder)) {
                return null;
            }
            return (IOpenBroadcastActionHolder) wrapper;
        }
    }

    ActionHolder[] getActionHolder();
}
