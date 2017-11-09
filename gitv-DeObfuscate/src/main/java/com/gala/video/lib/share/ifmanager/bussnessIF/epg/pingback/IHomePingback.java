package com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import java.util.HashMap;

public interface IHomePingback extends IInterfaceWrapper {

    public static abstract class Wrapper implements IHomePingback {
        public Object getInterface() {
            return this;
        }

        public static IHomePingback asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IHomePingback)) {
                return null;
            }
            return (IHomePingback) wrapper;
        }
    }

    IHomePingback addItem(String str, String str2);

    IHomePingback addItem(HashMap<String, String> hashMap);

    IHomePingback createPingback(Object obj);

    void post();

    IHomePingback setOthersNull();
}
