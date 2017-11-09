package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.feature.account.AccountManager;
import com.qiyi.tv.client.feature.account.UserInfo;
import com.qiyi.tv.client.feature.account.VipInfo;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class a implements AccountManager {
    private final Context a;

    public a(Context context) {
        this.a = context;
    }

    public final int login(UserInfo userInfo) {
        return login(userInfo, true);
    }

    public final int login(UserInfo userInfo, boolean fromThird) {
        Bundle bundle = new Bundle();
        ParamsHelper.setLoginUserInfo(bundle, userInfo);
        ParamsHelper.setFromThirdUser(bundle, fromThird);
        return ParamsHelper.parseResultCode(com.qiyi.tv.client.feature.a.a.a(bundle, b.a(this.a, 10002, OperationType.OP_LOGIN)));
    }

    public final int getStatus() {
        Bundle a = com.qiyi.tv.client.feature.a.a.a(null, b.a(this.a, 10002, 20003, DataType.DATA_ACCOUNT_STATUS));
        if (ParamsHelper.parseResultCode(a) == 0) {
            return ParamsHelper.parseLoginStatus(a);
        }
        return 0;
    }

    public final int logout() {
        return ParamsHelper.parseResultCode(com.qiyi.tv.client.feature.a.a.a(null, b.a(this.a, 10002, OperationType.OP_LOGOUT)));
    }

    public final int mergeAnonymousHistory() {
        return ParamsHelper.parseResultCode(com.qiyi.tv.client.feature.a.a.a(null, b.a(this.a, 10002, OperationType.OP_MERGE_HISTORY)));
    }

    public final int mergeAnonymousFavorite() {
        return ParamsHelper.parseResultCode(com.qiyi.tv.client.feature.a.a.a(null, b.a(this.a, 10002, OperationType.OP_MERGE_FAVORITE)));
    }

    public final Result<VipInfo> getVipInfo() {
        Bundle a = com.qiyi.tv.client.feature.a.a.a(null, b.a(this.a, 10002, 20003, DataType.DATA_VIP_INFO));
        return new Result(ParamsHelper.parseResultCode(a), ParamsHelper.parseVipInfo(a));
    }
}
