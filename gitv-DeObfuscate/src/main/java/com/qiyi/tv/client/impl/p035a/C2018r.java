package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.feature.p033a.C1989a;
import com.qiyi.tv.client.feature.viprights.VipRightsManager;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class C2018r implements VipRightsManager {
    private final Context f2125a;

    public C2018r(Context context) {
        this.f2125a = context;
    }

    public final Result<Integer> getActivationState() {
        int i = 0;
        Bundle a = C1989a.m1618a(null, C1994b.m1629a(this.f2125a, TargetType.TARGET_VIP_RIGHTS, 20003, DataType.DATA_ACTIVATION_STATE));
        int parseResultCode = ParamsHelper.parseResultCode(a);
        if (parseResultCode == 0) {
            i = ParamsHelper.parseActivationState(a);
        }
        return new Result(parseResultCode, Integer.valueOf(i));
    }

    public final int openActivationPage() {
        return ParamsHelper.parseResultCode(C1989a.m1618a(null, C1994b.m1628a(this.f2125a, TargetType.TARGET_VIP_RIGHTS, OperationType.OP_OPEN_ACTIVATION_PAGE)));
    }

    public final Result<String> checkActivationCode(String code) {
        Bundle bundle = new Bundle();
        ParamsHelper.setActivateCode(bundle, code);
        Bundle a = C1989a.m1618a(bundle, C1994b.m1628a(this.f2125a, TargetType.TARGET_VIP_RIGHTS, 20003));
        Object obj = "";
        int parseResultCode = ParamsHelper.parseResultCode(a);
        if (parseResultCode == 0) {
            obj = ParamsHelper.parseString(a);
        }
        return new Result(parseResultCode, obj);
    }
}
