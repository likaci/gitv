package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.qiyi.tv.client.impl.ParamsHelper;

final class C2002e<T extends Parcelable> extends C2001d<T> {
    public C2002e(Context context, int i, int i2) {
        super(context, i, i2);
    }

    public final Bundle mo4355a() {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, this.a);
        ParamsHelper.setResultData(bundle, this.a);
        return bundle;
    }
}
