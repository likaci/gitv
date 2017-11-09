package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;

abstract class C2005h<T> extends Command {
    protected int f2110a = 1;
    protected ArrayList<T> f2111a;

    public abstract Bundle mo4356a();

    public C2005h(Context context, int i, int i2) {
        super(context, TargetType.TARGET_APP_STORE_APPINFO, 20003, DataType.DATA_APP_INFO_LIST);
    }

    public Bundle process(Bundle params) {
        ArrayList arrayList;
        int i;
        Bundle bundle = new Bundle();
        Utils.copyBundle(bundle, params);
        ParamsHelper.setOperationTarget(bundle, getTarget());
        ParamsHelper.setOperationType(bundle, getOperationType());
        ParamsHelper.setOperationDataType(bundle, getDataType());
        try {
            bundle = C2000c.m1643a().m1663a(bundle);
            int parseResultCode = ParamsHelper.parseResultCode(bundle);
            arrayList = (ArrayList) ParamsHelper.parseResultData(bundle);
            i = parseResultCode;
        } catch (Throwable e) {
            Log.m1625w("SimpleDataListCommand", "process() error!", e);
            i = Utils.parseErrorCode(e);
            arrayList = null;
        }
        this.f2110a = i;
        this.f2111a = arrayList;
        Log.m1620d("SimpleDataListCommand", "process() mCode=" + this.f2110a + ", mList=" + this.f2111a);
        return mo4356a();
    }
}
