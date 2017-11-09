package com.qiyi.tv.client.feature.account;

import com.qiyi.tv.client.Result;

public interface AccountManager {
    public static final int ACCOUNT_STATUS_FAIL = 2;
    public static final int ACCOUNT_STATUS_LOGINED = 1;
    public static final int ACCOUNT_STATUS_UNKNOWN = 0;

    int getStatus();

    Result<VipInfo> getVipInfo();

    int login(UserInfo userInfo);

    int login(UserInfo userInfo, boolean z);

    int logout();

    int mergeAnonymousFavorite();

    int mergeAnonymousHistory();
}
