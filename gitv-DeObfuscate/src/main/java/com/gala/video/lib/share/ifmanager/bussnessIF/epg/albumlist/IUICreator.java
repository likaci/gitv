package com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;

public interface IUICreator extends IInterfaceWrapper {

    public static abstract class Wrapper implements IUICreator {
        public Object getInterface() {
            return this;
        }

        public static IUICreator asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IUICreator)) {
                return null;
            }
            return (IUICreator) wrapper;
        }
    }

    boolean isViewVisible(View view);

    Bitmap maketNoResultView(Context context, GlobalQRFeedbackPanel globalQRFeedbackPanel, ErrorKind errorKind, ApiException apiException);
}
