package com.gala.video.app.epg.uikit.item;

import com.gala.tvapi.type.UserType;
import com.gala.video.app.epg.uikit.contract.UCenterItemContract.Presenter;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.AccountLoginHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.text.SimpleDateFormat;

public class UCenterItem extends Item implements Presenter {
    private static final String LOG_TAG = "EPG/UCenterItem";

    public int getType() {
        return 220;
    }

    public int getWidth() {
        return -1;
    }

    public String getLoginTips() {
        IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String value = "";
        if (dynamicQDataModel != null) {
            return dynamicQDataModel.getLoginButtonBelowText();
        }
        return value;
    }

    public String getUserName() {
        CharSequence phone = GetInterfaceTools.getIGalaAccountManager().getUserPhone();
        if (StringUtils.isEmpty(phone)) {
            return LoginConstant.NICKNAME_PRE + GetInterfaceTools.getIGalaAccountManager().getUserName();
        }
        return LoginConstant.NICKNAME_PRE + AccountLoginHelper.getUserPhone(phone);
    }

    public String getUid() {
        return GetInterfaceTools.getIGalaAccountManager().getUID();
    }

    public String getStatus() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType == null) {
            return ResourceUtil.getStr(C1632R.string.str_my_novip);
        }
        if (userType.isLitchi()) {
            return getVipDeadLine();
        }
        if (userType.isPlatinum()) {
            if (userType.isExpire()) {
                return ResourceUtil.getStr(C1632R.string.vip_expire_tip);
            }
            return getVipDeadLine();
        } else if (userType.isLitchiOverdue()) {
            return ResourceUtil.getStr(C1632R.string.my_overdue_vip);
        } else {
            return ResourceUtil.getStr(C1632R.string.str_my_novip);
        }
    }

    private String getVipDeadLine() {
        long timeStamp = GetInterfaceTools.getIGalaAccountManager().getVipTimeStamp();
        int vipOverdueDay = getVipOverDay(timeStamp);
        if (vipOverdueDay > 0 && vipOverdueDay < 10) {
            return vipOverdueDay + "天后过期";
        }
        LogUtils.m1570d(LOG_TAG, ">>>>> timeStamp = ", Long.valueOf(timeStamp));
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Long(timeStamp)) + "过期";
    }

    private int getVipOverDay(long timeStamp) {
        if (timeStamp <= 0) {
            return -1;
        }
        return (int) Math.ceil((double) (((float) (timeStamp - DeviceUtils.getServerTimeMillis())) / 8.64E7f));
    }

    public boolean isLogin() {
        return GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
    }

    public boolean isVip() {
        return GetInterfaceTools.getIGalaAccountManager().isVip();
    }
}
