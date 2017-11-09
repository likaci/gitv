package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;

abstract class d<T> extends Command {
    protected int a = 1;
    protected ArrayList<T> f850a;

    public abstract Bundle a();

    public d(Context context, int i, int i2) {
        super(context, i, 20003, i2);
    }

    public Bundle process(Bundle params) {
        Bundle bundle = new Bundle();
        Utils.copyBundle(bundle, params);
        ParamsHelper.setOperationTarget(bundle, getTarget());
        ParamsHelper.setOperationType(bundle, getOperationType());
        ParamsHelper.setOperationDataType(bundle, getDataType());
        int parseMaxCount = ParamsHelper.parseMaxCount(bundle);
        int a = q.a();
        Object obj = null;
        Log.d("DataListCommand", "process() maxCount=" + parseMaxCount + ", pageSize=" + a);
        Bundle a2;
        int parseResultCode;
        int i;
        if (parseMaxCount > a) {
            int ceil = (int) Math.ceil(((double) parseMaxCount) / ((double) a));
            int i2 = 1;
            while (i2 <= ceil) {
                Object obj2;
                int i3 = (i2 != ceil || i2 == 1) ? a : parseMaxCount % a;
                ParamsHelper.setPageNo(bundle, i2);
                ParamsHelper.setPageSize(bundle, a);
                ParamsHelper.setMaxCount(bundle, i3);
                try {
                    a2 = c.a().a(bundle);
                    parseResultCode = ParamsHelper.parseResultCode(a2);
                    obj2 = (ArrayList) ParamsHelper.parseResultData(a2);
                    i = parseResultCode;
                } catch (Throwable e) {
                    Log.w("DataListCommand", "process() error!", e);
                    Object obj3 = obj;
                    i = Utils.parseErrorCode(e);
                    obj2 = obj3;
                }
                Log.d("DataListCommand", "process() pageNo=" + i2 + ", pageMaxCount=" + i3 + ", pageCode=" + i);
                if (i == 0) {
                    if (this.f850a == null) {
                        this.f850a = obj2;
                        this.a = i;
                        Log.d("DataListCommand", "process() first one mCode=" + this.a + ", mList=" + this.f850a);
                    } else if (obj2 != null) {
                        this.f850a.addAll(obj2);
                    }
                    if (obj2 == null || obj2.size() < a) {
                        Log.d("DataListCommand", "process() reach end! pageData=" + obj2);
                        break;
                    }
                    i2++;
                    obj = obj2;
                } else if (this.f850a == null) {
                    this.f850a = obj2;
                    this.a = i;
                    Log.d("DataListCommand", "process() fist one error mCode=" + this.a + ", mList=" + this.f850a);
                }
            }
        } else {
            ArrayList arrayList;
            ParamsHelper.setPageNo(bundle, 1);
            ParamsHelper.setPageSize(bundle, parseMaxCount);
            try {
                a2 = c.a().a(bundle);
                parseResultCode = ParamsHelper.parseResultCode(a2);
                arrayList = (ArrayList) ParamsHelper.parseResultData(a2);
                i = parseResultCode;
            } catch (Throwable e2) {
                Log.w("DataListCommand", "process() error!", e2);
                i = Utils.parseErrorCode(e2);
                arrayList = null;
            }
            Log.d("DataListCommand", "process() once pageCode=" + i + ", pageData=" + arrayList);
            this.a = i;
            this.f850a = arrayList;
        }
        Log.d("DataListCommand", "process() mCode=" + this.a + ", mList=" + this.f850a);
        return a();
    }
}
