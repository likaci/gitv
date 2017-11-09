package com.gala.video.lib.share.ifmanager.bussnessIF.player.utils;

import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.error.ErrorConstants;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.Arrays;
import java.util.List;

public class ErrorUtils {
    private static final String TAG = "ErrorUtils";

    public static String parseSecondCodeFromPumaError(ISdkError error) {
        if (error == null) {
            return "";
        }
        if (error.getModule() != 101) {
            return "";
        }
        CharSequence serverCode = error.getServerCode();
        CharSequence finalCode = serverCode;
        if (!StringUtils.isEmpty(serverCode) && serverCode.contains("-")) {
            List<String> errcodeList = Arrays.asList(serverCode.split("-"));
            if (errcodeList.size() != 2) {
                return finalCode;
            }
            String secondCode = (String) errcodeList.get(1);
            if (error.getCode() == 104) {
                return secondCode;
            }
            return finalCode;
        } else if (error.getCode() == 655360) {
            return error.getExtra1();
        } else {
            return finalCode;
        }
    }

    public static String getPfEc(ISdkError error) {
        String pfEc = "";
        if (error == null) {
            return pfEc;
        }
        if (error.getModule() == 201 || error.getModule() == 203 || error.getModule() == 202) {
            pfEc = error.getServerCode();
        } else {
            pfEc = String.valueOf(error.getCode());
        }
        return pfEc;
    }

    public static boolean isVipVideoAuthError(ISdkError error) {
        if (error == null) {
            return false;
        }
        if (error.getModule() == 201) {
            return isVipVideoAuthErrorCode(error.getServerCode());
        }
        if (error.getModule() == 101 && "504".equals(String.valueOf(error.getCode()))) {
            return isVipVideoAuthErrorCode(parseSecondCodeFromPumaError(error));
        }
        return false;
    }

    private static boolean isVipVideoAuthErrorCode(String errorCode) {
        return ErrorConstants.API_ERR_CODE_Q302.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q304.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q305.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q310.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q505.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q503.equals(errorCode);
    }

    public static boolean isVipAccountError(ISdkError error) {
        boolean ret = false;
        if (error == null) {
            return false;
        }
        String serverCode = error.getServerCode();
        switch (error.getModule()) {
            case 201:
                if (ErrorConstants.API_ERRO_CODE_Q312.equals(serverCode) || ErrorConstants.API_ERRO_CODE_Q311.equals(serverCode)) {
                    ret = true;
                } else {
                    ret = false;
                }
                break;
            case 203:
                if (ErrorConstants.API_ERR_CODE_TOO_MANY_USERS.equals(serverCode) || ErrorConstants.API_ERR_CODE_VIP_ACCOUNT_BANNED.equals(serverCode) || ErrorConstants.API_ERR_CODE_PASSWORD_CHANGED.equals(serverCode)) {
                    ret = true;
                } else {
                    ret = false;
                }
                break;
        }
        return ret;
    }
}
