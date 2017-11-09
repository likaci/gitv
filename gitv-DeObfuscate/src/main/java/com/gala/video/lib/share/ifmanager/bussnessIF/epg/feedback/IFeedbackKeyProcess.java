package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import android.content.Context;
import android.view.KeyEvent;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IFeedbackKeyProcess extends IInterfaceWrapper {

    public static abstract class Wrapper implements IFeedbackKeyProcess {
        public Object getInterface() {
            return this;
        }

        public static IFeedbackKeyProcess asInterface(Object wrapper) {
            TraceEx.beginSection("IFeedbackKeyProcess.asInterface");
            if (wrapper == null || !(wrapper instanceof IFeedbackKeyProcess)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IFeedbackKeyProcess) wrapper;
        }
    }

    void dispatchKeyEvent(KeyEvent keyEvent, Context context);
}
