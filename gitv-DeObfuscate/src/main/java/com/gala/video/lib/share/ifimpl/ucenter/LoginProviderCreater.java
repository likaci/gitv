package com.gala.video.lib.share.ifimpl.ucenter;

import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.ILoginProvider;

public class LoginProviderCreater {
    public static ILoginProvider create() {
        return new LoginProvider();
    }
}
