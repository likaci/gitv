package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

final class C2003f extends C2001d<String> {
    public C2003f(Context context) {
        super(context, TargetType.TARGET_PICTURE, DataType.DATA_URL);
    }

    public final Bundle mo4355a() {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, this.a);
        ParamsHelper.setResultDataOfArrayString(bundle, this.a);
        return bundle;
    }
}
