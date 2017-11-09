package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

final class C2006i<T extends Parcelable> extends C2005h<T> {
    public C2006i(Context context) {
        super(context, TargetType.TARGET_APP_STORE_APPINFO, DataType.DATA_APP_INFO_LIST);
    }

    public final Bundle mo4356a() {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, this.a);
        ParamsHelper.setResultData(bundle, this.a);
        return bundle;
    }
}
