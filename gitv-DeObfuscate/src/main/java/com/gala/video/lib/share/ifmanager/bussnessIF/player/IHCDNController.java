package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IHCDNController extends IInterfaceWrapper {

    public static abstract class Wrapper implements IHCDNController {
        public Object getInterface() {
            return this;
        }

        public static IHCDNController asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IHCDNController)) {
                return null;
            }
            return (IHCDNController) wrapper;
        }
    }

    void initialize();
}
