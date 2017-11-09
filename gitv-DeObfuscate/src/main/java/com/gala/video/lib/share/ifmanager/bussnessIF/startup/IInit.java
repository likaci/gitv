package com.gala.video.lib.share.ifmanager.bussnessIF.startup;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IInit extends IInterfaceWrapper {
    public static final int ASYNC = 101;
    public static final int PROCESS_MAIN = 0;
    public static final int PROCESS_PLAYER = 1;
    public static final int PROCESS_PUSHSERVICE = 2;
    public static final int SYNC = 100;

    public static abstract class Wrapper implements IInit {
        public Object getInterface() {
            return this;
        }

        public static IInit asInterface(Object wrapper) {
            TraceEx.beginSection("IInit.asInterface");
            if (wrapper == null || !(wrapper instanceof IInit)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IInit) wrapper;
        }
    }

    void execute(InitTaskInput initTaskInput);

    boolean isMainProcess();

    boolean isPlayerProcess();
}
