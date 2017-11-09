package com.gala.video.lib.share.ifmanager.bussnessIF.epg.homefocusimagead;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IHomeFocusImageAdTask extends IInterfaceWrapper {

    public static abstract class Wrapper implements IHomeFocusImageAdTask {
        public Object getInterface() {
            return this;
        }

        public static IHomeFocusImageAdTask asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IHomeFocusImageAdTask)) {
                return null;
            }
            return (IHomeFocusImageAdTask) wrapper;
        }
    }

    void execute();
}
