package com.gala.video.lib.share.ifimpl.errorcode;

import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.IErrorCodeProvider;

public class ErrorCodeProviderCreater {
    public static IErrorCodeProvider create() {
        return new ErrorCodeProvider();
    }
}
