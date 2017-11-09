package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account;

import android.content.Context;
import com.gala.tvapi.type.UserType;

public interface IGalaAccountLocal {
    String getAuthCookie();

    boolean getExpired();

    String getHu();

    boolean getIsLitchiVipForH5();

    String getUID();

    String getUserAccount();

    String getUserName();

    String getUserPhone();

    UserType getUserType();

    int getUserTypeForH5();

    String getVipDate();

    long getVipTimeStamp();

    boolean isLogin(Context context);

    boolean isVip();

    void setAccountType();
}
