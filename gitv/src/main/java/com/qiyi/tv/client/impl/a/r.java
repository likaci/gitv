package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.feature.a.a;
import com.qiyi.tv.client.feature.viprights.VipRightsManager;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class r implements VipRightsManager {
    private final Context a;

    public r(Context context) {
        this.a = context;
    }

    public final Result<Integer> getActivationState() {
        int i = 0;
        Bundle a = a.a(null, b.a(this.a, TargetType.TARGET_VIP_RIGHTS, 20003, DataType.DATA_ACTIVATION_STATE));
        int parseResultCode = ParamsHelper.parseResultCode(a);
        if (parseResultCode == 0) {
            i = ParamsHelper.parseActivationState(a);
        }
        return new Result(parseResultCode, Integer.valueOf(i));
    }

    public final int openActivationPage() {
        return ParamsHelper.parseResultCode(a.a(null, b.a(this.a, TargetType.TARGET_VIP_RIGHTS, OperationType.OP_OPEN_ACTIVATION_PAGE)));
    }

    public final Result<String> checkActivationCode(String code) {
        Bundle bundle = new Bundle();
        ParamsHelper.setActivateCode(bundle, code);
        Bundle a = a.a(bundle, b.a(this.a, TargetType.TARGET_VIP_RIGHTS, 20003));
        Object obj = "";
        int parseResultCode = ParamsHelper.parseResultCode(a);
        if (parseResultCode == 0) {
            obj = ParamsHelper.parseString(a);
        }
        return new Result(parseResultCode, obj);
    }
}
