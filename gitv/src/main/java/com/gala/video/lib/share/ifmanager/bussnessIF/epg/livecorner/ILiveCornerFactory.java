package com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface ILiveCornerFactory extends IInterfaceWrapper {

    public interface LiveCornerListener {
        void showBefore();

        void showEnd();

        void showPlaying();
    }

    public static abstract class Wrapper implements ILiveCornerFactory {
        public Object getInterface() {
            return this;
        }

        public static ILiveCornerFactory asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof ILiveCornerFactory)) {
                return null;
            }
            return (ILiveCornerFactory) wrapper;
        }
    }

    void end();

    void start(ChannelLabel channelLabel, LiveCornerListener liveCornerListener);
}
