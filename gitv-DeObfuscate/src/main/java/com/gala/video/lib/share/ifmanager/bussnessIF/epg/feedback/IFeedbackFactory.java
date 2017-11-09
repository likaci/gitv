package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.uikit.data.data.Model.ApiExceptionModel;

public interface IFeedbackFactory extends IInterfaceWrapper {

    public static abstract class Wrapper implements IFeedbackFactory {
        public Object getInterface() {
            return this;
        }

        public static IFeedbackFactory asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IFeedbackFactory)) {
                return null;
            }
            return (IFeedbackFactory) wrapper;
        }
    }

    FeedBackModel createFeedBack(ApiException apiException);

    FeedBackModel createFeedBack(ApiExceptionModel apiExceptionModel);
}
