package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.qiyi.tv.client.impl.ParamsHelper;

final class e<T extends Parcelable> extends d<T> {
    public e(Context context, int i, int i2) {
        super(context, i, i2);
    }

    public final Bundle a() {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, this.a);
        ParamsHelper.setResultData(bundle, this.a);
        return bundle;
    }
}
