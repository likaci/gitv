package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.IGalaAccountCloud;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.IGalaAccountLocal;
import com.gala.video.lib.share.utils.TraceEx;

public interface IGalaAccountManager extends IGalaAccountCloud, IGalaAccountLocal, IInterfaceWrapper {

    public static abstract class Wrapper implements IGalaAccountManager {
        public Object getInterface() {
            return this;
        }

        public static IGalaAccountManager asInterface(Object wrapper) {
            TraceEx.beginSection("IGalaAccountManager.asInterface");
            if (wrapper == null || !(wrapper instanceof IGalaAccountManager)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IGalaAccountManager) wrapper;
        }
    }
}
