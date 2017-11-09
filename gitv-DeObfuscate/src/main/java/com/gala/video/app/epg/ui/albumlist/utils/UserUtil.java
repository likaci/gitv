package com.gala.video.app.epg.ui.albumlist.utils;

import com.gala.tvapi.type.UserType;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.ResourceUtil;

public class UserUtil {
    private static final String TAG = "EPG/album4/UserUtil";

    public static boolean isLogin() {
        return GetInterfaceTools.getIGalaAccountManager().isLogin(ResourceUtil.getContext());
    }

    public static String getUserId() {
        return isLogin() ? getLoginUserId() : getLogoutUserId();
    }

    public static String getCookie() {
        return isLogin() ? getLoginCookie() : getLogoutUserId();
    }

    public static String getLoginCookie() {
        return GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
    }

    public static String getLogoutCookie() {
        return getLogoutUserId();
    }

    public static String getLoginUserId() {
        return GetInterfaceTools.getIGalaAccountManager().getUID();
    }

    public static String getLogoutUserId() {
        return AppRuntimeEnv.get().getDefaultUserId();
    }

    public static boolean isOTTVip() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType == null) {
            LogUtils.m1571e(TAG, "isOTTVip---userType=null");
            return false;
        }
        boolean litchi = userType.isLitchi();
        boolean platinum = userType.isPlatinum();
        LogUtils.m1571e(TAG, "isOTTVip---litchi=" + litchi + "---platinum=" + platinum);
        if (litchi || platinum) {
            return true;
        }
        return false;
    }

    public static String getUserTypeText() {
        CharSequence cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        LogUtils.m1568d(TAG, "updateVipViewText() --- cookie = " + cookie);
        if (StringUtils.isEmpty(cookie)) {
            return ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP;
        }
        LogUtils.m1568d(TAG, "updateVipViewText() --- 账号已经登录");
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        boolean isAccountExpire = userType == null ? false : userType.isExpire();
        LogUtils.m1568d(TAG, "updateVipViewText() --- isAccountExpire = " + isAccountExpire);
        if (isOTTVip() || isAccountExpire) {
            return ActionBarDataFactory.TOP_BAR_TIME_NAME_RENEW_VIP;
        }
        return ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP;
    }
}
