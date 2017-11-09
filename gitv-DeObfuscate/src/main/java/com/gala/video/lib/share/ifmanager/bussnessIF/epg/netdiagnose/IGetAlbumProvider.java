package com.gala.video.lib.share.ifmanager.bussnessIF.epg.netdiagnose;

import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IGetAlbumProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IGetAlbumProvider {
        public Object getInterface() {
            return this;
        }

        public static IGetAlbumProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IGetAlbumProvider)) {
                return null;
            }
            return (IGetAlbumProvider) wrapper;
        }
    }

    public interface Callback {
        void onResult(IOneAlbumProvider iOneAlbumProvider);
    }

    void getAlbumProviderAsync(Callback callback);
}
