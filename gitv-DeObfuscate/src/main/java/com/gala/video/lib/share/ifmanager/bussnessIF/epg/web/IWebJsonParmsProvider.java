package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public interface IWebJsonParmsProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IWebJsonParmsProvider {
        public Object getInterface() {
            return this;
        }

        public static IWebJsonParmsProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IWebJsonParmsProvider)) {
                return null;
            }
            return (IWebJsonParmsProvider) wrapper;
        }
    }

    WebViewDataImpl getDefaultDataImpl();

    String getJson();
}
