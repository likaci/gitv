package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import com.gala.sdk.player.IPlayerProfile;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IPlayerProfileCreator extends IInterfaceWrapper {

    public static abstract class Wrapper implements IPlayerProfileCreator {
        public Object getInterface() {
            return this;
        }

        public static IPlayerProfileCreator asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IPlayerProfileCreator)) {
                return null;
            }
            return (IPlayerProfileCreator) wrapper;
        }
    }

    IPlayerProfile createProfile();
}
