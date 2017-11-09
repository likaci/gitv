package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;

abstract class h<T> extends Command {
    protected int a = 1;
    protected ArrayList<T> f851a;

    public abstract Bundle a();

    public h(Context context, int i, int i2) {
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
            bundle = c.a().a(bundle);
            int parseResultCode = ParamsHelper.parseResultCode(bundle);
            arrayList = (ArrayList) ParamsHelper.parseResultData(bundle);
            i = parseResultCode;
        } catch (Throwable e) {
            Log.w("SimpleDataListCommand", "process() error!", e);
            i = Utils.parseErrorCode(e);
            arrayList = null;
        }
        this.a = i;
        this.f851a = arrayList;
        Log.d("SimpleDataListCommand", "process() mCode=" + this.a + ", mList=" + this.f851a);
        return a();
    }
}
