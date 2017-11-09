package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IGalaVipManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements IGalaVipManager {
        public Object getInterface() {
            return this;
        }

        public static IGalaVipManager asInterface(Object wrapper) {
            TraceEx.beginSection("IGalaAccountManager.asInterface");
            if (wrapper == null || !(wrapper instanceof IGalaVipManager)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IGalaVipManager) wrapper;
        }
    }

    int getAccountActivationState();

    String getActivationAccount();

    int getActivationFeedbackState();

    int getActivationState();

    String getPingBackVipAct();

    boolean needQueryActivationStateFromServer();

    boolean needShowActivationPage();

    void setActivationFeedbackState(int i);

    void setPingBackVipAct();
}
