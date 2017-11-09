package com.gala.video.app.epg.web.subject.play;

import com.gala.sdk.player.ISdkError;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.ErrorUtils;

public class PlayerErrorUtils {
    public static boolean onDianBoError(ISdkError error) {
        return (error != null && error.getModule() == 10000 && error.getCode() == 1000) || ErrorUtils.isVipVideoAuthError(error);
    }

    public static boolean handleNetWorkError(ISdkError error) {
        int errCode = error.getCode();
        CharSequence serverCode = error.getServerCode();
        int httpCode = error.getHttpCode();
        switch (error.getModule()) {
            case 101:
                if (!StringUtils.equals("502", String.valueOf(errCode)) && !StringUtils.equals("102", String.valueOf(errCode)) && !StringUtils.equals("112", String.valueOf(errCode))) {
                    return false;
                }
                CharSequence secondErrCode = ErrorUtils.parseSecondCodeFromPumaError(error);
                if (StringUtils.equals(secondErrCode, "null") || StringUtils.isEmpty(secondErrCode)) {
                    return true;
                }
                return false;
            case 201:
            case 202:
            case 203:
            case ISdkError.MODULE_SERVER_TV /*205*/:
                if (StringUtils.isEmpty(serverCode) && StringUtils.equals("-50", String.valueOf(httpCode))) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
