package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

final class f extends d<String> {
    public f(Context context) {
        super(context, TargetType.TARGET_PICTURE, DataType.DATA_URL);
    }

    public final Bundle a() {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, this.a);
        ParamsHelper.setResultDataOfArrayString(bundle, this.a);
        return bundle;
    }
}
