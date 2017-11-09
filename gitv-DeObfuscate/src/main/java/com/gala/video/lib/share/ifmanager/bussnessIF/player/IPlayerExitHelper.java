package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IPlayerExitHelper extends IInterfaceWrapper {

    public static abstract class Wrapper implements IPlayerExitHelper {
        public Object getInterface() {
            return this;
        }

        public static IPlayerExitHelper asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IPlayerExitHelper)) {
                return null;
            }
            return (IPlayerExitHelper) wrapper;
        }
    }

    void clearCarouselSharePre(Context context);
}
