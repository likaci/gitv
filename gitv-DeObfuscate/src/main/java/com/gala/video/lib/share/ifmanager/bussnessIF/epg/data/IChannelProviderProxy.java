package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IChannelProviderProxy extends IInterfaceWrapper {

    public static abstract class Wrapper implements IChannelProviderProxy {
        public Object getInterface() {
            return this;
        }

        public static IChannelProviderProxy asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IChannelProviderProxy)) {
                return null;
            }
            return (IChannelProviderProxy) wrapper;
        }
    }

    Channel getChannelById(int i);
}
