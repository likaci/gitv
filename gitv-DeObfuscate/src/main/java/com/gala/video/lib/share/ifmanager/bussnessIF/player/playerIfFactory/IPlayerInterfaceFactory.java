package com.gala.video.lib.share.ifmanager.bussnessIF.player.playerIfFactory;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IPlayerInterfaceFactory extends IInterfaceWrapper {

    public static abstract class Wrapper implements IPlayerInterfaceFactory {
        public Object getInterface() {
            return this;
        }

        public static IPlayerInterfaceFactory asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IPlayerInterfaceFactory)) {
                return null;
            }
            return (IPlayerInterfaceFactory) wrapper;
        }
    }

    IInterfaceWrapper getPlayerInterface(String str);
}
