package com.gala.video.lib.share.ifmanager.bussnessIF.errorcode;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IErrorCodeProvider extends IInterfaceWrapper {

    public interface Callback {
        void onException(Exception exception, String str, String str2);

        void onSuccess(String str);
    }

    public static abstract class Wrapper implements IErrorCodeProvider {
        public Object getInterface() {
            return this;
        }

        public static IErrorCodeProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IErrorCodeProvider)) {
                return null;
            }
            return (IErrorCodeProvider) wrapper;
        }
    }

    ErrorCodeModel getErrorCodeModel(String str);

    ErrorCodeModel getErrorCodeModel(String str, String str2);

    void updateErrorCode(Callback callback);
}
